package com.nullpointer.devs.drivers.data.remote.auth

import com.nullpointer.devs.drivers.data.model.auth.dto.CheckVerifyEmailDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.CheckVerifyEmailResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshTokenResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthRemoteDataSourceImplTest {

    private lateinit var authRemoteDataSource: AuthRemoteDataSourceImpl
    private val authApiServices: AuthApiServices = mockk()

    @Before
    fun setUp() {
        authRemoteDataSource = AuthRemoteDataSourceImpl(authApiServices)
    }

    @Test
    fun `login should return expected LoginResponseDTO`()=runBlocking {
        // Arrange
        val loginDTO = LoginDTO(email = "test@example.com", password = "password")
        val expectedResponse = LoginResponseDTO(
            token = "token",
            refreshToken = "refresh_token",
            user = LoginResponseDTO.User(
                name = "Test",
                id = 1,
                email = "example",
                roleID = 1,
                isEmailVerified = true
            )
        )

        coEvery { authApiServices.login(loginDTO) } returns expectedResponse

        // Act
        val result = authRemoteDataSource.login(loginDTO)

        // Assert
        assertEquals(expectedResponse, result)
        coVerify { authApiServices.login(loginDTO) } // Verifica que se llamó al método
    }

    @Test
    fun `register should return expected RegisterResponseDTO`() = runBlocking {
        val registerDTO = RegisterDTO(
            name = "Test",
            email = "example",
            password = "password",
            lastname = "Test",
            birthdate = "2021-01-01",
        )

        val expectedResponse = RegisterResponseDTO(
            token = "token",
            refreshToken = "refresh_token",
            user = RegisterResponseDTO.User(
                id = 1,
                name = "Test",
                email = "example",
                roleID = 1,
                isEmailVerified = true
            )
        )

        coEvery { authApiServices.register(registerDTO) } returns expectedResponse

        val result = authRemoteDataSource.register(registerDTO)

        assertEquals(expectedResponse, result)
        coVerify { authApiServices.register(registerDTO) }
    }

    @Test
    fun `refresh should return expected RefreshTokenResponseDTO`() = runBlocking {
        val refreshDTO = RefreshDTO(refreshToken = "old_token")
        val expectedResponse = RefreshTokenResponseDTO(
            token = "new_token",
            refreshToken = "new_refresh_token",
        )

        coEvery { authApiServices.refresh(refreshDTO) } returns expectedResponse

        val result = authRemoteDataSource.refresh(refreshDTO)

        assertEquals(expectedResponse, result)
        coVerify { authApiServices.refresh(refreshDTO) }
    }

    @Test
    fun `forgotPassword should return expected ForgotPasswordResponseDTO`() = runBlocking {
        val forgotPasswordDTO = ForgotPasswordDTO(email = "test@example.com")
        val expectedResponse = ForgotPasswordResponseDTO(
            message = "Email sent"
        )

        coEvery { authApiServices.forgotPassword(forgotPasswordDTO) } returns expectedResponse

        val result = authRemoteDataSource.forgotPassword(forgotPasswordDTO)

        assertEquals(expectedResponse, result)
        coVerify { authApiServices.forgotPassword(forgotPasswordDTO) }
    }

    @Test
    fun `checkVerifyEmail should return expected CheckVerifyEmailResponseDTO`() = runBlocking {
        val checkVerifyEmailDTO = CheckVerifyEmailDTO(
            email = "email@correo.com"
        )

        val expectedResponse = CheckVerifyEmailResponseDTO(
            message = "Email verified",
            isVerified = true
        )

        coEvery { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) } returns expectedResponse

        val result = authRemoteDataSource.checkVerifyEmail(checkVerifyEmailDTO)

        assertEquals(expectedResponse, result)

        coVerify { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) }

    }
}