package com.nullpointer.devs.drivers.presentation.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.domain.model.CredentialsData
import com.nullpointer.devs.drivers.domain.useCase.auth.login.LoginUseCase
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.InputState
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.PasswordState
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.ValidatorRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    companion object{
        const val KEY_EMAIL_LOGIN = "EMAIL_LOGIN"
        const val KEY_PASSWORD_LOGIN = "PASSWORD_LOGIN"
        const val MAX_LENGTH_PASSWORD = 50
        const val MAX_LENGTH_EMAIL = 50
    }

    val emailInputState = InputState(
        savedStateHandle = savedStateHandle,
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
        ),
        key = KEY_EMAIL_LOGIN,
        label = R.string.email,
        hint = R.string.email_hint,
        maxLength = MAX_LENGTH_EMAIL
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
        label = R.string.password,
        hint = R.string.password_hint,
        maxLength = MAX_LENGTH_PASSWORD
    )

    private val _message = Channel<Int>()
    val message = _message.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun validateFields():CredentialsData?{
        emailInputState.validate()
        passwordInputState.validate()

        val isValidEmail = emailInputState.error.value == null
        val isValidPassword = passwordInputState.error.value == null

        return if(!isValidEmail || !isValidPassword) {
            null
        } else {
            CredentialsData(
                email = emailInputState.value.value,
                password = passwordInputState.value.value
            )
        }

    }


    fun login(credentials: CredentialsData) {
        loginUseCase.login(
            scope = viewModelScope,
            credentialsData = credentials,
            onStarted = { _isLoading.value = true },
            onFinished = { _isLoading.value = false },
            onError = { errorResId -> _message.send(errorResId) },
        )
    }
}


