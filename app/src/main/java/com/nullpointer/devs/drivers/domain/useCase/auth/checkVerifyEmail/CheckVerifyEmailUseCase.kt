package com.nullpointer.devs.drivers.domain.useCase.auth.checkVerifyEmail

import kotlinx.coroutines.CoroutineScope

interface CheckVerifyEmailUseCase {

    fun checkVerifyEmail(
        scope: CoroutineScope,
        onError: suspend (Int) -> Unit,
        onStarted: suspend () -> Unit,
        onFinished: suspend () -> Unit,
        onSuccessful: suspend () -> Unit
    )

}