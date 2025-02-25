package com.nullpointer.devs.drivers.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nullpointer.devs.drivers.domain.model.UserAuthState
import com.nullpointer.devs.drivers.presentation.state.MainScreenState
import com.nullpointer.devs.drivers.presentation.state.NavigateRoot
import com.nullpointer.devs.drivers.presentation.state.rememberMainScreenState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.navgraphs.AuthNavGraph
import com.ramcosta.composedestinations.generated.navgraphs.EmailNavGraph
import com.ramcosta.composedestinations.generated.navgraphs.HomeContainerNavGraph
import com.ramcosta.composedestinations.navigation.dependency

@Composable
fun MainComponent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    mainScreenState: MainScreenState = rememberMainScreenState()
) {

    val userAuthState by mainViewModel.userAuthState.collectAsStateWithLifecycle()

    MainComponent(
        modifier = modifier,
        navController = mainScreenState.navHostController,
        navigateRoot = mainScreenState,
        userAuthState = userAuthState
    )

}


@Composable
fun MainComponent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateRoot: NavigateRoot,
    userAuthState: UserAuthState
) {

    val startRoute = remember(userAuthState) {
        when (userAuthState) {
            UserAuthState.UNKNOWN -> null
            UserAuthState.AUTHENTICATED -> HomeContainerNavGraph
            UserAuthState.UNAUTHENTICATED -> AuthNavGraph
            UserAuthState.EMAIL_NOT_VERIFIED -> EmailNavGraph
        }
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