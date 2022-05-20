package com.example.android.pets_finder.login.di

import com.example.android.domain.repositories.AuthenticationRepository
import com.example.android.domain.usecases.login.LoginUseCase
import com.example.android.domain.usecases.login.LoginUseCaseImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class LoginModule {
    @LoginScope
    @Provides
    fun provideLoginUseCase(
        authenticationRepository: AuthenticationRepository
    ): LoginUseCase = LoginUseCaseImpl(authenticationRepository)

    @LoginScope
    @Provides
    fun provideLoginDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
