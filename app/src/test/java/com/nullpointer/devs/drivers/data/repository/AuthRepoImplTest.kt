package com.nullpointer.devs.drivers.data.repository

import com.nullpointer.devs.drivers.data.local.auth.AuthLocalDataSource
import com.nullpointer.devs.drivers.data.mappers.auth.toAuthData
import com.nullpointer.devs.drivers.data.mappers.auth.toForgotPasswordDTO
import com.nullpointer.devs.drivers.data.mappers.auth.toLoginDTO
import com.nullpointer.devs.drivers.data.mappers.auth.toRegisterDTO
import com.nullpointer.devs.drivers.data.model.auth.AuthData
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshTokenResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO
import com.nullpointer.devs.drivers.data.remote.auth.AuthRemoteDataSource
import com.nullpointer.devs.drivers.domain.model.CredentialsData
import com.nullpointer.devs.drivers.domain.model.ForgotPasswordData
import com.nullpointer.devs.drivers.domain.model.RegisterData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AuthRepoImplTest {

    private lateinit var authRepoImpl: AuthRepoImpl

    private val authLocalDataSource: AuthLocalDataSource = mockk()
    private val authRemoteDataSource: AuthRemoteDataSource = mockk()

    @Before
    fun setUp() {
        authRepoImpl = AuthRepoImpl(authLocalDataSource, authRemoteDataSource)
    }

    @Test
    fun `getAuthData emits correct auth data`() = runBlocking {
        val authData = AuthData(
            token = "token",
            refreshToken = "refreshToken",
            id = 1,
        )

        coEvery { authLocalDataSource.getAuthData() } returns flowOf(authData)

        val resultFlow = authRepoImpl.authData

        assert(resultFlow.firstOrNull() == authData)

        coVerify { authLocalDataSource.getAuthData() }
    }

    @Test
    fun `loginCredentials should call remote and save data locally`() = runBlocking {
        val credentialsData = CredentialsData(email = "email", password = "password")
        val loginDTO = credentialsData.toLoginDTO()

        val authData = AuthData(token = "token", refreshToken = "refreshToken", id = 1)

        val loginResponseDTO = LoginResponseDTO(
            token = "token",
            refreshToken = "refreshToken",
            user = LoginResponseDTO.User(
                id = 1, name = "name", email = "email", roleID = 1, isEmailVerified = true
            )
        )

        coEvery { authRemoteDataSource.login(loginDTO) } returns loginResponseDTO
        coEvery { authLocalDataSource.saveAuthData(authData) } returns Unit

        authRepoImpl.loginCredentials(credentialsData)

        coVerifySequence {
            authRemoteDataSource.login(loginDTO)
            authLocalDataSource.saveAuthData(authData)
        }
    }

    @Test
    fun `registerCredentials should call remote and save data locally`() = runBlocking {
        val registerData = RegisterData(
            name = "name", email = "email", password = "password", lastname = "lastname", birthdate = "birthdate"
        )

        val registerDTO = registerData.toRegisterDTO()
        val authData = AuthData(token = "token", refreshToken = "refreshToken", id = 1)

        val registerResponseDTO = RegisterResponseDTO(
            token = "token",
            refreshToken = "refreshToken",
            user = RegisterResponseDTO.User(
                id = 1, name = "name", email = "email", roleID = 1, isEmailVerified = true
            )
        )

        coEvery { authRemoteDataSource.register(registerDTO) } returns registerResponseDTO
        coEvery { authLocalDataSource.saveAuthData(authData) } returns Unit

        authRepoImpl.registerCredentials(registerData)

        coVerifySequence {
            authRemoteDataSource.register(registerDTO)
            authLocalDataSource.saveAuthData(authData)
        }
    }

    @Test
    fun `forgotPassword should call remote service`() = runBlocking {
        val forgotPasswordData = ForgotPasswordData(email = "email")
        val forgotPasswordDTO = forgotPasswordData.toForgotPasswordDTO()
        val forgotPasswordResponseDTO = ForgotPasswordResponseDTO(message = "message")

        coEvery { authRemoteDataSource.forgotPassword(forgotPasswordDTO) } returns forgotPasswordResponseDTO

        authRepoImpl.forgotPassword(forgotPasswordData)

        coVerify { authRemoteDataSource.forgotPassword(forgotPasswordDTO) }
    }

    @Test
    fun `refreshToken should call remote and update local storage`() = runBlocking {
        val authData = AuthData(refreshToken = "refreshToken", token = "token", id = 1)
        val refreshTokenDTO = RefreshDTO(refreshToken = "refreshToken")

        val refreshTokenResponseDTO = RefreshTokenResponseDTO(token = "newToken", refreshToken = "newRefreshToken")

        val newAuthData = authData.copy(
            token = refreshTokenResponseDTO.token,
            refreshToken = refreshTokenResponseDTO.refreshToken
        )

        coEvery { authLocalDataSource.getAuthData() } returns flowOf(authData)
        coEvery { authRemoteDataSource.refresh(refreshTokenDTO) } returns refreshTokenResponseDTO
        coEvery { authLocalDataSource.saveAuthData(newAuthData) } returns Unit

        authRepoImpl.refreshToken()

        coVerifySequence {
            authLocalDataSource.getAuthData()
            authRemoteDataSource.refresh(refreshTokenDTO)
            authLocalDataSource.saveAuthData(newAuthData)
        }
    }

    @Test
    fun `refreshToken should clear auth data when no valid token is found`() = runBlocking {
        coEvery { authLocalDataSource.getAuthData() } returns emptyFlow()
        coEvery { authLocalDataSource.clearAuthData() } returns Unit

        authRepoImpl.refreshToken()

        coVerifySequence {
            authLocalDataSource.getAuthData()
            authLocalDataSource.clearAuthData()
        }
    }
}
