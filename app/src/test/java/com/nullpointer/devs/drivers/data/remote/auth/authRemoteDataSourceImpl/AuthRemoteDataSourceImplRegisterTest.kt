package com.nullpointer.devs.drivers.data.remote.auth.authRemoteDataSourceImpl

import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO
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

class AuthRemoteDataSourceImplRegisterTest {

    private lateinit var authRemoteDataSource: AuthRemoteDataSourceImpl
    private val authApiServices: AuthApiServices = mockk()
    private val registerDTO = RegisterDTO(
        name = "Test",
        email = "example@correo.com",
        password = "password",
        lastname = "Test",
        birthdate = "2021-01-01",
    )


    @Before
    fun setUp() {
        authRemoteDataSource = AuthRemoteDataSourceImpl(authApiServices)
    }

    @Test
    fun `register should return expected RegisterResponseDTO`() = runBlocking {
        // Arrange
        val expectedResponse = mockk<RegisterResponseDTO>(relaxed = true)

        coEvery { authApiServices.register(registerDTO) } returns expectedResponse

        // Act
        val result = authRemoteDataSource.register(registerDTO)

        // Assert
        assertEquals(expectedResponse, result)

        coVerify { authApiServices.register(registerDTO) }
    }

    @Test
    fun `register should throw UserNotFoundException when 409`() = runBlocking {
        val response = mockk<Response<RegisterResponseDTO>>(relaxed = true)
        val message = "User already exists"

        every { response.message() } returns message
        every { response.code() } returns 409

        val expectedException = HttpException(response)

        coEvery { authApiServices.register(registerDTO) } throws expectedException

        try {
            authRemoteDataSource.register(registerDTO)
        } catch (e: Exception) {
            assertTrue(e is AuthException.RegisterException.UserAlreadyExistsException)
        }

        coVerify { authApiServices.register(registerDTO) }
    }

    @Test
    fun `register should throw UserNotFoundException when 429`() = runBlocking {
        val response = mockk<Response<RegisterResponseDTO>>(relaxed = true)
        val message = "Too many request"

        every { response.message() } returns message
        every { response.code() } returns 429

        val expectedException = HttpException(response)

        coEvery { authApiServices.register(registerDTO) } throws expectedException

        try {
            authRemoteDataSource.register(registerDTO)
        } catch (e: Exception) {
            assertTrue(e is AuthException.RegisterException.TooManyRequestsException)
        }

        coVerify { authApiServices.register(registerDTO) }
    }

    @Test
    fun `register should throw UserNotFoundException when 500`() = runBlocking {
        val response = mockk<Response<RegisterResponseDTO>>(relaxed = true)
        val message = "Internal server error"

        every { response.message() } returns message
        every { response.code() } returns 500

        val expectedException = HttpException(response)

        coEvery { authApiServices.register(registerDTO) } throws expectedException

        try {
            authRemoteDataSource.register(registerDTO)
        } catch (e: Exception) {
            assertTrue(e is AuthException.RegisterException.ServerException)
        }

        coVerify { authApiServices.register(registerDTO) }
    }


    @Test
    fun `register should throw UnknownException with message`() = runBlocking {

        val message = "Unknown error"

        coEvery { authApiServices.register(registerDTO) } throws Exception(message)

        try {
            authRemoteDataSource.register(registerDTO)
        } catch (e: Exception) {
            assertTrue(e is AuthException.UnknownException)
            assertEquals(message, e.message)
        }

        coVerify { authApiServices.register(registerDTO) }
    }


    @Test
    fun `register should throw UnknownException without error`() = runBlocking {

        coEvery { authApiServices.register(registerDTO) } throws Exception()

        try {
            authRemoteDataSource.register(registerDTO)
        } catch (e: Exception) {
            assertTrue(e is AuthException.UnknownException)
        }

        coVerify { authApiServices.register(registerDTO) }
    }

}