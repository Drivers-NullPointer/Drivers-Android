package com.nullpointer.devs.drivers.presentation.ui.auth.register

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.InputState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.domain.model.RegisterData
import com.nullpointer.devs.drivers.domain.useCase.auth.register.RegisterUseCase
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.PasswordState
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.ValidatorRule
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class RegisterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    companion object{
        const val KEY_EMAIL_REGISTER = "EMAIL_REGISTER"
        const val KEY_PASSWORD_REGISTER = "PASSWORD_REGISTER"
        const val KEY_NAME_REGISTER = "NAME_REGISTER"
        const val KEY_LAST_NAME_REGISTER = "LAST_NAME_REGISTER"
        const val KEY_BIRTHDAY_REGISTER = "BIRTHDAY_REGISTER"


        const val MIN_NAME_LENGTH = 3
        const val MAX_NAME_LENGTH = 50

        const val MIN_LAST_NAME_LENGTH = 3
        const val MAX_LAST_NAME_LENGTH = 50

        const val MIN_PASSWORD_LENGTH = 6
        const val MAX_PASSWORD_LENGTH = 50

        const val MAX_EMAIL_LENGTH = 50
    }

    val nameInputState = InputState(
        savedStateHandle = savedStateHandle,
        key = KEY_NAME_REGISTER,
        label = R.string.name,
        hint = R.string.name,
        maxLength = MAX_NAME_LENGTH,
        validators = listOf(
            ValidatorRule(
                validator = { value -> if (value.isEmpty()) R.string.error_empty_name else null },
                validateOnChange = false
            ),
            ValidatorRule(
                validator = { value ->
                    if (value.length < MIN_NAME_LENGTH) R.string.error_min_name_length else null
                },
                validateOnChange = false
            ),
            ValidatorRule(
                validator = { value ->
                    if (value.length > MAX_NAME_LENGTH) R.string.error_max_name_length else null
                },
                validateOnChange = false
            )
        )
    )

    val lastNameInputState = InputState(
        savedStateHandle = savedStateHandle,
        key = KEY_LAST_NAME_REGISTER,
        label = R.string.last_name,
        hint = R.string.last_name,
        maxLength = MAX_LAST_NAME_LENGTH,
        validators = listOf(
            ValidatorRule(
                validator = { value -> if (value.isEmpty()) R.string.error_empty_last_name else null },
                validateOnChange = false
            ),
            ValidatorRule(
                validator = { value ->
                    if (value.length < MIN_LAST_NAME_LENGTH) R.string.error_min_last_name_length else null
                },
                validateOnChange = false
            ),
            ValidatorRule(
                validator = { value ->
                    if (value.length > MAX_LAST_NAME_LENGTH) R.string.error_max_last_name_length else null
                },
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
            ),
            ValidatorRule(
                validator = { value ->
                    if (value.length > MAX_EMAIL_LENGTH) R.string.error_max_email_length else null
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
            ),
            ValidatorRule(
                validator = { value ->
                    if (value.length < MIN_PASSWORD_LENGTH) R.string.error_min_password_length else null
                },
                validateOnChange = false
            ),
            ValidatorRule(
                validator = { value ->
                    if (value.length > MAX_PASSWORD_LENGTH) R.string.error_max_password_length else null
                },
                validateOnChange = false
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

    private val _message = Channel<Int>()
    val message = _message.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun validateFields(): RegisterData? {
        nameInputState.validate()
        lastNameInputState.validate()
        emailInputState.validate()
        passwordInputState.validate()
        birthdayInputState.validate()

        val isValidName = nameInputState.error.value == null
        val isValidLastName = lastNameInputState.error.value == null
        val isValidEmail = emailInputState.error.value == null
        val isValidPassword = passwordInputState.error.value == null
        val isValidBirthday = birthdayInputState.error.value == null

        return if (!isValidName || !isValidLastName || !isValidEmail || !isValidPassword || !isValidBirthday) {
            null
        } else {
            RegisterData(
                name = nameInputState.value.value,
                lastname = lastNameInputState.value.value,
                email = emailInputState.value.value,
                password = passwordInputState.value.value,
                birthdate = birthdayInputState.value.value
            )
        }
    }

    fun togglePasswordVisibility() {
        passwordInputState.togglePasswordVisibility()
    }


    fun register(
        registerData: RegisterData
    ) {
        registerUseCase.register(
            registerData = registerData,
            scope = viewModelScope,
            onStarted = { _isLoading.value = true },
            onFinished = { _isLoading.value = false },
            onError = { errorResId -> _message.send(errorResId) },
        )
    }

}