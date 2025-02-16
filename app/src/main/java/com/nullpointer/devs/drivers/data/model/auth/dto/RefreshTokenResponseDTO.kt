package com.nullpointer.devs.drivers.data.model.auth.dto

data class RefreshTokenResponseDTO(
    val token: String,
    val refreshToken: String
)