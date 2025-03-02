package com.nullpointer.devs.drivers.data.remote.auth.authRemoteDataSourceImpl

import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshTokenResponseDTO
import com.nullpointer.devs.drivers.data.remote.auth.AuthApiServices
import com.nullpointer.devs.drivers.data.remote.auth.AuthRemoteDataSourceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthRemoteDataSourceImplRefreshTest {

    private lateinit var authRemoteDataSource: AuthRemoteDataSourceImpl
    private val authApiServices: AuthApiServices = mockk()
    private val refreshDTO = RefreshDTO(
        refreshToken = "refreshToken"
    )

    @Before
    fun setUp() {
        authRemoteDataSource = AuthRemoteDataSourceImpl(authApiServices)
    }


    @Test
    fun `refresh should return expected RegisterResponseDTO`() = runBlocking {
        // Arrange
        val expectedResponse = mockk<RefreshTokenResponseDTO>(relaxed = true)

        coEvery { authApiServices.refresh(refreshDTO) } returns expectedResponse

        // Act
        val result = authRemoteDataSource.refresh(refreshDTO)

        // Assert
        assertEquals(expectedResponse, result)

        coVerify { authApiServices.refresh(refreshDTO) }
    }
}