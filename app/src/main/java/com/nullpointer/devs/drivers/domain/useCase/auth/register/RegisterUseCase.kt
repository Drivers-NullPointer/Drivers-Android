package com.nullpointer.devs.drivers.domain.useCase.auth.register

import com.nullpointer.devs.drivers.domain.model.CredentialsData
import com.nullpointer.devs.drivers.domain.model.RegisterData
import kotlinx.coroutines.CoroutineScope

interface RegisterUseCase {

    fun register(
        scope: CoroutineScope,
        onError: suspend (Int) -> Unit,
        onStarted: suspend () -> Unit,
        onFinished: suspend () -> Unit,
        registerData: RegisterData
    )
}