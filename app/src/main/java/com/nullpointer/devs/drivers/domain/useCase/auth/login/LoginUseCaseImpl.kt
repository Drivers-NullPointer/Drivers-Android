package com.nullpointer.devs.drivers.domain.useCase.auth.login

import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.domain.model.CredentialsData
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Implementation of [LoginUseCase] that handles the login process.
 *
 * @property authRepository Repository for handling authentication data.
 */
/**
 * Implementation of the LoginUseCase interface that handles user authentication.
 * This class is responsible for invoking the authentication process and handling possible errors.
 *
 * @property authRepository The repository responsible for authentication operations.
 */
class LoginUseCaseImpl(
    private val authRepository: AuthRepository
) : LoginUseCase {

    /**
     * Initiates the login process for the user.
     *
     * @param scope The CoroutineScope in which the login process will be executed.
     * @param onError The callback to handle errors that occur during the login process.
     * @param credentialsData The user's credentials (email and password).
     */
    override fun login(
        scope: CoroutineScope,
        onError: suspend (Int) -> Unit,
        onStarted: suspend () -> Unit,
        onFinished: suspend () -> Unit,
        credentialsData: CredentialsData
    ) {
        scope.launch(Dispatchers.IO) { // Launches a coroutine on the IO dispatcher for network operations.
            try {
                withContext(Dispatchers.Main) { onStarted() } // Notifies the UI that the login process has started.
                authRepository.loginCredentials(credentialsData) // Calls the repository to authenticate the user.
            } catch (e: Exception) {
                val errorMessage = handleLoginError(e) // Determines the appropriate error message.
                withContext(Dispatchers.Main) { onError(errorMessage) } // Ensures UI updates occur on the main thread.
            }finally {
                withContext(Dispatchers.Main) { onFinished() } // Notifies the UI that the login process has finished.
            }
        }
    }

    /**
     * Handles exceptions that may occur during the login process.
     *
     * @param exception The thrown exception.
     * @return The corresponding string resource ID for the error message.
     */
    private fun handleLoginError(exception: Exception): Int {
        return when (exception) {
            is AuthException.LoginException.UserNotFoundException -> {
                Timber.e("User not found while logging in: $exception")
                R.string.error_user_not_found
            }
            is AuthException.LoginException.InvalidCredentialsException -> {
                Timber.e("Invalid credentials while logging in: $exception")
                R.string.error_invalid_credentials
            }
            is AuthException.LoginException.ServerException -> {
                Timber.e("Server error while logging in: $exception")
                R.string.error_server
            }
            is AuthException.LoginException.TooManyRequestsException -> {
                Timber.e("Too many requests while logging in: $exception")
                R.string.error_too_many_requests
            }
            else -> throw exception // Throws unknown exceptions to be handled at a higher level.
        }
    }
}
