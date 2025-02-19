package com.nullpointer.devs.drivers.presentation.ui.login.state

import androidx.compose.runtime.Stable

@Stable
data class ValidatorRule(
    val validator: (String) -> Int?,
    val validateOnChange: Boolean
)
