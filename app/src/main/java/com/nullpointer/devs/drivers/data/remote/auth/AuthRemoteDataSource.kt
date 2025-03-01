package com.nullpointer.devs.drivers.data.remote.auth

import com.nullpointer.devs.drivers.data.model.auth.dto.CheckVerifyEmailDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.CheckVerifyEmailResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshTokenResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO

/**
 * Interface that defines the contract for authentication-related operations in the remote data source.
 * These methods interact with the backend API to handle login, registration, token refresh, and password recovery.
 */
interface AuthRemoteDataSource {

    /**
     * Logs in the user by sending their credentials to the backend API.
     *
     * @param loginDTO The data transfer object containing the user's credentials (email and password).
     * @return A [LoginResponseDTO] containing the authentication response, including the token and user data.
     */
    suspend fun login(loginDTO: LoginDTO): LoginResponseDTO

    /**
     * Registers a new user by sending their details to the backend API.
     *
     * @param registerDTO The data transfer object containing the user's information for registration.
     * @return A [RegisterResponseDTO] containing the response after the registration process, including the token and user data.
     */
    suspend fun register(registerDTO: RegisterDTO): RegisterResponseDTO

    /**
     * Refreshes the authentication token by sending the refresh token to the backend API.
     *
     * @param refreshDTO The data transfer object containing the refresh token.
     * @return A [RefreshTokenResponseDTO] containing the new authentication and refresh tokens.
     */
    suspend fun refresh(refreshDTO: RefreshDTO): RefreshTokenResponseDTO

    /**
     * Initiates the password recovery process by sending the user's email to the backend API.
     *
     * @param forgotPasswordDTO The data transfer object containing the user's email address for password recovery.
     * @return A [ForgotPasswordResponseDTO] containing a message indicating the result of the recovery request.
     */
    suspend fun forgotPassword(forgotPasswordDTO: ForgotPasswordDTO): ForgotPasswordResponseDTO

    /**
     * Checks if the email is verified by sending the user's email to the backend API.
     *
     * @param checkVerifyEmailDTO The data transfer object containing the user's email address for verification.
     * @return A [CheckVerifyEmailResponseDTO] containing a message indicating the result of the verification request.
     */
    suspend fun checkVerifyEmail(checkVerifyEmailDTO: CheckVerifyEmailDTO): CheckVerifyEmailResponseDTO
}
