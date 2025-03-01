package com.nullpointer.devs.drivers.data.remote.auth

import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
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
import retrofit2.HttpException

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
     * @throws AuthException.LoginException.UserNotFoundException If the user is not found (HTTP 401).
     * @throws AuthException.LoginException.InvalidCredentialsException If the credentials are invalid (HTTP 403).
     * @throws AuthException.LoginException.ServerException If an unexpected server error occurs.
     * @throws AuthException.LoginException.TooManyRequestsException If too many requests are made (HTTP 429).
     * @throws AuthException.UnknownException If an unknown error occurs.
     */
    override suspend fun login(loginDTO: LoginDTO): LoginResponseDTO {
        return try {
            authApiServices.login(loginDTO)
        } catch (e: HttpException) {
            throw when (e.code()) {
                404 -> AuthException.LoginException.UserNotFoundException(e.message())
                403 -> AuthException.LoginException.InvalidCredentialsException(e.message())
                429 -> AuthException.LoginException.TooManyRequestsException(e.message())
                else -> AuthException.LoginException.ServerException(e.message())
            }
        } catch (e: Exception) {
            throw AuthException.UnknownException(e.message ?: "An unknown error occurred while logging in")
        }
    }

    /**
     * Registers a new user by calling the backend API's register method.
     *
     * @param registerDTO The data transfer object containing the user's information for registration.
     * @return A [RegisterResponseDTO] containing the registration response, including the token and user data.
     * @throws AuthException.RegisterException.UserAlreadyExistsException If the user already exists (HTTP 409).
     * @throws AuthException.RegisterException.ServerException If an unexpected server error occurs.
     * @throws AuthException.RegisterException.TooManyRequestsException If too many requests are made (HTTP 429).
     * @throws AuthException.UnknownException If an unknown error occurs.
     */
    override suspend fun register(registerDTO: RegisterDTO): RegisterResponseDTO{
        return try {
            authApiServices.register(registerDTO)
        } catch (e: HttpException) {
            throw when (e.code()) {
                409 -> AuthException.RegisterException.UserAlreadyExistsException(e.message())
                429 -> AuthException.RegisterException.TooManyRequestsException(e.message())
                else -> AuthException.RegisterException.ServerException(e.message())
            }
        } catch (e: Exception) {
            throw AuthException.UnknownException(e.message ?: "An unknown error occurred while registering")
        }
    }

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
     * @throws AuthException.ForgotException.UserNotFoundException If the user is not found (HTTP 404).
     * @throws AuthException.ForgotException.EmailUserNotVerifiedException If the email is not verified (HTTP 403).
     * @throws AuthException.ForgotException.TooManyRequestsException If too many requests are made (HTTP 429).
     * @throws AuthException.ForgotException.ServerException If an unexpected server error occurs.
     * @throws AuthException.UnknownException If an unknown error occurs.
     */
    override suspend fun forgotPassword(forgotPasswordDTO: ForgotPasswordDTO): ForgotPasswordResponseDTO {
        return try {
            authApiServices.forgotPassword(forgotPasswordDTO)
        } catch (e:HttpException){
            throw when(e.code()){
                403 -> AuthException.ForgotException.EmailUserNotVerifiedException(e.message())
                404 -> AuthException.ForgotException.UserNotFoundException(e.message())
                429 -> AuthException.ForgotException.TooManyRequestsException(e.message())
                else -> AuthException.ForgotException.ServerException(e.message())
            }
        }catch (e:Exception){
            throw AuthException.UnknownException(e.message ?: "An unknown error occurred while recovering password")
        }
    }

    /**
     * Checks if the email is verified by calling the backend API's check verify email method.
     *
     * @param checkVerifyEmailDTO The data transfer object containing the user's email address for verification.
     * @return A [CheckVerifyEmailResponseDTO] containing a message indicating the result of the verification request.
     * @throws AuthException.CheckVerifyEmailException.ServerException If an unexpected server error occurs.
     * @throws AuthException.CheckVerifyEmailException.UnauthorizedException If the request is unauthorized (HTTP 401).
     * @throws AuthException.UnknownException If an unknown error occurs.
     */
    override suspend fun checkVerifyEmail(checkVerifyEmailDTO: CheckVerifyEmailDTO): CheckVerifyEmailResponseDTO {
        return try {
            authApiServices.checkVerifyEmail(checkVerifyEmailDTO)
        } catch (e: HttpException) {
            throw when (e.code()) {
                401 -> AuthException.CheckVerifyEmailException.UnauthorizedException(e.message())
                else -> AuthException.CheckVerifyEmailException.ServerException(e.message())
            }
        } catch (e: Exception) {
            throw AuthException.UnknownException(
                e.message ?: "An unknown error occurred while checking email verification"
            )
        }
    }
}
