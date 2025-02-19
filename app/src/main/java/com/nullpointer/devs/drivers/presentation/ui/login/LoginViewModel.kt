package com.nullpointer.devs.drivers.presentation.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.presentation.ui.login.state.InputState
import com.nullpointer.devs.drivers.presentation.ui.login.state.PasswordState
import com.nullpointer.devs.drivers.presentation.ui.login.state.ValidatorRule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object{
        const val KEY_EMAIL_LOGIN = "EMAIL_LOGIN"
        const val KEY_PASSWORD_LOGIN = "PASSWORD_LOGIN"
    }

    val emailInputState = InputState(
        savedStateHandle = savedStateHandle,
        validators = listOf(
            ValidatorRule(
                validator = { value -> if (value.isEmpty()) R.string.error_empty_mail else null },
                validateOnChange = true
            ),
            ValidatorRule(
                validator = { value -> if (!value.contains("@")) R.string.error_invalid_mail else null },
                validateOnChange = false
            )
        ),
        key = KEY_EMAIL_LOGIN
    )

    val passwordInputState = PasswordState(
        savedStateHandle = savedStateHandle,
        key = KEY_PASSWORD_LOGIN,
        validators = listOf(
            ValidatorRule(
                validator = { value -> if (value.isEmpty()) R.string.error_empty_password else null },
                validateOnChange = true
            )
        ),
    )
}