package com.example.android.pets_finder.createadvertisement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.common.CreateAdvertisementUiState
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.domain.usecases.advertisement.addadvertisementtodb.AddAdvertisementToBDUseCase
import com.example.android.domain.usecases.advertisement.addimagestostorage.AddAdvertisementImagesToStorageUseCase
import com.example.android.domain.usecases.advertisement.getimagesuris.GetImagesUrisUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateAdvertisementViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    firebaseAuth: FirebaseAuth,
    private val addAdvertisementToBDUseCase: AddAdvertisementToBDUseCase,
    private val addAdvertisementImagesToStorageUseCase: AddAdvertisementImagesToStorageUseCase,
    private val getImagesUrisUseCase: GetImagesUrisUseCase
) : ViewModel() {
    private val _createAdvertisementState: MutableStateFlow<CreateAdvertisementUiState> =
        MutableStateFlow(CreateAdvertisementUiState.Success())
    val advertisementListStatus = _createAdvertisementState.asStateFlow()
    val advertisement = MutableStateFlow(AdvertisementModel())
    private var userId: String? = EMPTY_STRING

    init {
        userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            _createAdvertisementState.value = CreateAdvertisementUiState.Error(AUTHORIZATION_ERROR)
        } else advertisement.value.userId = userId!!
    }

    fun createAdvertisement(address: String, description: String) {
        // проверка заполнености формы создания объявления
        if (advertisement.value.petStatus.isBlank() ||
            advertisement.value.petType.isBlank() ||
            address.isBlank() ||
            advertisement.value.urisList.isEmpty() ||
            description.isBlank() ||
            advertisement.value.userId.isEmpty()
        ) {
            _createAdvertisementState.value = CreateAdvertisementUiState.Error(EMPTY_FIELDS)
        } else {
            advertisement.value.description = description
            advertisement.value.address = address
            addAdvertisementImagesToBD(advertisement.value)
        }
    }

    fun addAdvertisementToBd(advertisementModel: AdvertisementModel) {
        viewModelScope.launch(dispatcher) {
            val addAdvertisementToBDResult = addAdvertisementToBDUseCase.execute(advertisementModel)
            _createAdvertisementState.value = addAdvertisementToBDResult
        }
    }

    fun getImagesUris(advertisementModel: AdvertisementModel) {
        viewModelScope.launch(dispatcher) {
            launch {
                val getImagesUrisResult =
                    getImagesUrisUseCase.execute(
                        advertisementModel
                    )
                _createAdvertisementState.value = getImagesUrisResult
            }.join()
        }
    }

    private fun addAdvertisementImagesToBD(
        advertisementModel: AdvertisementModel
    ) {
        _createAdvertisementState.value = CreateAdvertisementUiState.Loading
        viewModelScope.launch(dispatcher) {
            val addAdvertisementImagesResult = addAdvertisementImagesToStorageUseCase.execute(
                advertisementModel
            )
            _createAdvertisementState.value = addAdvertisementImagesResult
        }
    }

    companion object {
        const val EMPTY_FIELDS = "All fields must be filled"
        const val EMPTY_STRING = ""
        const val AUTHORIZATION_ERROR = "User is not authorized"
    }
}
