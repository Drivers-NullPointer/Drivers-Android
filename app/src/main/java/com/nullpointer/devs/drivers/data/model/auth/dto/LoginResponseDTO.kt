package com.nullpointer.devs.drivers.data.model.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDTO (
    val user: User,
    val token: String,
    val refreshToken: String,
){
    @Serializable
    data class User (
        val id: Long,
        val name: String,
        val email: String,

        @SerialName("roleId")
        val roleID: Long,

        val isEmailVerified: Boolean
    )
}

