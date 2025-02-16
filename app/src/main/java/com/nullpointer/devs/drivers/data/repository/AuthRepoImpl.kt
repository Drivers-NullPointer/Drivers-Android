package com.nullpointer.devs.drivers.data.repository

import com.nullpointer.devs.drivers.data.local.auth.AuthLocalDataSource
import com.nullpointer.devs.drivers.data.mappers.auth.toAuthData
import com.nullpointer.devs.drivers.data.mappers.auth.toForgotPasswordDTO
import com.nullpointer.devs.drivers.data.mappers.auth.toLoginDTO
import com.nullpointer.devs.drivers.data.mappers.auth.toRegisterDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.remote.auth.AuthRemoteDataSource
import com.nullpointer.devs.drivers.domain.model.CredentialsData
import com.nullpointer.devs.drivers.domain.model.ForgotPasswordData
import com.nullpointer.devs.drivers.domain.model.RegisterData
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull

/**
 * Implementation of [AuthRepository] that manages authentication data.
 * It acts as a bridge between the data sources (local and remote).
 *
 * @property authLocalDataSource Local data source for storing authentication data.
 * @property authRemoteDataSource Remote data source for handling authentication API requests.
 */
class AuthRepoImpl(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {

    /**
     * A Flow that emits authentication data updates in real-time.
     */
    override val authData get() = authLocalDataSource.getAuthData()

    /**
     * Logs in a user using the provided credentials.
     * Converts the domain-level [CredentialsData] to a DTO, sends the request to the remote data source,
     * and saves the received authentication data in the local data source.
     *
     * @param credentialsData The user's login credentials (e.g., email and password).
     */
    override suspend fun loginCredentials(credentialsData: CredentialsData) {
        val loginDTO = credentialsData.toLoginDTO()
        val response = authRemoteDataSource.login(loginDTO)
        authLocalDataSource.saveAuthData(response.toAuthData())
    }

    /**
     * Registers a new user with the provided registration data.
     * Converts the domain-level [RegisterData] to a DTO, sends the request to the remote data source,
     * and stores the received authentication data locally.
     *
     * @param registerData The user's registration details.
     */
    override suspend fun registerCredentials(registerData: RegisterData) {
        val registerDTO = registerData.toRegisterDTO()
        val response = authRemoteDataSource.register(registerDTO)
        authLocalDataSource.saveAuthData(response.toAuthData())
    }

    /**
     * Initiates a password recovery process for the user.
     * Converts the domain-level [ForgotPasswordData] to a DTO and sends it to the remote data source.
     *
     * @param forgotPasswordData Data required to reset the user's password.
     */
    override suspend fun forgotPassword(forgotPasswordData: ForgotPasswordData) {
        val forgotPasswordDTO = forgotPasswordData.toForgotPasswordDTO()
        authRemoteDataSource.forgotPassword(forgotPasswordDTO)
    }

    /**
     * Refreshes the authentication token if it has expired.
     * Retrieves the stored authentication data, requests a new token, and updates the local storage.
     * If no authentication data is found, it clears the stored credentials.
     */
    override suspend fun refreshToken() {
        val authData = authData.firstOrNull()
        if (authData != null) {
            val refreshTokenDTO = RefreshDTO(authData.refreshToken)
            val response = authRemoteDataSource.refresh(refreshTokenDTO)
            val newAuthData = authData.copy(
                token = response.token,
                refreshToken = response.refreshToken
            )
            authLocalDataSource.saveAuthData(newAuthData)
        } else {
            authLocalDataSource.clearAuthData()
        }
    }
}
