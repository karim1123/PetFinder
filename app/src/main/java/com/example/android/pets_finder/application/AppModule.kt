package com.example.android.pets_finder.application

import android.content.Context
import com.example.android.data.repositories.AdvertisementRepositoryImpl
import com.example.android.data.repositories.AuthenticationRepositoryImpl
import com.example.android.domain.repositories.AdvertisementRepository
import com.example.android.domain.repositories.AuthenticationRepository
import com.example.android.data.repositories.UsersRepositoryImpl
import com.example.android.domain.repositories.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    @Singleton
    @Provides
    fun providesContext() = context

    @Singleton
    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        val database = Firebase.database
        database.setPersistenceEnabled(true)
        return database
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideAuthenticationRepository(
        firebaseAuth: FirebaseAuth
    ): AuthenticationRepository = AuthenticationRepositoryImpl(firebaseAuth, Dispatchers.IO)

    @Singleton
    @Provides
    fun provideUsersRepository(database: FirebaseDatabase): UsersRepository =
        UsersRepositoryImpl(Dispatchers.IO, database)

    @Singleton
    @Provides
    fun provideAdvertisementRepository(
        database: FirebaseDatabase,
        storage: FirebaseStorage
    ): AdvertisementRepository =
        AdvertisementRepositoryImpl(
            database = database,
            storage = storage,
            dispatcher = Dispatchers.IO
        )
}
