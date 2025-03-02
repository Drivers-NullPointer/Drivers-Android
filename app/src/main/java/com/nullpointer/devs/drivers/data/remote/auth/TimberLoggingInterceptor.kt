package com.nullpointer.devs.drivers.data.remote.auth

import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.nio.charset.Charset

/**
 * An OkHttp interceptor that logs HTTP requests and responses using Timber.
 */
class TimberLoggingInterceptor : Interceptor {
    /**
     * Intercepts the request and response, logging their details including headers and formatted JSON body.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val requestBody = request.body()
        val requestBodyString = requestBody?.let { bodyToString(it) } ?: "No Body"
        val formattedRequestBody = formatJson(requestBodyString)

        Timber.d("\u2b06\ufe0f REQUEST: ${request.method()} ${request.url()}\nHeaders: ${request.headers()}\nBody:$formattedRequestBody")

        val response = chain.proceed(request)

        val responseBody = response.body()
        val responseBodyString = responseBody?.let { bodyToString(it) } ?: "No Body"
        val formattedResponseBody = formatJson(responseBodyString)

        Timber.d("\u2b07\ufe0f RESPONSE: ${response.code()} ${response.message()}\nHeaders: ${response.headers()}\nBody:$formattedResponseBody")

        return response
    }

    /**
     * Converts the request body into a UTF-8 string.
     *
     * @param requestBody The request body.
     * @return The string representation of the request body.
     */
    private fun bodyToString(requestBody: RequestBody): String {
        return try {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            "Error reading body"
        }
    }

    /**
     * Converts the response body into a UTF-8 string.
     *
     * @param responseBody The response body.
     * @return The string representation of the response body.
     */
    private fun bodyToString(responseBody: ResponseBody): String {
        return try {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer
            val charset: Charset = responseBody.contentType()?.charset(Charset.forName("UTF-8")) ?: Charset.forName("UTF-8")
            buffer.clone().readString(charset)
        } catch (e: Exception) {
            "Error reading body"
        }
    }

    /**
     * Formats a JSON string to improve readability.
     *
     * @param json The raw JSON string.
     * @return A formatted JSON string with proper indentation.
     */
    private fun formatJson(json: String): String {
        return try {
            when {
                json.trim().startsWith("{") -> JSONObject(json).toString(4)
                json.trim().startsWith("[") -> JSONArray(json).toString(4)
                else -> json
            }
        } catch (e: Exception) {
            json
        }
    }
}
