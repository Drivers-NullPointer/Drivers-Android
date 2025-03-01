package com.nullpointer.devs.drivers.data.local.auth

import com.nullpointer.devs.drivers.data.local.datastore.auth.AuthDataStore
import com.nullpointer.devs.drivers.data.model.auth.AuthData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthLocalDataSourceImplTest {

    private lateinit var authLocalDataSource: AuthLocalDataSourceImpl
    private val authDataStore: AuthDataStore = mockk()


    @Before
    fun setUp() {
        authLocalDataSource = AuthLocalDataSourceImpl(authDataStore)
    }

    @Test
    fun getAuthData() = runBlocking{

        val authData =
            AuthData(
                token = "token",
                refreshToken = "refreshToken",
                id = 1,
                isEmailVerified = true,
                email = "email"
            )

        coEvery { authDataStore.getAuthData() } returns flowOf(authData)

        val result = authLocalDataSource.getAuthData().first()


        assertEquals(authData, result)

        coVerify { authDataStore.getAuthData() }
    }

    @Test
    fun saveAuthData() = runBlocking{

        val authData =
            AuthData(
                token = "token",
                refreshToken = "refreshToken",
                id = 1,
                isEmailVerified = true,
                email = "email"
            )

        coEvery { authDataStore.saveAuthData(authData) } returns Unit

        authLocalDataSource.saveAuthData(authData)
    }

    @Test
    fun clearAuthData() = runBlocking{

        coEvery { authDataStore.clearAuthData() } returns Unit

        authLocalDataSource.clearAuthData()

        coVerify { authDataStore.clearAuthData() }
    }
}