package com.example.android.domain.usecases.login

import com.example.android.domain.common.Resource

interface LoginUseCase {
    suspend fun execute(email: String, pass: String): Resource<String>
}
