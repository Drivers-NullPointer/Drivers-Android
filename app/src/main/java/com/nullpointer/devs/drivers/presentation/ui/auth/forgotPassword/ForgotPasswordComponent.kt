package com.nullpointer.devs.drivers.presentation.ui.auth.forgotPassword

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.presentation.state.BasicScreenState
import com.nullpointer.devs.drivers.presentation.state.NavigateRoot
import com.nullpointer.devs.drivers.presentation.state.rememberBasicScreenState
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.InputState
import com.nullpointer.devs.drivers.presentation.ui.components.TextFieldComponent
import com.nullpointer.devs.drivers.presentation.ui.graph.AuthGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import timber.log.Timber

@Destination<AuthGraph>
@Composable
fun ForgotPasswordComponent(
    navigateRoot: NavigateRoot,
    destinationsNavigator: DestinationsNavigator,
    forgotScreenState:BasicScreenState = rememberBasicScreenState(),
    forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel(),
) {

    LaunchedEffect(key1 = Unit) {
        forgotPasswordViewModel.message.collect {
            forgotScreenState.showSnackbar(it)
        }
    }

    LaunchedEffect(key1 =Unit) {
        forgotPasswordViewModel.backAction.collect {
            forgotScreenState.showSnackbar(R.string.reset_password_send_success)
            delay(1000)
            destinationsNavigator.popBackStack()
        }
    }


    ForgotPasswordComponent(
        emailInputState = forgotPasswordViewModel.emailInputState,
        snackbarHostState = forgotScreenState.snackbarHostState,
        forgotPasswordAction = {
            forgotScreenState.hideKeyboard()
            forgotPasswordViewModel.validateForm()?.let {
                forgotPasswordViewModel.forgotPassword(it)
            }
        }
    )
}


@Composable
private fun ForgotPasswordComponent(
    emailInputState: InputState,
    modifier: Modifier = Modifier,
    forgotPasswordAction: () -> Unit = {},
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                ,
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(24.dp)
                        .widthIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.reset_password_text),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black
                    )


                    TextFieldComponent(
                        state = emailInputState,
                        modifier = Modifier.fillMaxWidth()
                    )

                        ExtendedFloatingActionButton(
                            onClick = forgotPasswordAction,
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text(text = stringResource(R.string.send_reset_link))
                        }

                    }
                }
            }
        }
    }


@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun ForgotPasswordComponentPreview() {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val saveStateHandle = remember {
        SavedStateHandle()
    }
    ForgotPasswordComponent(
        emailInputState = InputState(
            label = R.string.email,
            hint = R.string.email,
            validators = listOf(),
            key = "email",
            savedStateHandle = saveStateHandle,
            currentValue = ""
        ),
        snackbarHostState = snackbarHostState
    )

}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.TABLET
)
@Composable
fun ForgotPasswordComponentPreviewTablet() {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val saveStateHandle = remember {
        SavedStateHandle()
    }
    ForgotPasswordComponent(
        emailInputState = InputState(
            label = R.string.email,
            hint = R.string.email,
            validators = listOf(),
            key = "email",
            savedStateHandle = saveStateHandle,
            currentValue = ""
        ),
        snackbarHostState = snackbarHostState
    )

}