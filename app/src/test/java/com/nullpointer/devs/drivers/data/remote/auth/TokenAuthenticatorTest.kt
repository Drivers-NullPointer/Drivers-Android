package com.nullpointer.devs.drivers.data.remote.auth

import com.nullpointer.devs.drivers.data.local.auth.AuthLocalDataSource
import com.nullpointer.devs.drivers.data.model.auth.AuthData
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshTokenResponseDTO
import com.nullpointer.devs.drivers.utils.Constants
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException


class TokenAuthenticatorTest {


    private val authLocalDataSourceLazy: dagger.Lazy<AuthLocalDataSource> = mockk()
    private val authLocalDataSource: AuthLocalDataSource = mockk()
    private val authRemoteDataSourceLazy: dagger.Lazy<AuthRemoteDataSource> = mockk()
    private val authRemoteDataSource: AuthRemoteDataSource = mockk()
    private lateinit var tokenAuthenticator: TokenAuthenticator

    @Before
    fun setUp() {
        every { authLocalDataSourceLazy.get() } returns authLocalDataSource
        every { authRemoteDataSourceLazy.get() } returns authRemoteDataSource
        tokenAuthenticator = TokenAuthenticator(authLocalDataSourceLazy, authRemoteDataSourceLazy)
    }

    @Test
    fun `authenticate should return null if response code is 403`() {
        val response = mockk<Response>(relaxed = true)
        every { response.code() } returns 403

        val result = tokenAuthenticator.authenticate(null, response)

        assertNull(result)  // Should return null if response code is 403
    }

    @Test
    fun `authenticate should clear auth data after MAX_ATTEMPTS are reached`() = runBlocking {
        val response = mockk<Response>(relaxed = true)
        every { response.code() } returns 401

        val authData = mockk<AuthData>()
        every { authLocalDataSource.getAuthData() } returns flowOf(authData)
        coEvery { authLocalDataSource.clearAuthData() } just Runs

        // Simulate 3 failed attempts using reflection
        val retryCountField = TokenAuthenticator::class.java.getDeclaredField("retryCount")
        retryCountField.isAccessible = true  // Make it accessible
        val retryCount = retryCountField.get(tokenAuthenticator) as MutableStateFlow<Int>
        retryCount.value = 3

        val result = tokenAuthenticator.authenticate(null, response)

        assertNull(result)
        coVerify { authLocalDataSource.clearAuthData() }
    }

    @Test
    fun `authenticate should refresh token and return new request`() = runBlocking {
        val response = mockk<Response>(relaxed = true)
        every { response.code() } returns 401

        // Datos de prueba
        val authData = AuthData(
            refreshToken = "refreshToken",
            id = 1,
            token = "token",
            email = "email",
            isEmailVerified = true
        )
        val newToken = RefreshTokenResponseDTO("newToken", "newRefreshToken")
        val updatedAuthData = authData.copy(token = newToken.token)

        // Simulación de las dependencias
        every { authLocalDataSource.getAuthData() } returns flowOf(authData)
        coEvery { authRemoteDataSource.refresh(any()) } returns newToken
        coEvery { authLocalDataSource.saveAuthData(updatedAuthData) } just Runs
        every { response.request() } returns Request.Builder().url("https://example.com").build()

        // Llamada al método
        val result = tokenAuthenticator.authenticate(null, response)

        // Verificación del resultado
        assertNotNull(result)  // Verifica que el resultado no sea nulo
        assert(result?.header(Constants.HEADER_AUTHORIZATION) == "Bearer ${newToken.token}")

        // Verificación de que el método saveAuthData fue llamado
        coVerify { authLocalDataSource.saveAuthData(updatedAuthData) }
    }

