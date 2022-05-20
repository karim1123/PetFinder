package com.example.android.pets_finder.createadvertisement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.utils.RepositoriesNames
import com.example.android.domain.common.CreateAdvertisementUiState
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.domain.entities.UserModel
import com.example.android.domain.usecases.advertisement.AddAdvertisementImagesToStorageUseCase
import com.example.android.domain.usecases.advertisement.AddAdvertisementToBDUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateAdvertisementViewModel @Inject constructor(
    private val database: FirebaseDatabase,
    private val dispatcher: CoroutineDispatcher,
    firebaseAuth: FirebaseAuth,
    private val addAdvertisementToBDUseCase: AddAdvertisementToBDUseCase,
    private val addAdvertisementImagesToStorageUseCase: AddAdvertisementImagesToStorageUseCase
) : ViewModel() {
    private lateinit var postsValueEventListener: ValueEventListener
    private val _createAdvertisementState: MutableStateFlow<CreateAdvertisementUiState> =
        MutableStateFlow(CreateAdvertisementUiState.Success())
    val advertisementListStatus = _createAdvertisementState.asStateFlow()
    var advertisement: AdvertisementModel? = null
    val imagesUris = mutableListOf<String>()
    var petType = EMPTY_STRING
    var petStatus = EMPTY_STRING

    var userId: String? = EMPTY_STRING

    init {
        userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            _createAdvertisementState.value = CreateAdvertisementUiState.Error(AUTHORIZATION_ERROR)
        }
    }

    private fun listenForPostsValueChanges() {
        postsValueEventListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                _createAdvertisementState.value =
                    CreateAdvertisementUiState.Error(databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user: UserModel? = dataSnapshot.getValue(UserModel::class.java)
                    _createAdvertisementState.value =
                        CreateAdvertisementUiState.Success(user = user)
                }
            }
        }
        userId?.let {
            database.getReference(RepositoriesNames.Users.name).child(it)
                .addValueEventListener(postsValueEventListener)
        }
    }

    fun onPostsValuesChange() {
        if (userId != null) {
            _createAdvertisementState.value = CreateAdvertisementUiState.Loading
        }
        listenForPostsValueChanges()
    }

    fun removePostsValuesChangesListener() {
        database.getReference(RepositoriesNames.Users.name)
            .removeEventListener(postsValueEventListener)
    }

    fun createAdvertisement(address: String) {
        // проверка заполнености формы создания объявления
        if (petStatus.isBlank() || petType.isBlank() || address.isBlank() || imagesUris.isEmpty()) {
            _createAdvertisementState.value = CreateAdvertisementUiState.Error(EMPTY_FIELDS)
        } else {
            advertisement?.let {
                it.petStatus = petStatus
                it.petType = petType
                it.address = address
            }
            addAdvertisementImagesToBD()
        }
    }

    fun addAdvertisementToBd(advertisementModel: AdvertisementModel) {
        viewModelScope.launch(dispatcher) {
            val addAdvertisementToBDResult = addAdvertisementToBDUseCase.execute(advertisementModel)
            _createAdvertisementState.value = addAdvertisementToBDResult
        }
    }

    private fun addAdvertisementImagesToBD() {
        _createAdvertisementState.value = CreateAdvertisementUiState.Loading
        viewModelScope.launch(dispatcher) {
            val addAdvertisementImagesResult = advertisement?.let {
                addAdvertisementImagesToStorageUseCase.execute(
                    advertisement!!, imagesUris
                )
            }
            _createAdvertisementState.value = addAdvertisementImagesResult!!
        }
    }

    companion object {
        const val EMPTY_FIELDS = "All fields must be filled"
        const val EMPTY_STRING = ""
        const val AUTHORIZATION_ERROR = "User is not authorized"
    }
}
