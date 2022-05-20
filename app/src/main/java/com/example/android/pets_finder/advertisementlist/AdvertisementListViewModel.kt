package com.example.android.pets_finder.advertisementlist

import androidx.lifecycle.ViewModel
import com.example.android.domain.common.LatestAdvertisementListUiState
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.data.utils.RepositoriesNames
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdvertisementListViewModel @Inject constructor(
    private val database: FirebaseDatabase
) : ViewModel() {

    private lateinit var postsValueEventListener: ValueEventListener
    private val _advertisementListStatus: MutableStateFlow<LatestAdvertisementListUiState> =
        MutableStateFlow(LatestAdvertisementListUiState.Success(emptyList()))
    val advertisementListStatus = _advertisementListStatus.asStateFlow()

    private fun listenForPostsValueChanges() {
        postsValueEventListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                _advertisementListStatus.value =
                    LatestAdvertisementListUiState.Error(databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val advertisements: List<AdvertisementModel> =
                        dataSnapshot.children.mapNotNull {
                            it.getAdvertisementModel()
                        }.toList()
                    _advertisementListStatus.value =
                        LatestAdvertisementListUiState.Success(advertisements)
                } else {
                    _advertisementListStatus.value = LatestAdvertisementListUiState.Success(
                        emptyList()
                    )
                }
            }
        }
        database.getReference(RepositoriesNames.Advertisements.name)
            .addValueEventListener(postsValueEventListener)
    }

    fun onPostsValuesChange(): MutableStateFlow<LatestAdvertisementListUiState> {
        _advertisementListStatus.value = LatestAdvertisementListUiState.Loading
        listenForPostsValueChanges()
        return _advertisementListStatus
    }

    fun removePostsValuesChangesListener() {
        database.getReference(RepositoriesNames.Advertisements.name).removeEventListener(postsValueEventListener)
    }

    fun DataSnapshot.getAdvertisementModel(): AdvertisementModel? {
        return this.getValue(AdvertisementModel::class.java)
    }
}
