package com.example.android.domain.repositories

import com.example.android.domain.common.Resource
import com.example.android.domain.entities.UserModel

interface UsersRepository {
    suspend fun addUserToDB(user: UserModel): Resource<String>
}
