package com.nullpointer.devs.drivers.data.remote.auth

import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshTokenResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO
import retrofit2.http.POST


/**
 * Interface that defines the API calls for authentication-related operations.
 * These operations interact with the backend to handle login, registration, token refresh, and password recovery.
 */
interface AuthApiServices {

    /**
     * Makes a POST request to the login endpoint to authenticate the user.
     *
     * @param loginDTO The login data transfer object containing the user's credentials.
     * @return A [LoginResponseDTO] containing the authentication response, including a token and user data.
     */
    @POST("login")
    suspend fun login(loginDTO: LoginDTO): LoginResponseDTO

    /**
     * Makes a POST request to the register endpoint to create a new user account.
     *
     * @param registerDTO The registration data transfer object containing the user's information.
     * @return A [RegisterResponseDTO] containing the registration response, including a token and user data.
     */
    @POST("register")
    suspend fun register(registerDTO: RegisterDTO): RegisterResponseDTO

    /**
     * Makes a POST request to the refresh endpoint to obtain a new authentication token.
     *
     * @param refreshDTO The refresh data transfer object containing the old refresh token.
     * @return A [RefreshTokenResponseDTO] containing the new authentication and refresh tokens.
     */
    @POST("refresh")
    suspend fun refresh(refreshDTO: RefreshDTO): RefreshTokenResponseDTO

    /**
     * Makes a POST request to the forgot-password endpoint to initiate the password recovery process.
     *
     * @param forgotPasswordDTO The data transfer object containing the user's email for password recovery.
     * @return A [ForgotPasswordResponseDTO] containing a message indicating the result of the request.
     */
    @POST("forgot-password")
    suspend fun forgotPassword(forgotPasswordDTO: ForgotPasswordDTO): ForgotPasswordResponseDTO
}
