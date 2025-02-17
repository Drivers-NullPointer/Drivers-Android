package com.nullpointer.devs.drivers.di.api

import com.nullpointer.devs.drivers.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.drivers_api)
            .addConverterFactory(
                Json.asConverterFactory(
                    contentType = MediaType.get("application/json")
                )
            )
            .build()
    }

}