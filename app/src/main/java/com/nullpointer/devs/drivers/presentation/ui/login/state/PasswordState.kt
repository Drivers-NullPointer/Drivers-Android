package com.nullpointer.devs.drivers.presentation.ui.login.state

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PasswordState(
    currentValue: String = "",
    @StringRes
    label: Int? = null,
    @StringRes
    hint: Int? = null,
    validators: List<(String) -> Int?>,
    savedStateHandle: SavedStateHandle,
    key: String
) : InputState(
    currentValue = currentValue,
    validators = validators,
    label = label,
    hint = hint,
    savedStateHandle = savedStateHandle,
    key = key
) {

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible.asStateFlow()
}