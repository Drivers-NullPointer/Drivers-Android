package com.nullpointer.devs.drivers.presentation.ui.auth.email

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.devs.drivers.presentation.ui.graph.EmailGraph
import com.ramcosta.composedestinations.annotation.Destination

@Destination<EmailGraph>(start = true)
@Composable
fun CheckVerifyEmailComponent(
    checkVerifyEmailViewModel: CheckVerifyEmailViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        checkVerifyEmailViewModel.checkVerifyEmail()
    }
    CheckVerifyEmailComponent()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckVerifyEmailComponent() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Verifica tu correo") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tu cuenta no ha sido verificada.\nPor favor revisa tu correo y sigue las instrucciones.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}