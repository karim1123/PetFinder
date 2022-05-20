package com.example.android.domain.usecases.login

import com.example.android.domain.common.Resource
import com.example.android.domain.repositories.AuthenticationRepository

class LoginUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository
) : LoginUseCase {
    override suspend fun execute(email: String, pass: String): Resource<String> {
        return authenticationRepository.login(email, pass)
    }
}
