package com.nullpointer.devs.drivers.data.local.datastore.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nullpointer.devs.drivers.data.model.auth.AuthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

/**
 * Implementation of [AuthDataStore] that uses Jetpack DataStore to securely store authentication data.
 *
 * @property dataStore An instance of [DataStore] used to store user preferences.
 */
class AuthDataStoreImpl(
    private val dataStore: DataStore<Preferences>
) : AuthDataStore {

    companion object {
        /** Key used to store authentication data in DataStore. */
        private const val KEY_AUTH_DATA = "KEY_AUTH_DATA"
    }

    /** Preference key for storing authentication data in serialized format. */
    val keyAuthData = stringPreferencesKey(KEY_AUTH_DATA)

    /**
     * Saves authentication data in DataStore.
     *
     * @param authData An instance of [AuthData] containing the user's authentication information.
     */
    override suspend fun saveAuthData(authData: AuthData) {
        dataStore.edit { pref ->
            val authDataSerialized = Json.encodeToString(authData)
            pref[keyAuthData] = authDataSerialized
        }
    }

    /**
     * Retrieves the stored authentication data from DataStore.
     *
     * @return A [Flow] that emits an [AuthData] object if it exists, or `null` if no data is stored.
     */
    override fun getAuthData(): Flow<AuthData?> = dataStore.data.map {
        val authDataSerialized = it[keyAuthData] ?: return@map null
        Json.decodeFromString(authDataSerialized)
    }

    /**
     * Clears the stored authentication data from DataStore.
     */
    override suspend fun clearAuthData() {
        dataStore.edit {
            it.remove(keyAuthData)
        }
    }
}
