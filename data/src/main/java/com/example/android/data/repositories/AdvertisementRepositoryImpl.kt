package com.example.android.data.repositories

import android.net.Uri
import com.example.android.data.utils.RepositoriesNames
import com.example.android.data.utils.advertisementDetailsSafeCall
import com.example.android.data.utils.createAdvertisementSafeCall
import com.example.android.domain.common.AdvertisementDetailsUiState
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
        advertisementModel: AdvertisementModel
    ): CreateAdvertisementUiState {
        return withContext(dispatcher) {
            createAdvertisementSafeCall {
                val key = databaseReference.push().key.toString()
                advertisementModel.advertisementId = key
                val storageReference =
                    storage.getReference("/${RepositoriesNames.images.name}/$key")
                // загрузка картинок в firebase storage и получение их url
                for (index in advertisementModel.urisList.indices) {
                    async(dispatcher) {
                        storageReference.child(index.toString())
                            .putFile(Uri.parse(advertisementModel.urisList[index]))
                            .addOnCompleteListener {
                            }.await()
                    }
                }
                CreateAdvertisementUiState.Success(
                    advertisement = advertisementModel
                )
            }
        }
    }

    override suspend fun getImagesUris(
        advertisement: AdvertisementModel
    ): CreateAdvertisementUiState =
        withContext(dispatcher) {
            createAdvertisementSafeCall {
                val storageReference = storage.getReference(
                    "/${RepositoriesNames.images.name}/${advertisement.advertisementId}"
                )
                val size = advertisement.urisList.size - 1
                for (i in 0..size) {
                    val imageReference = storageReference.child("$i")
                    async {
                        imageReference.downloadUrl.addOnSuccessListener { uri ->
                            advertisement.urisList.removeAt(i)
                            advertisement.urisList.add(i, uri.toString())
                        }.await()
                    }
                }
                CreateAdvertisementUiState.Success(advertisement = advertisement, closeFlag = true)
            }
        }

    override suspend fun deleteAdvertisement(advertisementId: String): AdvertisementDetailsUiState {
        return withContext(dispatcher) {
            advertisementDetailsSafeCall {
                databaseReference.child(advertisementId).removeValue()
                val storageRef = storage.getReference(
                    "/${RepositoriesNames.images.name}"
                ).child(advertisementId)
                storageRef.listAll().addOnSuccessListener {
                    it.items.forEach { image ->
                        image.delete()
                    }
                }
                AdvertisementDetailsUiState.Success(deleteFlag = true)
            }
        }
    }
}
