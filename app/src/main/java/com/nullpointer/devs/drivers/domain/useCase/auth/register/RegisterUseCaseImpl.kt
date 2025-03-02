package com.nullpointer.devs.drivers.domain.useCase.auth.register

import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.domain.model.RegisterData
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

/**
 * Implementation of `RegisterUseCase` that handles user registration
 * using `authRepository` to interact with the authentication system.
 *
 * @property authRepository Authentication repository for user registration.
 */
class RegisterUseCaseImpl(
    private val authRepository: AuthRepository
) : RegisterUseCase {

    /**
     * Registers a user using the provided `registerData`.
     *
     * @param scope `CoroutineScope` where the operation will be executed.
     * @param onError Callback executed if an error occurs, receiving an error code.
     * @param onStarted Callback executed before starting the registration process.
     * @param onFinished Callback executed after the operation completes, whether successful or not.
     * @param registerData User registration data.
     */
    override fun register(
        scope: CoroutineScope,
        onError: suspend (Int) -> Unit,
        onStarted: suspend () -> Unit,
        onFinished: suspend () -> Unit,
        registerData: RegisterData
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { onStarted() }
                authRepository.registerCredentials(registerData)
            } catch (e: Exception) {
                val errorMessage = handleRegisterError(e)
                withContext(Dispatchers.Main) { onError(errorMessage) }
            } finally {
                withContext(Dispatchers.Main) { onFinished() }
            }
        }
    }

    /**
     * Handles specific exceptions that may occur during registration.
     * Returns a string resource identifier (`R.string`) based on the error type.
     *
     * @param exception Exception caught during registration.
     * @return Error code corresponding to the exception type.
     */
    private fun handleRegisterError(exception: Exception): Int {
        return when (exception) {
            is CancellationException -> throw exception
            is AuthException.RegisterException.UserAlreadyExistsException -> {
                Timber.e("User already exists: ${exception.message}")
                R.string.error_user_already_exists
            }
            is AuthException.RegisterException.TooManyRequestsException -> {
                Timber.e("Too many requests: ${exception.message}")
                R.string.error_too_many_requests
            }
            is AuthException.RegisterException.ServerException -> {
                Timber.e("Server error: ${exception.message}")
                R.string.error_server
            }
            else -> {
                Timber.e("Unknown error: ${exception.message}")
                R.string.error_server
            }
        }
    }
}

