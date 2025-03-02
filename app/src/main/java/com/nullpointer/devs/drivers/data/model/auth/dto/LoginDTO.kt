package com.nullpointer.devs.drivers.data.model.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(
    val email: String,
    val password: String
)
