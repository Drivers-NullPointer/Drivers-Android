package com.nullpointer.devs.drivers.di.viewModel

import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import com.nullpointer.devs.drivers.domain.useCase.auth.login.LoginUseCase
import com.nullpointer.devs.drivers.domain.useCase.auth.login.LoginUseCaseImpl
import com.nullpointer.devs.drivers.domain.useCase.auth.register.RegisterUseCase
import com.nullpointer.devs.drivers.domain.useCase.auth.register.RegisterUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(
        authRepository: AuthRepository
    ): LoginUseCase = LoginUseCaseImpl(
        authRepository = authRepository
    )

    @Provides
    @Singleton
    fun provideRegisterUseCase(
        authRepository: AuthRepository
    ): RegisterUseCase = RegisterUseCaseImpl(
        authRepository = authRepository
    )
}