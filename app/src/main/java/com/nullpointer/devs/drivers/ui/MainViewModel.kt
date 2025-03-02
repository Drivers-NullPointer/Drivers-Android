package com.nullpointer.devs.drivers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.devs.drivers.domain.model.UserAuthState
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository
):ViewModel() {

    val userAuthState = authRepository.authData.map {
        when {
            it == null -> UserAuthState.UNAUTHENTICATED
            it.isEmailVerified -> UserAuthState.AUTHENTICATED
            else -> UserAuthState.EMAIL_NOT_VERIFIED
        }
    }.flowOn(Dispatchers.IO)
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserAuthState.UNKNOWN
    )
}