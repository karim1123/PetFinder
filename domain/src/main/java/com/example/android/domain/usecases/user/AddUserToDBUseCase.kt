package com.example.android.domain.usecases.user

import com.example.android.domain.entities.UserModel
import com.example.android.domain.common.Resource

interface AddUserToDBUseCase {
    suspend fun execute(user: UserModel): Resource<String>
}
