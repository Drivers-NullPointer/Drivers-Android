package com.nullpointer.devs.drivers.presentation.components.auth.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.presentation.ui.login.state.InputState

class LoginViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object{
        const val KEY_EMAIL_LOGIN = "EMAIL_LOGIN"
        const val KEY_PASSWORD_LOGIN = "PASSWORD_LOGIN"
    }

    val emailInputState = InputState(
        savedStateHandle = savedStateHandle,
        validators = listOf(
            { value -> if (value.isEmpty()) R.string.error_empty_mail else null },
            { value -> if (!value.contains("@")) R.string.error_invalid_mail else null }
        ),
        key = KEY_EMAIL_LOGIN
    )

    val passwordInputState = InputState(
        savedStateHandle = savedStateHandle,
        key = KEY_PASSWORD_LOGIN,
        validators = listOf { value -> if (value.isEmpty()) R.string.error_empty_password else null },
    )
}