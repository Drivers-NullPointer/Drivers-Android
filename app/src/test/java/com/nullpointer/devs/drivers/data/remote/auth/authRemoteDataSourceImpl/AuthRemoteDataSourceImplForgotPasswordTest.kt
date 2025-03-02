package com.nullpointer.devs.drivers.data.remote.auth.authRemoteDataSourceImpl

import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordResponseDTO
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

class AuthRemoteDataSourceImplForgotPasswordTest {

    private lateinit var authRemoteDataSource: AuthRemoteDataSourceImpl
    private val authApiServices: AuthApiServices = mockk()
    private val forgotPasswordDTO = ForgotPasswordDTO(
        email = "example@correo.com",
    )


    @Before
    fun setUp() {
        authRemoteDataSource = AuthRemoteDataSourceImpl(authApiServices)
    }

    @Test
    fun `forgotPassword should return expected RegisterResponseDTO`() = runBlocking {
        // Arrange
        val expectedResponse = mockk<ForgotPasswordResponseDTO>(relaxed = true)

        coEvery { authApiServices.forgotPassword(forgotPasswordDTO) } returns expectedResponse

        // Act
        val result = authRemoteDataSource.forgotPassword(forgotPasswordDTO)

        // Assert
        assertEquals(expectedResponse, result)

        coVerify { authApiServices.forgotPassword(forgotPasswordDTO) }
    }

    @Test
    fun `forgotPassword should throw EmailUserNotVerifiedException when 403`() = runBlocking {
        val response = mockk<Response<ForgotPasswordResponseDTO>>(relaxed = true)
        val message = "User not verified"
        every { response.code() } returns 403
        every { response.message() } returns message

        coEvery { authApiServices.forgotPassword(forgotPasswordDTO) } throws HttpException(response)

        try {
            authRemoteDataSource.forgotPassword(forgotPasswordDTO)
        } catch (e: Exception) {
            assertEquals(message, e.message)
            assertTrue(e is AuthException.ForgotException.EmailUserNotVerifiedException)
        }

        coVerify { authApiServices.forgotPassword(forgotPasswordDTO) }
    }

    @Test
    fun `forgotPassword should throw UserNotFoundException when 404`() = runBlocking {
        val response = mockk<Response<ForgotPasswordResponseDTO>>(relaxed = true)
        val message = "User not found"
        every { response.code() } returns 404
        every { response.message() } returns message

        coEvery { authApiServices.forgotPassword(forgotPasswordDTO) } throws HttpException(response)

        try {
            authRemoteDataSource.forgotPassword(forgotPasswordDTO)
        } catch (e: Exception) {
            assertEquals(message, e.message)
            assertTrue(e is AuthException.ForgotException.UserNotFoundException)
        }

        coVerify { authApiServices.forgotPassword(forgotPasswordDTO) }
    }

    @Test
    fun `forgotPassword should throw UserNotFoundException when 429`() = runBlocking {
        val response = mockk<Response<ForgotPasswordResponseDTO>>(relaxed = true)
        val message = "Too many request"
        every { response.code() } returns 429
        every { response.message() } returns message

        coEvery { authApiServices.forgotPassword(forgotPasswordDTO) } throws HttpException(response)

        try {
            authRemoteDataSource.forgotPassword(forgotPasswordDTO)
        } catch (e: Exception) {
            assertEquals(message, e.message)
            assertTrue(e is AuthException.ForgotException.TooManyRequestsException)
        }

        coVerify { authApiServices.forgotPassword(forgotPasswordDTO) }
    }


    @Test
    fun `forgotPassword should throw ServerException when 500`() = runBlocking {
        val response = mockk<Response<ForgotPasswordResponseDTO>>(relaxed = true)
        val message = "Internal server error"
        every { response.code() } returns 500
        every { response.message() } returns message

        coEvery { authApiServices.forgotPassword(forgotPasswordDTO) } throws HttpException(response)

        try {
            authRemoteDataSource.forgotPassword(forgotPasswordDTO)
        } catch (e: Exception) {
            assertEquals(message, e.message)
            assertTrue(e is AuthException.ForgotException.ServerException)
        }

        coVerify { authApiServices.forgotPassword(forgotPasswordDTO) }
    }

    @Test
    fun `forgotPassword should throw UnknownException with message`() = runBlocking {
        val message = "Unknown error"

        coEvery { authApiServices.forgotPassword(forgotPasswordDTO) } throws Exception(message)

        try {
            authRemoteDataSource.forgotPassword(forgotPasswordDTO)
        } catch (e: Exception) {
            assertEquals(message, e.message)
            assertTrue(e is AuthException.UnknownException)
        }

        coVerify { authApiServices.forgotPassword(forgotPasswordDTO) }
    }

    @Test
    fun `forgotPassword should throw UnknownException without error`() = runBlocking {
        coEvery { authApiServices.forgotPassword(forgotPasswordDTO) } throws Exception()

        try {
            authRemoteDataSource.forgotPassword(forgotPasswordDTO)
        } catch (e: Exception) {
            assertTrue(e is AuthException.UnknownException)
        }

        coVerify { authApiServices.forgotPassword(forgotPasswordDTO) }
    }

}