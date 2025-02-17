package com.nullpointer.devs.drivers.di.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nullpointer.devs.drivers.data.local.auth.AuthLocalDataSource
import com.nullpointer.devs.drivers.data.local.auth.AuthLocalDataSourceImpl
import com.nullpointer.devs.drivers.data.local.datastore.auth.AuthDataStore
import com.nullpointer.devs.drivers.data.local.datastore.auth.AuthDataStoreImpl
import com.nullpointer.devs.drivers.data.remote.auth.AuthApiServices
import com.nullpointer.devs.drivers.data.remote.auth.AuthRemoteDataSource
import com.nullpointer.devs.drivers.data.remote.auth.AuthRemoteDataSourceImpl
import com.nullpointer.devs.drivers.data.repository.AuthRepoImpl
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthDataStore(
        dataStore: DataStore<Preferences>
    ): AuthDataStore = AuthDataStoreImpl(
        dataStore = dataStore
    )

    @Provides
    @Singleton
    fun provideAuthApiServices(
        retrofit: Retrofit
    ): AuthApiServices = retrofit.create(AuthApiServices::class.java)

    @Provides
    @Singleton
    fun provideAuthLocalDataSource(
        authDataStore: AuthDataStore
    ): AuthLocalDataSource = AuthLocalDataSourceImpl(
        authDataStore = authDataStore
    )

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
        authApiServices: AuthApiServices
    ): AuthRemoteDataSource = AuthRemoteDataSourceImpl(
        authApiServices =  authApiServices
    )

    @Provides
    @Singleton
    fun provideAuthRepository(
        authLocalDataSource: AuthLocalDataSource,
        authRemoteDataSource: AuthRemoteDataSource
    ): AuthRepository = AuthRepoImpl(
        authLocalDataSource = authLocalDataSource,
        authRemoteDataSource = authRemoteDataSource
    )
}