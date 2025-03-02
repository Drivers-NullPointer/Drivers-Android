package com.nullpointer.devs.drivers.di.api

import com.nullpointer.devs.drivers.BuildConfig
import com.nullpointer.devs.drivers.data.local.auth.AuthLocalDataSource
import com.nullpointer.devs.drivers.data.remote.auth.AuthRemoteDataSource
import com.nullpointer.devs.drivers.data.remote.auth.SigningInterceptor
import com.nullpointer.devs.drivers.data.remote.auth.TimberLoggingInterceptor
import com.nullpointer.devs.drivers.data.remote.auth.TokenAuthenticator
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideLoggerInterceptor(): TimberLoggingInterceptor = TimberLoggingInterceptor()

    @Provides
    @Singleton
    fun provideHeaderMobileInterceptor(
        authLocalDataSourceLazy: Lazy<AuthLocalDataSource>
    ): SigningInterceptor =
        SigningInterceptor(authLocalDataSourceLazy)

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        authLocalRepositoryLazy: Lazy<AuthLocalDataSource>,
        authRemoteDataSourceLazy: Lazy<AuthRemoteDataSource>
    ): TokenAuthenticator = TokenAuthenticator(
        authLocalDataSourceLazy = authLocalRepositoryLazy,
        authRemoteDataSourceLazy = authRemoteDataSourceLazy
    )

    @Provides
    @Singleton
    fun provideHttpClient(
        headerMobileInterceptor: SigningInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        loggerInterceptor: TimberLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerMobileInterceptor)
        .authenticator(tokenAuthenticator)
        .addInterceptor(loggerInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.drivers_api)
            .client(httpClient)
            .addConverterFactory(
                Json.asConverterFactory(
                    contentType = MediaType.get("application/json")
                )
            )
            .build()
    }

}