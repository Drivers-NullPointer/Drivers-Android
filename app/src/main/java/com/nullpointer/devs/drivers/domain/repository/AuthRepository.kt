package com.nullpointer.devs.drivers.domain.repository

import com.nullpointer.devs.drivers.data.model.auth.AuthData
import com.nullpointer.devs.drivers.domain.model.CredentialsData
import com.nullpointer.devs.drivers.domain.model.ForgotPasswordData
import com.nullpointer.devs.drivers.domain.model.RegisterData
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication-related operations.
 * It defines methods to handle user authentication and session management.
 */
interface AuthRepository {

    /**
     * A Flow that emits authentication data updates.
     * It provides real-time updates about the user's authentication status.
     */
    val authData: Flow<AuthData?>

    /**
     * Attempts to log in the user using the provided credentials.
     *
     * @param credentialsData The user's login credentials (e.g., email and password).
     */
    suspend fun loginCredentials(credentialsData: CredentialsData)

    /**
     * Registers a new user with the provided registration data.
     *
     * @param registerData The user's registration information.
     */
    suspend fun registerCredentials(registerData: RegisterData)

    /**
     * Initiates a password recovery process for the user.
     *
     * @param forgotPasswordData Data required to reset the user's password.
     */
    suspend fun forgotPassword(forgotPasswordData: ForgotPasswordData)

    /**
     * Refreshes the user's authentication token if it has expired.
     * Clears authentication data if the refresh fails.
     */
    suspend fun refreshToken()
}
