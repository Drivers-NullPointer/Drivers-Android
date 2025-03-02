package com.nullpointer.devs.drivers.data.local.auth

import com.nullpointer.devs.drivers.data.local.datastore.auth.AuthDataStore
import com.nullpointer.devs.drivers.data.model.auth.AuthData
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [AuthLocalDataSource] that interacts with a local data store to manage authentication data.
 * This class delegates the actual data operations to the provided [AuthDataStore] instance.
 *
 * @param authDataStore The data store used for managing authentication data.
 */
class AuthLocalDataSourceImpl(
    private val authDataStore: AuthDataStore
) : AuthLocalDataSource {

    /**
     * Retrieves the authentication data from the local data store.
     *
     * @return A [Flow] of [AuthData], which emits the authentication data or null if not available.
     */
    override fun getAuthData(): Flow<AuthData?> =
        authDataStore.getAuthData()

    /**
     * Saves the provided authentication data to the local data store.
     *
     * @param authData The authentication data to be saved.
     */
    override suspend fun saveAuthData(authData: AuthData) =
        authDataStore.saveAuthData(authData)

    /**
     * Clears the stored authentication data from the local data store.
     */
    override suspend fun clearAuthData() =
        authDataStore.clearAuthData()
}
