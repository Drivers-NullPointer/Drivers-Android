package com.nullpointer.devs.drivers.presentation.state

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.spec.DestinationSpec

class MainScreenState(
    contextState: State<Context>,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController?,
    val navHostController: NavHostController
):BasicScreenState(
    snackbarHostState = snackbarHostState,
    contextState = contextState,
    keyboardController = keyboardController
),NavigateRoot{

    override fun navigate(destination: DestinationSpec) {
        navHostController.navigate(destination.route)
    }

    override fun navigateBack() {
        navHostController.popBackStack()
    }

}




@Composable
fun rememberMainScreenState(
    navHostController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): MainScreenState {
    val context = rememberUpdatedState(LocalContext.current)
    val keyboardController = LocalSoftwareKeyboardController.current

    return remember(snackbarHostState, keyboardController) {
        MainScreenState(
            contextState = context,
            snackbarHostState = snackbarHostState,
            keyboardController = keyboardController,
            navHostController = navHostController
        )
    }
}