package com.nullpointer.devs.drivers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nullpointer.devs.drivers.presentation.state.MainScreenState
import com.nullpointer.devs.drivers.presentation.state.NavigateRoot
import com.nullpointer.devs.drivers.presentation.state.rememberMainScreenState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.navigation.dependency

@Composable
fun MainComponent(
    modifier: Modifier = Modifier,
    mainScreenState: MainScreenState = rememberMainScreenState()
) {

    DestinationsNavHost(
        navGraph = NavGraphs.root,
        modifier = modifier,
        navController = mainScreenState.navHostController,
        dependenciesContainerBuilder = {
            dependency(mainScreenState as NavigateRoot)
        }
    )

}