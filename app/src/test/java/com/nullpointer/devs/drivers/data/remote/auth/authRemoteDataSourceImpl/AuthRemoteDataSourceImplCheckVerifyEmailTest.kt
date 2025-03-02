package com.nullpointer.devs.drivers.data.remote.auth.authRemoteDataSourceImpl

import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.data.model.auth.dto.CheckVerifyEmailDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.CheckVerifyEmailResponseDTO
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

class AuthRemoteDataSourceImplCheckVerifyEmailTest {

    private lateinit var authRemoteDataSource: AuthRemoteDataSourceImpl
    private val authApiServices: AuthApiServices = mockk()
    private val checkVerifyEmailDTO = CheckVerifyEmailDTO(
        email = "correo.com ",
    )


    @Before
    fun setUp() {
        authRemoteDataSource = AuthRemoteDataSourceImpl(authApiServices)
    }

    @Test
    fun `checkVerifyEmail should return expected RegisterResponseDTO`() = runBlocking {
        // Arrange
        val expectedResponse = mockk<CheckVerifyEmailResponseDTO>(relaxed = true)

        coEvery { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) } returns expectedResponse

        // Act
        val result = authRemoteDataSource.checkVerifyEmail(checkVerifyEmailDTO)

        // Assert
        assertEquals(expectedResponse, result)

        coVerify { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) }
    }

    @Test
    fun `checkVerifyEmail should throw UnauthorizedException when 401`() = runBlocking {
        val response = mockk<Response<CheckVerifyEmailResponseDTO>>(relaxed = true)
        val message = "Server error"
        every { response.code() } returns 401
        every { response.message() } returns message

        coEvery { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) } throws HttpException(
            response
        )

        try {
            authRemoteDataSource.checkVerifyEmail(checkVerifyEmailDTO)
        } catch (e: AuthException) {
            assertTrue(e is AuthException.CheckVerifyEmailException.UnauthorizedException)
        }

        coVerify { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) }
    }

    @Test
    fun `checkVerifyEmail should throw ServerErrorException when 500`() = runBlocking {
        val response = mockk<Response<CheckVerifyEmailResponseDTO>>(relaxed = true)
        val message = "Server error"
        every { response.code() } returns 500
        every { response.message() } returns message

        coEvery { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) } throws HttpException(
            response
        )

        try {
            authRemoteDataSource.checkVerifyEmail(checkVerifyEmailDTO)
        } catch (e: AuthException) {
            assertTrue(e is AuthException.CheckVerifyEmailException.ServerException)
        }

        coVerify { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) }
    }

    @Test
    fun `checkVerifyEmail should throw UnknownException when unknown error with message`() =
        runBlocking {
            val message = "Unknown error"
            val exception = Exception(message)

            coEvery { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) } throws exception

            try {
                authRemoteDataSource.checkVerifyEmail(checkVerifyEmailDTO)
            } catch (e: AuthException) {
                assertTrue(e is AuthException.UnknownException)
            }

            coVerify { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) }
        }


    @Test
    fun `checkVerifyEmail should throw UnknownException when unknown error without message`() =
        runBlocking {
            coEvery { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) } throws Exception()

            try {
                authRemoteDataSource.checkVerifyEmail(checkVerifyEmailDTO)
            } catch (e: AuthException) {
                assertTrue(e is AuthException.UnknownException)
            }

            coVerify { authApiServices.checkVerifyEmail(checkVerifyEmailDTO) }
        }


}