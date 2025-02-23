package com.nullpointer.devs.drivers.presentation.ui.auth.forgotPassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.domain.model.ForgotPasswordData
import com.nullpointer.devs.drivers.domain.useCase.auth.forgot.ForgotPasswordUseCase
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.InputState
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.ValidatorRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
):ViewModel() {

    companion object{
        const val KEY_EMAIL_FORGOT_PASSWORD = "EMAIL_FORGOT_PASSWORD"
    }

    private val _message = Channel<Int>()
    val message = _message.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _backAction = Channel<Unit>()
    val backAction = _backAction.receiveAsFlow()


    val emailInputState = InputState(
        savedStateHandle = savedStateHandle,
        key = KEY_EMAIL_FORGOT_PASSWORD,
        label = R.string.email,
        hint = R.string.email,
        validators = listOf(
            ValidatorRule(
                validator = { value -> if (value.isEmpty()) R.string.error_empty_mail else null },
                validateOnChange = false
            )
        )
    )

    fun validateForm(): ForgotPasswordData? {
        emailInputState.validate()

        val hasEmailError = emailInputState.error.value != null

        return if (hasEmailError) {
            null
        } else {
            ForgotPasswordData(
                email = emailInputState.value.value
            )
        }
    }

    fun forgotPassword(
        forgotData: ForgotPasswordData
    ) {
        forgotPasswordUseCase.forgotPassword(
            scope = viewModelScope,
            forgotData = forgotData,
            onError = { _message.send(it) },
            onStarted = { _isLoading.value = true },
            onFinished = { _isLoading.value = false },
            onSuccessful = { _backAction.send(Unit) }
        )
    }


}