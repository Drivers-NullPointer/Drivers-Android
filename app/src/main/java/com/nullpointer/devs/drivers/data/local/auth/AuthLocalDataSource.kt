package com.nullpointer.devs.drivers.data.local.auth

import com.nullpointer.devs.drivers.data.model.auth.AuthData
import kotlinx.coroutines.flow.Flow

/**
 * Interface that defines the operations for managing authentication data locally.
 * This is typically used for saving, retrieving, and clearing authentication data from local storage.
 */
interface AuthLocalDataSource {

    /**
     * Retrieves the authentication data as a flow.
     * Returns null if no authentication data is found in local storage.
     *
     * @return A [Flow] of [AuthData], which emits the authentication data or null if not available.
     */
    fun getAuthData(): Flow<AuthData?>

    /**
     * Saves the provided authentication data to local storage.
     *
     * @param authData The authentication data to be saved.
     */
    suspend fun saveAuthData(authData: AuthData)

    /**
     * Clears the stored authentication data from local storage.
     */
    suspend fun clearAuthData()
}
