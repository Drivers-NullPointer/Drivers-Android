package com.nullpointer.devs.drivers.data.remote.auth

import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.ForgotPasswordResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RefreshTokenResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO

interface AuthRemoteDataSource {

    suspend fun login(loginDTO: LoginDTO): LoginResponseDTO

    suspend fun register(registerDTO: RegisterDTO): RegisterResponseDTO

    suspend fun refresh(refreshDTO: RefreshDTO): RefreshTokenResponseDTO

    suspend fun forgotPassword(forgotPasswordDTO: ForgotPasswordDTO): ForgotPasswordResponseDTO

}