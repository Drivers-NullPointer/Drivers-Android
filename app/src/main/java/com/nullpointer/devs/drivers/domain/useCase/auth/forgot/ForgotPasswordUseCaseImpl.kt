package com.nullpointer.devs.drivers.domain.useCase.auth.forgot

import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.domain.model.ForgotPasswordData
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


/**
 * Implementation of the [ForgotPasswordUseCase] interface responsible for handling the password recovery process.
 *
 * This class communicates with the [AuthRepository] to initiate the password reset flow. It executes
 * the operation within a coroutine to ensure asynchronous execution and proper thread handling.
 *
 * @property authRepository Repository responsible for authentication operations.
 */
class ForgotPasswordUseCaseImpl(
    private val authRepository: AuthRepository
) : ForgotPasswordUseCase {

    /**
     * Initiates the password recovery process.
     *
     * @param scope Coroutine scope in which the operation is executed.
     * @param onError Callback triggered when an error occurs, receiving an error message resource ID.
     * @param onStarted Callback triggered before the request is sent.
     * @param onFinished Callback triggered after the request completes, whether successfully or with an error.
     * @param onSuccessful Callback triggered when the password recovery process is successful.
     * @param forgotData Data required to initiate the password reset process.
     */
    override fun forgotPassword(
        scope: CoroutineScope,
        onError: suspend (Int) -> Unit,
        onStarted: suspend () -> Unit,
        onFinished: suspend () -> Unit,
        onSuccessful: suspend () -> Unit,
        forgotData: ForgotPasswordData
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { onStarted() }
                authRepository.forgotPassword(forgotData)
                withContext(Dispatchers.Main) { onSuccessful() }
            } catch (e: Exception) {
                val errorMessage = handleLoginError(e)
                withContext(Dispatchers.Main) { onError(errorMessage) }
            } finally {
                withContext(Dispatchers.Main) { onFinished() }
            }
        }
    }

    /**
     * Handles exceptions that may occur during the password recovery process.
     *
     * @param exception The exception thrown during the process.
     * @return The corresponding error message resource ID.
     */
    private fun handleLoginError(exception: Exception): Int {
        return when (exception) {
            is CancellationException -> throw exception
            is AuthException.ForgotException.UserNotFoundException -> {
                Timber.e("User not found while recovering password: $exception")
                R.string.error_user_not_found
            }
            is AuthException.ForgotException.EmailUserNotVerifiedException -> {
                Timber.e("Email not verified while recovering password: $exception")
                R.string.error_email_not_verified
            }
            is AuthException.ForgotException.TooManyRequestsException -> {
                Timber.e("Too many requests while recovering password: $exception")
                R.string.error_too_many_requests
            }
            is AuthException.ForgotException.ServerException -> {
                Timber.e("Server error while recovering password: $exception")
                R.string.error_server
            }
            else -> {
                Timber.e("Unknown error while recovering password: $exception")
                R.string.error_server
            }
        }
    }
}
