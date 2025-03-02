package com.nullpointer.devs.drivers.data.remote.auth.authRemoteDataSourceImpl

import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.remote.auth.AuthApiServices
import com.nullpointer.devs.drivers.data.remote.auth.AuthRemoteDataSourceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class AuthRemoteDataSourceImplLoginTest {

    private lateinit var authRemoteDataSource: AuthRemoteDataSourceImpl
    private val authApiServices: AuthApiServices = mockk()
    private val loginDTO = LoginDTO(email = "test@example.com", password = "password")

    @Before
    fun setUp() {
        authRemoteDataSource = AuthRemoteDataSourceImpl(authApiServices)
    }

    @Test
    fun `login should return expected LoginResponseDTO`() = runBlocking {
        // Arrange

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
        coVerify { authApiServices.login(loginDTO) }
    }

    @Test
    fun `login should throw UserNotFoundException when 404`() = runBlocking {
        val response = mockk<Response<LoginResponseDTO>>(relaxed = true)
        val message = "User not found"
        every { response.code() } returns 404
        every { response.message() } returns message

        val httpException = HttpException(response)

        coEvery { authApiServices.login(loginDTO) } throws httpException

        // Act
        try {
            authRemoteDataSource.login(loginDTO)
        } catch (e: Exception) {
            // Assert
            assertTrue(e is AuthException.LoginException.UserNotFoundException)
            assertEquals(message, e.message)
        }

        coVerify { authApiServices.login(loginDTO) }
    }


    @Test
    fun `login should throw InvalidCredentialsException when 403`() = runBlocking {
        val response = mockk<Response<LoginResponseDTO>>(relaxed = true)
        val message = "Invalid credentials"
        every { response.code() } returns 403
        every { response.message() } returns message

        val httpException = HttpException(response)

        coEvery { authApiServices.login(loginDTO) } throws httpException

        // Act
        try {
            authRemoteDataSource.login(loginDTO)
        } catch (e: Exception) {
            // Assert
            assertTrue(e is AuthException.LoginException.InvalidCredentialsException)
            assertEquals(message, e.message)
        }

        coVerify { authApiServices.login(loginDTO) }
    }

    @Test
    fun `login should throw TooManyRequestsException when 429`() = runBlocking {
        val response = mockk<Response<LoginResponseDTO>>(relaxed = true)
        val message = "Too many requests"
        every { response.code() } returns 429
        every { response.message() } returns message

        val httpException = HttpException(response)

        coEvery { authApiServices.login(loginDTO) } throws httpException

        // Act
        try {
            authRemoteDataSource.login(loginDTO)
        } catch (e: Exception) {
            // Assert
            assertTrue(e is AuthException.LoginException.TooManyRequestsException)
            assertEquals(message, e.message)
        }

        coVerify { authApiServices.login(loginDTO) }
    }


    @Test
    fun `login should throw ServerException when 500`() = runBlocking {
        val response = mockk<Response<LoginResponseDTO>>(relaxed = true)
        val message = "Server error"
        every { response.code() } returns 500
        every { response.message() } returns message

        val httpException = HttpException(response)

        coEvery { authApiServices.login(loginDTO) } throws httpException

        // Act
        try {
            authRemoteDataSource.login(loginDTO)
        } catch (e: Exception) {
            // Assert
            assertTrue(e is AuthException.LoginException.ServerException)
            assertEquals(message, e.message)
        }

        coVerify { authApiServices.login(loginDTO) }
    }

    @Test
    fun `login should throw UnknownException when unknown error`() = runBlocking {
        val message = "Unknown error"
        val exception = Exception(message)

        coEvery { authApiServices.login(loginDTO) } throws exception

        // Act
        try {
            authRemoteDataSource.login(loginDTO)
        } catch (e: Exception) {
            // Assert
            assertTrue(e is AuthException.UnknownException)
            assertEquals(message, e.message)
        }

        coVerify { authApiServices.login(loginDTO) }
    }


    @Test
    fun `login should throw UnknownException when unknown error withOutMessage`() = runBlocking {
        val exception = Exception()

        coEvery { authApiServices.login(loginDTO) } throws exception

        // Act
        try {
            authRemoteDataSource.login(loginDTO)
        } catch (e: Exception) {
            // Assert
            assertTrue(e is AuthException.UnknownException)
            assertEquals("An unknown error occurred while logging in", e.message)
        }

        coVerify { authApiServices.login(loginDTO) }
    }

}