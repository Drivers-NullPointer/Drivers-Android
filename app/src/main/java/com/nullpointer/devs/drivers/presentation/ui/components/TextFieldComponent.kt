package com.nullpointer.devs.drivers.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import com.nullpointer.devs.drivers.presentation.ui.auth.login.state.InputState


@Composable
fun TextFieldComponent(
    state: InputState,
    modifier: Modifier = Modifier,
    isEnable: Boolean = true,
    singleLine: Boolean = true
) {

    val value by state.value.collectAsState()
    val error by state.error.collectAsState()

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth(),
            value = value,
            isError = error != null,
            onValueChange = state::onValueChanged,
            label = state.label?.let { { Text(text = stringResource(id = it)) } },
            placeholder = state.hint?.let { { Text(text = stringResource(id = it)) } },
            enabled = isEnable
        )
        Text(
            text = error?.let { stringResource(id = it) } ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall
        )

    }
}


@Preview(
    showBackground = true
)
@Composable
private fun TextFieldComponentPreview() {
    TextFieldComponent(
        state = InputState(
            currentValue = "value",
            label = null,
            hint = null,
            validators = emptyList(),
            savedStateHandle = SavedStateHandle(),
            key = ""
        )
    )
}
