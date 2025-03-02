package com.nullpointer.devs.drivers.data.remote.auth

import com.nullpointer.devs.drivers.data.local.auth.AuthLocalDataSource
import com.nullpointer.devs.drivers.data.model.auth.AuthData
import com.nullpointer.devs.drivers.utils.Constants
import dagger.Lazy
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SigningInterceptorTest {
    private lateinit var interceptor: SigningInterceptor
    private val authLocalDataSource: AuthLocalDataSource = mockk()
    private val authLocalDataSourceLazy: Lazy<AuthLocalDataSource> = mockk()
    private val chain: Interceptor.Chain = mockk()
    private val request: Request = Request.Builder().url("https://example.com").build()
    private val response: Response = mockk()

    @Before
    fun setUp() {
        every { authLocalDataSourceLazy.get() } returns authLocalDataSource
        interceptor = SigningInterceptor(authLocalDataSourceLazy)
    }

    @Test
    fun `intercept adds authorization header when auth data is available`() {
        val authData = AuthData(
            id = 1,
            email = "example@correo.com",
            refreshToken = "test_refresh_token",
            isEmailVerified = true,
            token = "test_token"
        )
        every { authLocalDataSource.getAuthData() } returns flowOf(authData)
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response

        runBlocking {
            val interceptedResponse = interceptor.intercept(chain)
            assertEquals(response, interceptedResponse)
        }

        verify { chain.request() }
        verify {
            chain.proceed(withArg {
                assert(it.header(Constants.HEADER_AUTHORIZATION) == "Bearer ${authData.token}")
            })
        }
    }

    @Test
    fun `intercept does not add authorization header when auth data is null`() {
        every { authLocalDataSource.getAuthData() } returns flowOf(null)
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response

        runBlocking {
            val interceptedResponse = interceptor.intercept(chain)
            assertEquals(response, interceptedResponse)
        }

        verify { chain.request() }
        verify {
            chain.proceed(withArg {
                assert(it.header(Constants.HEADER_AUTHORIZATION) == null)
            })
        }
    }
}
