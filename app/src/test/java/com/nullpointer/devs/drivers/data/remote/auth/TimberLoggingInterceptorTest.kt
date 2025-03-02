package com.nullpointer.devs.drivers.data.remote.auth

import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test

class TimberLoggingInterceptorTest {
    @Test
    fun `intercept should log request and response`() {
        val interceptor = TimberLoggingInterceptor()
        val chain = mockk<Interceptor.Chain>(relaxed = true)


        val mediaType = MediaType.get("application/json")
        val requestBody = RequestBody.create(mediaType, "{\"key\":\"value\"}")
        val request = Request.Builder()
            .url("https://example.com")
            .post(requestBody)
            .build()

        val responseBody = ResponseBody.create(mediaType, "{\"responseKey\":\"responseValue\"}")
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(responseBody)
            .build()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val interceptedResponse = interceptor.intercept(chain)

        assertEquals(200, interceptedResponse.code())
        assertEquals("OK", interceptedResponse.message())
    }

    @Test
    fun `intercept should log request and response with out body`() {
        val interceptor = TimberLoggingInterceptor()
        val chain = mockk<Interceptor.Chain>(relaxed = true)


        val request = Request.Builder()
            .url("https://example.com")

            .build()

        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")

            .build()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val interceptedResponse = interceptor.intercept(chain)

        assertEquals(200, interceptedResponse.code())
        assertEquals("OK", interceptedResponse.message())
    }

    @Test
    fun `intercept should log request and response2`() {
        val interceptor = TimberLoggingInterceptor()
        val chain = mockk<Interceptor.Chain>(relaxed = true)


        val mediaType = MediaType.get("application/json")
        val requestBody = RequestBody.create(mediaType, "[{\"key\":\"value\"}]")
        val request = Request.Builder()
            .url("https://example.com")
            .post(requestBody)
            .build()

        val responseBody = ResponseBody.create(mediaType, "{\"responseKey\":\"responseValue\"}")
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(responseBody)
            .build()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val interceptedResponse = interceptor.intercept(chain)

        assertEquals(200, interceptedResponse.code())
        assertEquals("OK", interceptedResponse.message())
    }


    @Test
    fun `intercept should log request and response3`() {
        val interceptor = TimberLoggingInterceptor()
        val chain = mockk<Interceptor.Chain>(relaxed = true)


        val mediaType = MediaType.get("application/json")
        val requestBody = RequestBody.create(mediaType, "plain text")
        val request = Request.Builder()
            .url("https://example.com")
            .post(requestBody)
            .build()

        val responseBody = ResponseBody.create(mediaType, "{\"responseKey\":\"responseValue\"}")
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(responseBody)
            .build()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val interceptedResponse = interceptor.intercept(chain)

        assertEquals(200, interceptedResponse.code())
        assertEquals("OK", interceptedResponse.message())
    }


    @Test
    fun `intercept should log request and response4`() {
        val interceptor = TimberLoggingInterceptor()
        val chain = mockk<Interceptor.Chain>(relaxed = true)


        val mediaType = MediaType.get("application/json")
        val requestBody = RequestBody.create(mediaType, "{invalid json}")
        val request = Request.Builder()
            .url("https://example.com")
            .post(requestBody)
            .build()

        val responseBody = ResponseBody.create(mediaType, "{\"responseKey\":\"responseValue\"}")
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(responseBody)
            .build()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val interceptedResponse = interceptor.intercept(chain)

        assertEquals(200, interceptedResponse.code())
        assertEquals("OK", interceptedResponse.message())
    }


}
