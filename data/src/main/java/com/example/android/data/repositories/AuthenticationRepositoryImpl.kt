package com.example.android.data.repositories

import com.example.android.data.utils.safeCall
import com.example.android.domain.common.Resource
import com.example.android.domain.repositories.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher
) : AuthenticationRepository {
    override suspend fun login(email: String, password: String): Resource<String> {
        return withContext(dispatcher) {
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result.user?.uid.toString())
            }
        }
    }

    override suspend fun register(email: String, password: String): Resource<String> {
        return withContext(dispatcher) {
            safeCall {
                val registrationResult =
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = registrationResult.user?.uid!!
                Resource.Success(userId)
            }
        }
    }
}
