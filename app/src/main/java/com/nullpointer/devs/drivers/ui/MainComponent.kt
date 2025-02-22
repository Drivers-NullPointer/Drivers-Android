package com.nullpointer.devs.drivers.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.nullpointer.devs.drivers.presentation.state.MainScreenState
import com.nullpointer.devs.drivers.presentation.state.NavigateRoot
import com.nullpointer.devs.drivers.presentation.state.rememberMainScreenState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LoginComponentDestination
import com.ramcosta.composedestinations.generated.navgraphs.AuthNavGraph
import com.ramcosta.composedestinations.generated.navgraphs.HomeContainerNavGraph
import com.ramcosta.composedestinations.generated.navgraphs.HomeNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.utils.startDestination

@Composable
fun MainComponent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    mainScreenState: MainScreenState = rememberMainScreenState()
) {

    val isUserLoggedIn by mainViewModel.isUserLoggedIn.collectAsState()


    MainComponent(
        modifier = modifier,
        navController = mainScreenState.navHostController,
        navigateRoot = mainScreenState,
        isUserLoggedIn = isUserLoggedIn
    )

}


@Composable
fun MainComponent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateRoot: NavigateRoot,
    isUserLoggedIn: Boolean?
) {

    val startRoute = when {
        isUserLoggedIn == null -> null
        isUserLoggedIn -> HomeContainerNavGraph
        else -> AuthNavGraph
    }

    startRoute?.let { route ->
        DestinationsNavHost(
            startRoute = route,
            navGraph = NavGraphs.root,
            modifier = modifier,
            navController = navController,
            dependenciesContainerBuilder = {
                dependency(navigateRoot)
            }
        )
    }
}