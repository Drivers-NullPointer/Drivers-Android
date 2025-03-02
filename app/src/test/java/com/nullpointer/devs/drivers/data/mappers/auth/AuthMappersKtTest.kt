package com.nullpointer.devs.drivers.data.mappers.auth

import com.nullpointer.devs.drivers.data.model.auth.AuthData
import com.nullpointer.devs.drivers.data.model.auth.dto.LoginResponseDTO
import com.nullpointer.devs.drivers.data.model.auth.dto.RegisterResponseDTO
import com.nullpointer.devs.drivers.domain.model.CredentialsData
import com.nullpointer.devs.drivers.domain.model.ForgotPasswordData
import com.nullpointer.devs.drivers.domain.model.RegisterData
import org.junit.Assert.assertEquals
import org.junit.Test

class DataExtensionsTest {

    // Test para la extensión toLoginDTO
    @Test
    fun `test toLoginDTO converts CredentialsData to LoginDTO correctly`() {
        val credentialsData = CredentialsData("user@example.com", "password123")

        val loginDTO = credentialsData.toLoginDTO()

        assertEquals("user@example.com", loginDTO.email)
        assertEquals("password123", loginDTO.password)
    }

    // Test para la extensión toAuthData (LoginResponseDTO)
    @Test
    fun `test toAuthData converts LoginResponseDTO to AuthData correctly`() {
        val loginResponseDTO = LoginResponseDTO(
            user = LoginResponseDTO.User(
                email = "examole@xcorre.com",
                id = 1,
                roleID = 1,
                isEmailVerified = true,
                name = "John",
            ),
            token = "token123",
            refreshToken = "refreshToken123"
        )

        val authData = loginResponseDTO.toAuthData()

        assertEquals(1, authData.id)
        assertEquals(loginResponseDTO.user.email, authData.email)
        assertEquals("token123", authData.token)
        assertEquals("refreshToken123", authData.refreshToken)
        assertEquals(true, authData.isEmailVerified)
    }

    // Test para la extensión toRegisterDTO
    @Test
    fun `test toRegisterDTO converts RegisterData to RegisterDTO correctly`() {
        val registerData =
            RegisterData("John", "Doe", "john.doe@example.com", "password123", "1990-01-01")

        val registerDTO = registerData.toRegisterDTO()

        assertEquals("John", registerDTO.name)
        assertEquals("Doe", registerDTO.lastname)
        assertEquals("john.doe@example.com", registerDTO.email)
        assertEquals("password123", registerDTO.password)
        assertEquals("1990-01-01", registerDTO.birthdate)
    }

    // Test para la extensión toAuthData (RegisterResponseDTO)
    @Test
    fun `test toAuthData converts RegisterResponseDTO to AuthData correctly`() {
        val registerResponseDTO = RegisterResponseDTO(
            user = RegisterResponseDTO.User(
                email = "example@correo.com",
                name = "John",
                id = 2,
                roleID = 1,
                isEmailVerified = true
            ),
            token = "token123",
            refreshToken = "refreshToken123"
        )

        val authData = registerResponseDTO.toAuthData()

        assertEquals(2, authData.id)
        assertEquals(registerResponseDTO.user.email, authData.email)
        assertEquals("token123", authData.token)
        assertEquals("refreshToken123", authData.refreshToken)
        assertEquals(true, authData.isEmailVerified)
    }

    // Test para la extensión toForgotPasswordDTO
    @Test
    fun `test toForgotPasswordDTO converts ForgotPasswordData to ForgotPasswordDTO correctly`() {
        val forgotPasswordData = ForgotPasswordData("john.doe@example.com")

        val forgotPasswordDTO = forgotPasswordData.toForgotPasswordDTO()

        assertEquals("john.doe@example.com", forgotPasswordDTO.email)
    }

    // Test para la extensión toRefreshTokenDTO
    @Test
    fun `test toRefreshTokenDTO converts AuthData to RefreshDTO correctly`() {
        val authData = AuthData(1, "token123", "refreshToken123", true, "john.doe@example.com")

        val refreshDTO = authData.toRefreshTokenDTO()

        assertEquals("refreshToken123", refreshDTO.refreshToken)
    }
}