    @Test
    fun `authenticate should return null if HttpException occurs 401`() = runBlocking {

        // Crear el mock de la respuesta
        val response = mockk<Response>()
        val retrofitResponse = mockk<retrofit2.Response<RefreshTokenResponseDTO>>()

        // Configurar que el código de la respuesta será 401
        every { response.code() } returns 401
        every { response.body() } returns null


        every { retrofitResponse.code() } returns 401
        every { retrofitResponse.body() } returns null
        every { retrofitResponse.message() } returns "Unauthorized"

        // Crear los datos de autenticación simulados
        val authData = AuthData(
            refreshToken = "refreshToken",
            id = 1,
            token = "token",
            email = "email",
            isEmailVerified = true
        )

        // Configurar los mocks para obtener los datos y simular un error al refrescar el token
        every { authLocalDataSource.getAuthData() } returns flowOf(authData)
        coEvery { authRemoteDataSource.refresh(any()) } throws HttpException(retrofitResponse)
        coEvery { authLocalDataSource.clearAuthData() } just Runs

        // Llamar al método
        val result = tokenAuthenticator.authenticate(null, response)

        // Verificación del resultado
        assertNull(result)  // Debería retornar null si ocurre una HttpException

        // Verificación de que los datos de autenticación fueron limpiados
        coVerify { authLocalDataSource.clearAuthData() }
    }

    @Test
    fun `authenticate should return null if HttpException occurs 400`() = runBlocking {

        // Crear el mock de la respuesta
        val response = mockk<Response>()
        val retrofitResponse = mockk<retrofit2.Response<RefreshTokenResponseDTO>>()

        // Configurar que el código de la respuesta será 401
        every { response.code() } returns 401
        every { response.body() } returns null


        every { retrofitResponse.code() } returns 400
        every { retrofitResponse.body() } returns null
        every { retrofitResponse.message() } returns "Unauthorized"

        // Crear los datos de autenticación simulados
        val authData = AuthData(
            refreshToken = "refreshToken",
            id = 1,
            token = "token",
            email = "email",
            isEmailVerified = true
        )

        // Configurar los mocks para obtener los datos y simular un error al refrescar el token
        every { authLocalDataSource.getAuthData() } returns flowOf(authData)
        coEvery { authRemoteDataSource.refresh(any()) } throws HttpException(retrofitResponse)
        coEvery { authLocalDataSource.clearAuthData() } just Runs

        // Llamar al método
        val result = tokenAuthenticator.authenticate(null, response)

        // Verificación del resultado
        assertNull(result)  // Debería retornar null si ocurre una HttpException


    }


    @Test
    fun `authenticate should return null if IOException occurs`() = runBlocking {
        // Crear el mock de la respuesta
        val response = mockk<Response>(relaxed = true)
        // Configurar que el código de la respuesta será 401
        every { response.code() } returns 401
        every { response.body() } returns null

        // Crear los datos de autenticación simulados
        val authData = AuthData(
            refreshToken = "refreshToken",
            id = 1,
            token = "token",
            email = "email",
            isEmailVerified = true
        )

        // Configurar los mocks para obtener los datos y simular un error de IOException al refrescar el token
        every { authLocalDataSource.getAuthData() } returns flowOf(authData)
        coEvery { authRemoteDataSource.refresh(any()) } throws IOException()  // Lanzamos IOException

        // Llamar al método
        val result = tokenAuthenticator.authenticate(null, response)

        // Verificación del resultado
        assertNull(result)  // Debería retornar null si ocurre una IOException
    }

    @Test
    fun `authenticate should return null for any other unexpected exception`() = runBlocking {
        // Crear el mock de la respuesta
        val response = mockk<Response>(relaxed = true)
        // Configurar que el código de la respuesta será 401
        every { response.code() } returns 401
        every { response.body() } returns null

        // Crear los datos de autenticación simulados
        val authData = AuthData(
            refreshToken = "refreshToken",
            id = 1,
            token = "token",
            email = "email",
            isEmailVerified = true
        )

        // Configurar los mocks para obtener los datos y simular una excepción inesperada al refrescar el token
        every { authLocalDataSource.getAuthData() } returns flowOf(authData)
        coEvery { authRemoteDataSource.refresh(any()) } throws Exception()  // Lanzamos una excepción inesperada

        // Llamar al método
        val result = tokenAuthenticator.authenticate(null, response)

        // Verificación del resultado
        assertNull(result)  // Debería retornar null si ocurre una excepción inesperada
    }

}
