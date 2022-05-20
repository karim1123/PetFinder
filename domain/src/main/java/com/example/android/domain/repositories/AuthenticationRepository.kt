package com.example.android.domain.repositories

import com.example.android.domain.common.Resource

interface AuthenticationRepository {
    suspend fun login(email: String, password: String): Resource<String>
    suspend fun register(email: String, password: String): Resource<String>
}
