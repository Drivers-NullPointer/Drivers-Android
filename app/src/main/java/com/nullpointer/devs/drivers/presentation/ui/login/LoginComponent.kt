package com.nullpointer.devs.drivers.presentation.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.devs.drivers.presentation.ui.login.components.PasswordFieldComponent
import com.nullpointer.devs.drivers.presentation.ui.login.components.TextFieldComponent
import com.nullpointer.devs.drivers.presentation.ui.login.state.InputState
import com.nullpointer.devs.drivers.presentation.ui.login.state.PasswordState


@Composable
fun LoginComponent(
    viewModel: LoginViewModel = hiltViewModel()
) {

    LoginComponent(
        modifier = Modifier.fillMaxSize(),
        emailInputState = viewModel.emailInputState,
        passwordState = viewModel.passwordInputState
    )
}





@Composable
private fun LoginComponent(
    modifier: Modifier = Modifier,
    emailInputState: InputState,
    passwordState: PasswordState
) {
    Scaffold(
        modifier = modifier
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
                        text = "Login",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black
                    )


                    TextFieldComponent(
                        state = emailInputState,
                        modifier = Modifier.fillMaxWidth()
                    )

                    PasswordFieldComponent(
                        state = passwordState,
                        modifier = Modifier.fillMaxWidth()
                    )


                    ExtendedFloatingActionButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(text = "Login")
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
private fun LoginComponentPreview() {
    LoginComponent()
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.TABLET
)
@Composable
private fun LoginComponentPreviewTablet() {
    LoginComponent()
}