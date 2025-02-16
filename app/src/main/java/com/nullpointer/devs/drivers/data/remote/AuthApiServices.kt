package com.nullpointer.devs.drivers.data.remote

import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshTokenResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO
import retrofit2.http.POST


interface AuthApiServices {

    @POST("login")
    suspend fun login(loginDTO: LoginDTO): LoginResponseDTO


    @POST("register")
    suspend fun register(registerResponseDTO: RegisterResponseDTO):RegisterResponseDTO


    @POST("refresh")
    suspend fun refresh(refreshDTO: RefreshDTO): RefreshTokenResponseDTO

    @POST("forgot-password")
    suspend fun forgotPassword(forgotPasswordDTO: ForgotPasswordDTO): ForgotPasswordResponseDTO
}