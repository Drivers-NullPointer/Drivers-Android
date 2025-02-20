package com.nullpointer.devs.drivers.presentation.ui.auth.forgotPassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.InputState
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.ValidatorRule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
):ViewModel() {

    companion object{
        const val KEY_EMAIL_FORGOT_PASSWORD = "EMAIL_FORGOT_PASSWORD"
    }

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
}