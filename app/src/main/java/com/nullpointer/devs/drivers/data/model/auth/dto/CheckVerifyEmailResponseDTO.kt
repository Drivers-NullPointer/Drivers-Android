package com.nullpointer.devs.drivers.data.model.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckVerifyEmailResponseDTO(
    val isVerified: Boolean,
    val message: String
)
