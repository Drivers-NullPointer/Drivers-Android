package com.nullpointer.devs.drivers.domain.useCase.auth.checkVerifyEmail

import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

class CheckVerifyEmailUseCaseImpl(
    private val authRepository: AuthRepository
) : CheckVerifyEmailUseCase {

    override fun checkVerifyEmail(
        scope: CoroutineScope
    ) {
        scope.launch(
            Dispatchers.IO
        ) {
            try {
                authRepository.checkVerifyEmail()
            } catch (e: Exception) {
                handleLoginError(e)
            }
        }
    }


    /**
     * Handles exceptions that may occur during the email verification process.
     *
     * @param exception Exception that occurred during the email verification process.
     * @throws CancellationException If the exception is a [CancellationException].
     *
     */
    private fun handleLoginError(exception: Exception) {
        when (exception) {
            is CancellationException -> throw exception
            is AuthException.CheckVerifyEmailException.UserNotFoundException -> {
                Timber.e("User not found while verifying email: $exception")
            }

            is AuthException.CheckVerifyEmailException.UnauthorizedException -> {
                Timber.e("Unauthorized while verifying email: $exception")
            }
            else -> {
                Timber.e("Error while verifying email: $exception")
            }
        }
    }

}