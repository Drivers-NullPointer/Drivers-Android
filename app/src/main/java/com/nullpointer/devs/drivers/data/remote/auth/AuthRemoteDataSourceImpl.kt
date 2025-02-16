package com.nullpointer.devs.drivers.data.remote.auth

import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshTokenResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO

/**
 * Implementation of the [AuthRemoteDataSource] interface.
 * This class communicates with the remote backend API through the [AuthApiServices] to perform authentication operations.
 * It acts as a bridge between the repository layer and the actual API services.
 */
class AuthRemoteDataSourceImpl(
    private val authApiServices: AuthApiServices
) : AuthRemoteDataSource {

    /**
     * Logs in the user by calling the backend API's login method.
     *
     * @param loginDTO The data transfer object containing the user's credentials (email and password).
     * @return A [LoginResponseDTO] containing the authentication response, including the token and user data.
     */
    override suspend fun login(loginDTO: LoginDTO): LoginResponseDTO =
        authApiServices.login(loginDTO)

    /**
     * Registers a new user by calling the backend API's register method.
     *
     * @param registerDTO The data transfer object containing the user's information for registration.
     * @return A [RegisterResponseDTO] containing the registration response, including the token and user data.
     */
    override suspend fun register(registerDTO: RegisterDTO): RegisterResponseDTO =
        authApiServices.register(registerDTO)

    /**
     * Refreshes the authentication token by calling the backend API's refresh method.
     *
     * @param refreshDTO The data transfer object containing the refresh token.
     * @return A [RefreshTokenResponseDTO] containing the new authentication and refresh tokens.
     */
    override suspend fun refresh(refreshDTO: RefreshDTO): RefreshTokenResponseDTO =
        authApiServices.refresh(refreshDTO)

    /**
     * Initiates the password recovery process by calling the backend API's forgot password method.
     *
     * @param forgotPasswordDTO The data transfer object containing the user's email address for password recovery.
     * @return A [ForgotPasswordResponseDTO] containing a message indicating the result of the recovery request.
     */
    override suspend fun forgotPassword(forgotPasswordDTO: ForgotPasswordDTO): ForgotPasswordResponseDTO =
        authApiServices.forgotPassword(forgotPasswordDTO)
}
