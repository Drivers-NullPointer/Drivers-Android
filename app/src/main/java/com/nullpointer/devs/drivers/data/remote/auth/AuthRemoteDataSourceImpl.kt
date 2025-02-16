package com.nullpointer.devs.drivers.data.remote.auth

import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshTokenResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO

class AuthRemoteDataSourceImpl(
    private val authApiServices: AuthApiServices
):AuthRemoteDataSource {

    override suspend fun login(loginDTO: LoginDTO): LoginResponseDTO =
        authApiServices.login(loginDTO)

    override suspend fun register(registerResponseDTO: RegisterResponseDTO): RegisterResponseDTO =
        authApiServices.register(registerResponseDTO)

    override suspend fun refresh(refreshDTO: RefreshDTO): RefreshTokenResponseDTO =
        authApiServices.refresh(refreshDTO)

    override suspend fun forgotPassword(forgotPasswordDTO: ForgotPasswordDTO): ForgotPasswordResponseDTO =
        authApiServices.forgotPassword(forgotPasswordDTO)
}