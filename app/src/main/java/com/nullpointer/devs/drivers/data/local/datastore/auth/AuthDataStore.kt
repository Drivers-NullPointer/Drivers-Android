package com.nullpointer.devs.drivers.data.local.datastore.auth

import com.nullpointer.devs.drivers.data.model.auth.AuthData
import kotlinx.coroutines.flow.Flow

/**
 * Interface that defines the operations for managing authentication data.
 * This is typically used for saving, retrieving, and clearing authentication information.
 */
interface AuthDataStore {

    /**
     * Saves the provided authentication data.
     *
     * @param authData The authentication data to be saved.
     */
    suspend fun saveAuthData(authData: AuthData)

    /**
     * Retrieves the authentication data as a flow.
     * Returns null if no authentication data is available.
     *
     * @return A [Flow] of [AuthData], which can emit the authentication data or null if none is found.
     */
    fun getAuthData(): Flow<AuthData?>

    /**
     * Clears the stored authentication data.
     */
    suspend fun clearAuthData()
}
