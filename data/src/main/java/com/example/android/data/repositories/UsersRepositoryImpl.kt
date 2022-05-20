package com.example.android.data.repositories

import com.example.android.data.utils.RepositoriesNames
import com.example.android.data.utils.safeCall
import com.example.android.domain.entities.UserModel
import com.example.android.domain.common.Resource
import com.example.android.domain.repositories.UsersRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UsersRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val database: FirebaseDatabase
) : UsersRepository {
    override suspend fun addUserToDB(user: UserModel): Resource<String> {
        return withContext(dispatcher) {
            safeCall {
                val databaseReference = database.getReference(RepositoriesNames.Users.name)
                databaseReference.child(user.id).setValue(user)
                Resource.Success(user.id)
            }
        }
    }
}
