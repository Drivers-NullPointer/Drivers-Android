package com.nullpointer.devs.drivers.data.local.datastore.auth

import com.nullpointer.devs.drivers.data.model.auth.AuthData
import kotlinx.coroutines.flow.Flow

interface AuthDataStore {

    suspend fun saveAuthData(authData: AuthData)

    fun getAuthData(): Flow<AuthData?>

    suspend fun clearAuthData()

}