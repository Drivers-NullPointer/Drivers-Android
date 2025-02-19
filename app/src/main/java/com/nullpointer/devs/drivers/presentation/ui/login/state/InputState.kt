package com.nullpointer.devs.drivers.presentation.ui.login.state

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

@Stable
open class InputState(
    currentValue: String = "",
    @StringRes val label: Int? = null,
    @StringRes val hint: Int? = null,
    val validators: List<ValidatorRule>,
    private val savedStateHandle: SavedStateHandle,
    private val key: String
) {
    val value = savedStateHandle.getStateFlow(key, currentValue)

    private val _error = MutableStateFlow<Int?>(null)
    val error: StateFlow<Int?> = _error.asStateFlow()
    val hasError: Flow<Boolean> = _error.map { it != null }

    fun onValueChanged(value: String) {
        savedStateHandle[key] = value
        _error.value = validators
            .filter { it.validateOnChange }
            .firstNotNullOfOrNull { it.validator(value) }
    }

    fun validate() {
        _error.value = validators.firstNotNullOfOrNull { it.validator(value.value) }
    }

    fun setError(@StringRes error: Int) {
        _error.value = error
    }
}