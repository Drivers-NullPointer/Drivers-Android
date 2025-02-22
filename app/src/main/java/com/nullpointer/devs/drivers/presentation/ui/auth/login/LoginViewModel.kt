package com.nullpointer.devs.drivers.presentation.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.domain.model.CredentialsData
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.InputState
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.PasswordState
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.ValidatorRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
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
        hint = R.string.email_hint
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
        hint = R.string.password_hint
    )

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


    fun login(
        credentialsData: CredentialsData
    ) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        try {
            authRepository.loginCredentials(credentialsData)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}


