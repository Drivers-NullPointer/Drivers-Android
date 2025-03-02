package com.nullpointer.devs.drivers.domain.useCase.auth.login

import com.nullpointer.devs.drivers.domain.model.CredentialsData
import kotlinx.coroutines.CoroutineScope

interface LoginUseCase {

    fun login(
        scope: CoroutineScope,
        onError: suspend (Int) -> Unit,
        onStarted: suspend () -> Unit,
        onFinished: suspend () -> Unit,
        credentialsData: CredentialsData,
    )
}