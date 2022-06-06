package com.example.android.domain.usecases.registration

import com.example.android.domain.common.Resource
import com.example.android.domain.repositories.AuthenticationRepository

class RegisterUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository
) : RegisterUseCase {
    override suspend fun execute(email: String, password: String): Resource<String> {
        return authenticationRepository.register(email, password)
    }
}
