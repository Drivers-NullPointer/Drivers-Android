package com.nullpointer.devs.drivers.presentation.state

import com.ramcosta.composedestinations.spec.DestinationSpec

interface NavigateRoot {

    fun navigate(destination:DestinationSpec)

    fun navigateBack()
}