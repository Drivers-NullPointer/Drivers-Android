package com.nullpointer.devs.drivers.data.remote.auth

import com.nullpointer.devs.drivers.data.local.auth.AuthLocalDataSource
import com.nullpointer.devs.drivers.utils.Constants
import dagger.Lazy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor

/**
 * An implementation of [Interceptor] that adds authentication headers to network requests.
 *
 * This interceptor retrieves authentication data from a local data source and attaches
 * it as a Bearer token in the Authorization header of outgoing requests.
 * It also adds a User-Agent header to specify the type of client making the request.
 *
 * @property authLocalDataSourceLazy A lazy-initialized instance of [AuthLocalDataSource]
 *   used to fetch the authentication data when required.
 */
class SigningInterceptor(
    private val authLocalDataSourceLazy: Lazy<AuthLocalDataSource>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val authLocalDataSource = authLocalDataSourceLazy.get()
        val request = chain.request().newBuilder()
            .addHeader("User-Agent", Constants.HEADER_MOBILE).apply {
                val authData = runBlocking { authLocalDataSource.getAuthData().first() }
                if (authData != null) {
                    addHeader(Constants.HEADER_AUTHORIZATION, "Bearer ${authData.token}")
                }
            }
            .build()
        return chain.proceed(request)
    }
}
