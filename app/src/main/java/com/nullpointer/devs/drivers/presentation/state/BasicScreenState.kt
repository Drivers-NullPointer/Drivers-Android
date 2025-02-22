package com.nullpointer.devs.drivers.presentation.state

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

class BasicScreenState(
    val snackbarHostState: SnackbarHostState,
) {
    suspend fun showSnackbar(message: String) {
        snackbarHostState.showSnackbar(message)
    }

    suspend fun showSnackbar(
        @StringRes message: Int,
        context: Context,
    ) {
        snackbarHostState.showSnackbar(context.getString(message))
    }
}


@Composable
fun rememberBasicScreenState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
)= remember(
    snackbarHostState
) {
    BasicScreenState(
        snackbarHostState = snackbarHostState
    )
}