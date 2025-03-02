package com.nullpointer.devs.drivers.presentation.ui.auth.login.state

import androidx.compose.runtime.Stable

@Stable
data class ValidatorRule(
    val validator: (String) -> Int?,
    val validateOnChange: Boolean
)
