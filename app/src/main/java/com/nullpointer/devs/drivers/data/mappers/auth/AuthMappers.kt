package com.nullpointer.devs.drivers.data.mappers.auth

import com.nullpointer.devs.drivers.data.model.auth.AuthData
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO
import com.nullpointer.devs.drivers.domain.model.CredentialsData
import com.nullpointer.devs.drivers.domain.model.ForgotPasswordData
import com.nullpointer.devs.drivers.domain.model.RegisterData


fun CredentialsData.toLoginDTO() = LoginDTO(
    email = email,
    password = password
)

fun LoginResponseDTO.toAuthData() = AuthData(
    id = user.id,
    token = token,
    refreshToken = refreshToken,
)

fun RegisterData.toRegisterDTO() = RegisterDTO(
    name = name,
    lastname = lastname,
    email = email,
    password = password,
    birthdate = birthdate
)

fun RegisterResponseDTO.toAuthData() = AuthData(
    id = user.id,
    token = token,
    refreshToken = refreshToken,
)

fun ForgotPasswordData.toForgotPasswordDTO() = ForgotPasswordDTO(
    email = email
)

fun AuthData.toRefreshTokenDTO() = RefreshDTO(
    refreshToken = refreshToken
)
