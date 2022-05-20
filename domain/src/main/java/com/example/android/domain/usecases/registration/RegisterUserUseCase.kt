package com.example.android.domain.usecases.registration

import com.example.android.domain.common.Resource

interface RegisterUserUseCase {
    suspend fun execute(email: String, password: String): Resource<String>
}
