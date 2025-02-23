package com.nullpointer.devs.drivers.domain.useCase.auth.forgot

import com.nullpointer.devs.drivers.domain.model.ForgotPasswordData
import kotlinx.coroutines.CoroutineScope

interface ForgotPasswordUseCase {

    fun forgotPassword(
        scope: CoroutineScope,
        onError: suspend (Int) -> Unit,
        onStarted: suspend () -> Unit,
        onFinished: suspend () -> Unit,
        forgotData: ForgotPasswordData
    )
}