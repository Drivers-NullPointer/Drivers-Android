package com.nullpointer.devs.drivers.presentation.ui.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.presentation.ui.login.state.InputState
import com.nullpointer.devs.drivers.presentation.ui.login.state.PasswordState


@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    state: InputState,
) {

    val value by state.value.collectAsState()
    val error by state.error.collectAsState()

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            isError = error != null,
            onValueChange = state::onValueChanged,
            label = state.label?.let { { Text(text = stringResource(id = it)) } },
            placeholder = state.hint?.let { { Text(text = stringResource(id = it)) } },
        )
        Text(
            text = error?.let { stringResource(id = it) } ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall
        )

    }
}

@Composable
fun PasswordFieldComponent(
    modifier: Modifier = Modifier,
    state: PasswordState,
) {

    val value by state.value.collectAsState()
    val error by state.error.collectAsState()
    val isPasswordVisible by state.isPasswordVisible.collectAsState()

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            isError = error != null,
            onValueChange = state::onValueChanged,
            label = state.label?.let { { Text(text = stringResource(id = it)) } },
            placeholder = state.hint?.let { { Text(text = stringResource(id = it)) } },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = state::togglePasswordVisibility
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = stringResource(id = R.string.password_visibility_toggle)
                    )
                }
            }
        )
        Text(
            text = error?.let { stringResource(id = it) } ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall
        )

    }
}