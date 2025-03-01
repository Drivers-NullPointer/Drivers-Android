package com.nullpointer.devs.drivers.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthData(
    val id:Long,
    val token:String,
    val refreshToken:String,
    val isEmailVerified: Boolean,
    val email: String,
)
