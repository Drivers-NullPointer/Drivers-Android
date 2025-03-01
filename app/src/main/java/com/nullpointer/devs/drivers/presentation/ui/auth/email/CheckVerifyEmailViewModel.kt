package com.nullpointer.devs.drivers.presentation.ui.auth.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.devs.drivers.domain.useCase.auth.checkVerifyEmail.CheckVerifyEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckVerifyEmailViewModel @Inject constructor(
    private val checkVerifyEmailUseCase: CheckVerifyEmailUseCase
) : ViewModel() {

    fun checkVerifyEmail() {
        checkVerifyEmailUseCase.checkVerifyEmail(
            scope = viewModelScope,
        )
    }
}