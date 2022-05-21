package com.example.android.pets_finder.advertisementdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.utils.RepositoriesNames
import com.example.android.domain.common.AdvertisementDetailsUiState
import com.example.android.domain.entities.UserModel
import com.example.android.domain.usecases.advertisement.deleteadvertisement.DeleteAdvertisementUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdvertisementDetailsViewModel @Inject constructor(
    private val database: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher,
    private val deleteAdvertisementUseCase: DeleteAdvertisementUseCase
) : ViewModel() {
    private lateinit var postsValueEventListener: ValueEventListener
    private val _advertisementDetailsStatus: MutableStateFlow<AdvertisementDetailsUiState> =
        MutableStateFlow(AdvertisementDetailsUiState.Success())
    val advertisementDetailsStatus = _advertisementDetailsStatus.asStateFlow()

    private fun listenForPostsValueChanges(userId: String) {
        postsValueEventListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                _advertisementDetailsStatus.value =
                    AdvertisementDetailsUiState.Error(databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user: UserModel? = dataSnapshot.getValue(UserModel::class.java)
                    _advertisementDetailsStatus.value =
                        AdvertisementDetailsUiState.Success(user = user)
                }
            }
        }
        database.getReference(RepositoriesNames.Users.name).child(userId)
            .addValueEventListener(postsValueEventListener)
    }

    fun onPostsValuesChange(userId: String) {
        _advertisementDetailsStatus.value = AdvertisementDetailsUiState.Loading
        listenForPostsValueChanges(userId)
    }

    fun removePostsValuesChangesListener() {
        database.getReference(RepositoriesNames.Users.name)
            .removeEventListener(postsValueEventListener)
    }

    fun isUserAdvertisementAuthor(): Boolean {
        val userId = firebaseAuth.currentUser?.uid
        return userId != null
    }

    fun deleteAdvertisement(advertisementId: String) {
        viewModelScope.launch(dispatcher) {
            val deleteResult = deleteAdvertisementUseCase.execute(advertisementId)
            _advertisementDetailsStatus.value = deleteResult
        }
    }
}
