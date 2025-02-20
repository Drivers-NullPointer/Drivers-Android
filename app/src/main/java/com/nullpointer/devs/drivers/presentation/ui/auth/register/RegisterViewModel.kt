package com.nullpointer.devs.drivers.presentation.ui.auth.register

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.InputState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.PasswordState
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.ValidatorRule

@HiltViewModel
class RegisterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object{
        const val KEY_EMAIL_REGISTER = "EMAIL_REGISTER"
        const val KEY_PASSWORD_REGISTER = "PASSWORD_REGISTER"
        const val KEY_NAME_REGISTER = "NAME_REGISTER"
        const val KEY_LAST_NAME_REGISTER = "LAST_NAME_REGISTER"
        const val KEY_BIRTHDAY_REGISTER = "BIRTHDAY_REGISTER"
    }

    val nameInputState = InputState(
        savedStateHandle = savedStateHandle,
        key = KEY_NAME_REGISTER,
        label = R.string.name,
        hint = R.string.name,
        validators = listOf(
            ValidatorRule(
                validator = { value -> if (value.isEmpty()) R.string.error_empty_name else null },
                validateOnChange = false
            )
        )
    )

    val lastNameInputState = InputState(
        savedStateHandle = savedStateHandle,
        key = KEY_LAST_NAME_REGISTER,
        label = R.string.last_name,
        hint = R.string.last_name,
        validators = listOf(
            ValidatorRule(
                validator = { value -> if (value.isEmpty()) R.string.error_empty_last_name else null },
                validateOnChange = false
            )
        )
    )

    val emailInputState = InputState(
        savedStateHandle = savedStateHandle,
        key = KEY_EMAIL_REGISTER,
        label = R.string.email,
        hint = R.string.email,
        validators = listOf(
            ValidatorRule(
                validator = { value -> if (value.isEmpty()) R.string.error_empty_mail else null },
                validateOnChange = true
            ),
            ValidatorRule(
                validator = { value ->
                    if (!Patterns.EMAIL_ADDRESS.matcher(value)
                            .matches()
                    ) R.string.error_invalid_mail else null
                },
                validateOnChange = false
            )
        )
    )

    val passwordInputState = PasswordState(
        savedStateHandle = savedStateHandle,
        key = KEY_PASSWORD_REGISTER,
        label = R.string.password,
        hint = R.string.password,
        validators = listOf(
            ValidatorRule(
                validator = { value -> if (value.isEmpty()) R.string.error_empty_password else null },
                validateOnChange = true
            )
        )
    )

    val birthdayInputState = InputState(
        savedStateHandle = savedStateHandle,
        key = KEY_BIRTHDAY_REGISTER,
        label = R.string.birthday,
        hint = R.string.birthday,
        validators = listOf(
            ValidatorRule(
                validator = { value -> if (value.isEmpty()) R.string.error_empty_birthday else null },
                validateOnChange = false
            )
        )
    )
}