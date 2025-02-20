package com.nullpointer.devs.drivers.presentation.ui.auth.forgotPassword

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.InputState
import com.nullpointer.devs.drivers.presentation.ui.components.TextFieldComponent

@Composable
fun ForgotPasswordComponent(
    forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()
) {

    ForgotPasswordComponent(
        emailInputState = forgotPasswordViewModel.emailInputState,
        validateForm = {

        }
    )
}


@Composable
private fun ForgotPasswordComponent(
    emailInputState: InputState,
    modifier: Modifier = Modifier,
    validateForm: () -> Unit = {},
    forgotPasswordAction: () -> Unit = {}
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
                        text = stringResource(R.string.reset_password_text),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black
                    )


                    TextFieldComponent(
                        state = emailInputState,
                        modifier = Modifier.fillMaxWidth()
                    )

                        ExtendedFloatingActionButton(
                            onClick = validateForm,
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text(text = stringResource(R.string.send_reset_link))
                        }

                    }
                }
            }
        }
    }
