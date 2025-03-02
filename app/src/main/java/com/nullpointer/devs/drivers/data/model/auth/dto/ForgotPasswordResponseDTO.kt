package com.nullpointer.devs.drivers.data.model.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordResponseDTO(
    val message: String
)
