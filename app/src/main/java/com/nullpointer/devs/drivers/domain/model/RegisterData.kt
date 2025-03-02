package com.nullpointer.devs.drivers.domain.model

data class RegisterData(
    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val birthdate: String,
)