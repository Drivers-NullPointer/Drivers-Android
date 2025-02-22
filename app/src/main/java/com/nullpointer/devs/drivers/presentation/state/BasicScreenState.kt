package com.nullpointer.devs.drivers.presentation.state

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController

class BasicScreenState(
    private val contextState: State<Context>,
    val snackbarHostState: SnackbarHostState,
    private val keyboardController: SoftwareKeyboardController?,
) {
    suspend fun showSnackbar(message: String) {
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(message)
    }

    suspend fun showSnackbar(@StringRes messageRes: Int) {
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(contextState.value.getString(messageRes))
    }

    fun hideKeyboard() {
        keyboardController?.hide()
    }
}

@Composable
fun rememberBasicScreenState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): BasicScreenState {
    val context = rememberUpdatedState(LocalContext.current)
    val keyboardController = LocalSoftwareKeyboardController.current

    return remember(snackbarHostState, keyboardController) {
        BasicScreenState(
            contextState = context,
            snackbarHostState = snackbarHostState,
            keyboardController = keyboardController
        )
    }
}
