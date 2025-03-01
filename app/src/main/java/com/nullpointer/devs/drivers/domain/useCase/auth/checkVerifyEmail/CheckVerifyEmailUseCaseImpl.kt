package com.nullpointer.devs.drivers.domain.useCase.auth.checkVerifyEmail

import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

class CheckVerifyEmailUseCaseImpl(
    private val authRepository: AuthRepository
) : CheckVerifyEmailUseCase {

    override fun checkVerifyEmail(
        scope: CoroutineScope,
        onError: suspend (Int) -> Unit,
        onStarted: suspend () -> Unit,
        onFinished: suspend () -> Unit,
        onSuccessful: suspend () -> Unit
    ) {
        scope.launch(
            Dispatchers.IO
        ) {
            try {
                withContext(Dispatchers.Main) { onStarted() }
                authRepository.checkVerifyEmail()
                withContext(Dispatchers.Main) { onSuccessful() }
            } catch (e: Exception) {
                val errorMessage = handleLoginError(e)
                withContext(Dispatchers.Main) { onError(errorMessage) }
            } finally {
                withContext(Dispatchers.Main) {
                    onFinished()
                }
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
            is AuthException.CheckVerifyEmailException.UserNotFoundException -> {
                Timber.e("User not found while verifying email: $exception")
                R.string.error_user_not_found
            }

            is AuthException.CheckVerifyEmailException.UnauthorizedException -> {
                Timber.e("Unauthorized while verifying email: $exception")
                R.string.error_server
            }

            else -> {
                Timber.e("Unknown error while recovering password: $exception")
                R.string.error_server
            }
        }
    }

}