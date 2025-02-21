package com.nullpointer.devs.drivers.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.navgraphs.RootNavGraph

@Composable
fun MainComponent(
    modifier: Modifier = Modifier,
) {

    DestinationsNavHost(
        navGraph = NavGraphs.root,
        modifier = modifier
    )

}