package com.example.android.pets_finder.registration.di

import com.example.android.domain.repositories.AuthenticationRepository
import com.example.android.domain.repositories.UsersRepository
import com.example.android.domain.usecases.registration.RegisterUserUseCase
import com.example.android.domain.usecases.registration.RegisterUserUseCaseImpl
import com.example.android.domain.usecases.user.AddUserToDBUseCase
import com.example.android.domain.usecases.user.AddUserToDBUseCaseImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class RegistrationModule {
    @RegistrationScope
    @Provides
    fun provideRegisterUserUseCase(
        authenticationRepository: AuthenticationRepository
    ): RegisterUserUseCase = RegisterUserUseCaseImpl(authenticationRepository)

    @RegistrationScope
    @Provides
    fun provideAddUserToBDUseCase(
        usersRepository: UsersRepository
    ): AddUserToDBUseCase = AddUserToDBUseCaseImpl(usersRepository)

    @RegistrationScope
    @Provides
    fun provideRegistrationDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
