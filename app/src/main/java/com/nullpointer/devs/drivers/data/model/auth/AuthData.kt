package com.nullpointer.devs.drivers.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthData(
    val token:String,
    val refreshToken:String
)
