package com.nullpointer.devs.drivers.data.model.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDTO(
    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val birthdate: String,
)
