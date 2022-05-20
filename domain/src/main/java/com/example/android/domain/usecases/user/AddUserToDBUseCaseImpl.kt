package com.example.android.domain.usecases.user

import com.example.android.domain.entities.UserModel
import com.example.android.domain.common.Resource
import com.example.android.domain.repositories.UsersRepository

class AddUserToDBUseCaseImpl(private val usersRepository: UsersRepository) : AddUserToDBUseCase {
    override suspend fun execute(user: UserModel): Resource<String> {
        return usersRepository.addUserToDB(user)
    }
}
