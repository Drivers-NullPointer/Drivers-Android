package com.nullpointer.devs.drivers.presentation.ui.home.travel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nullpointer.devs.drivers.presentation.ui.graph.HomeGraph
import com.ramcosta.composedestinations.annotation.Destination


@Destination<HomeGraph>
@Composable
fun TravelScreen(

) {

    Scaffold() { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Travel",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        }
    }
}