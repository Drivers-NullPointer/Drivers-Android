package com.nullpointer.devs.drivers.presentation.components.auth.login

import androidx.lifecycle.ViewModel
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

}