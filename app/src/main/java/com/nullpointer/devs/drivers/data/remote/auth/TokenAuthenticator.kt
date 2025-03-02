package com.nullpointer.devs.drivers.data.remote.auth


import com.nullpointer.devs.drivers.data.local.auth.AuthLocalDataSource
import com.nullpointer.devs.drivers.data.mappers.auth.toRefreshTokenDTO
import com.nullpointer.devs.drivers.utils.Constants
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import java.io.IOException

/**
 * `TokenAuthenticator` is an OkHttp `Authenticator` that handles token-based authentication.
 * It refreshes the access token when a 401 (Unauthorized) response is received.
 *
 * @property authLocalDataSourceLazy Lazy reference to the local data source that stores authentication information.
 * @property authRemoteDataSourceLazy Lazy reference to the remote data source that allows token refreshing.
 */
class TokenAuthenticator(
    private val authLocalDataSourceLazy: Lazy<AuthLocalDataSource>,
    private val authRemoteDataSourceLazy: Lazy<AuthRemoteDataSource>
) : Authenticator {

    companion object {
        /** Maximum number of retry attempts before forcing authentication data clearance. */
        const val MAX_ATTEMPTS = 3
    }

    /** Retry attempt counter to prevent infinite loops. */
    private val retryCount = MutableStateFlow(0)

    /**
     * This method is triggered when a request receives a 401 (Unauthorized) response.
     * It attempts to refresh the access token and retry the request with the updated token.
     *
     * @param route The route of the original request.
     * @param response The HTTP response that triggered authentication failure.
     * @return A new request with the updated token, or `null` if the token could not be refreshed.
     */
    override fun authenticate(route: Route?, response: Response): Request? {
        val authLocalDataSource = authLocalDataSourceLazy.get()
        val authRemoteDataSource = authRemoteDataSourceLazy.get()

        // If the response code is 403, authentication is forbidden, and token renewal should not be attempted.
        if (response.code() == 403) return null

        return runBlocking {
            try {
                // If the maximum number of attempts is reached, clear authentication data and cancel authentication.
                if (retryCount.value >= MAX_ATTEMPTS) {
                    authLocalDataSource.clearAuthData()
                    return@runBlocking null
                }

                // Retrieve current authentication data.
                val authData =
                    authLocalDataSource.getAuthData().firstOrNull() ?: return@runBlocking null

                // Generate a DTO for token refresh.
                val refreshDTO = authData.toRefreshTokenDTO()
                val newToken = authRemoteDataSource.refresh(refreshDTO)

                // Save the new token in the local data source.
                val newAuthData = authData.copy(token = newToken.token)
                authLocalDataSource.saveAuthData(newAuthData)

                // Reset the retry counter after a successful authentication.
                retryCount.value = 0

                // Create a new request with the updated token.
                response.request().newBuilder()
                    .header(Constants.HEADER_AUTHORIZATION, "Bearer ${newToken.token}")
                    .build()

            } catch (e: HttpException) {
                // If the error is 401, clear authentication data to force a new login.
                if (e.code() == 401) {
                    authLocalDataSource.clearAuthData()
                }
                retryCount.value++
                null
            } catch (e: IOException) {
                // In case of a network error, increment the retry counter.
                retryCount.value++
                null
            } catch (e: Exception) {
                // Catch any other unexpected exceptions.
                retryCount.value++
                null
            }
        }
    }
}

