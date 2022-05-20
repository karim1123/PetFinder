package com.example.android.data.repositories

import android.net.Uri
import com.example.android.data.utils.RepositoriesNames
import com.example.android.data.utils.createAdvertisementSafeCall
import com.example.android.domain.common.CreateAdvertisementUiState
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.domain.repositories.AdvertisementRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AdvertisementRepositoryImpl(
    database: FirebaseDatabase,
    private val storage: FirebaseStorage,
    private val dispatcher: CoroutineDispatcher
) : AdvertisementRepository {
    private val databaseReference = database.getReference(RepositoriesNames.Advertisements.name)

    override suspend fun addAdvertisementToBD(
        advertisementModel: AdvertisementModel
    ): CreateAdvertisementUiState {
        return withContext(dispatcher) {
            createAdvertisementSafeCall {
                databaseReference.child(advertisementModel.advertisementId)
                    .setValue(advertisementModel)
                CreateAdvertisementUiState.Success(closeFlag = true)
            }
        }
    }

    override suspend fun addAdvertisementImagesToStorage(
        advertisementModel: AdvertisementModel,
        imagesUris: MutableList<String>
    ): CreateAdvertisementUiState {
        return withContext(dispatcher) {
            createAdvertisementSafeCall {
                val key = databaseReference.push().key.toString()
                val storageReference =
                    storage.getReference("/${RepositoriesNames.images.name}/$key")
                advertisementModel.advertisementId = key
                // загрузка картинок в firebase storage и получение их url
                for (index in imagesUris.indices) {
                    async(dispatcher) {
                        storageReference.child(index.toString())
                            .putFile(Uri.parse(imagesUris[index]))
                            .addOnCompleteListener {
                                it.result.storage.downloadUrl.addOnSuccessListener { uri ->
                                    advertisementModel.urisList.add(uri.toString())
                                }
                            }.await()
                    }
                }
                CreateAdvertisementUiState.Success(
                    advertisement = advertisementModel
                )
            }
        }
    }
}
