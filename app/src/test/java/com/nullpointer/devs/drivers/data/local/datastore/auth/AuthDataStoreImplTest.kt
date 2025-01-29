package com.nullpointer.devs.drivers.data.local.datastore.auth

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.nullpointer.devs.drivers.data.model.auth.AuthData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class AuthDataStoreImplTest {

    @get:Rule
    val temporaryFolder: TemporaryFolder = TemporaryFolder.builder()
        .assureDeletion()
        .build()


    @Test
    fun `saveAuthData should save authData correctly in DataStore`() = runTest {
        val authData = AuthData("token", "refreshToken")
        val autoDataSerializedExpected = Json.encodeToString(authData)

        // Mock DataStore
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { temporaryFolder.newFile("test_store.preferences_pb") },
        )

        // * create an instance of AuthDataStoreImpl
        val authDataStore = AuthDataStoreImpl(dataStore)

        // * save authData
        authDataStore.saveAuthData(authData)

        // * verify that the data was saved correctly
        val preferences = dataStore.data.first()
        val authDataSerialized = preferences[authDataStore.keyAuthData]

        // * verify that the data was saved correctly
        assertEquals(autoDataSerializedExpected, authDataSerialized)
    }

    @Test
    fun `getAuthData should return authData from DataStore when it exists`() = runTest {
        val authData = AuthData("token", "refreshToken")

        // Mock DataStore
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { temporaryFolder.newFile("test_store.preferences_pb") },
        )

        // * create an instance of AuthDataStoreImpl
        val authDataStore = AuthDataStoreImpl(dataStore)

        // * save authData
        authDataStore.saveAuthData(authData)

        // * get authData
        val authDataResult = authDataStore.getAuthData().first()

        // * verify that the data was retrieved correctly
        assertEquals(authData, authDataResult)
    }

    @Test
    fun `getAuthData should return null from DataStore when it does not exist`() = runTest {
        // Mock DataStore
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { temporaryFolder.newFile("test_store.preferences_pb") },
        )

        // * create an instance of AuthDataStoreImpl
        val authDataStore = AuthDataStoreImpl(dataStore)

        // * get authData
        val authDataResult = authDataStore.getAuthData().first()

        // * verify that the data was retrieved correctly
        assertNull(authDataResult)
    }

    @Test
    fun `clearAuthData should remove authData from DataStore`() = runTest {
        val authData = AuthData("token", "refreshToken")

        // Mock DataStore
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { temporaryFolder.newFile("test_store.preferences_pb") },
        )

        // * create an instance of AuthDataStoreImpl
        val authDataStore = AuthDataStoreImpl(dataStore)

        // * save authData
        authDataStore.saveAuthData(authData)

        // * clear authData
        authDataStore.clearAuthData()

        // * verify that the data was removed
        val preferences = dataStore.data.first()
        val authDataSerialized = preferences[authDataStore.keyAuthData]

        // * verify that the data was removed
        assertNull(authDataSerialized)
    }
}