package com.example.android.pets_finder.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.common.Resource
import com.example.android.domain.usecases.login.LoginUseCase
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dispatcher: CoroutineDispatcher,
    firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _userSignInStatus: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Success(EMPTY_ID))
    val userSignInStatus: StateFlow<Resource<String>> = _userSignInStatus

    init {
        // проверка на авторизацию для того, чтобы сразу "перенести" авторизованного пользователя
        // в фрагмент просмотра объявлений
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            _userSignInStatus.value = Resource.Success(userId)
        }
    }

    // лоигин пользователя
    fun login(email: String, pass: String) {
        // проверка заполнености формы логина
        if (email.isEmpty() || pass.isEmpty()) {
            _userSignInStatus.value = Resource.Error(EMPTY_FIELDS)
        } else {
            _userSignInStatus.value = Resource.Loading()
            viewModelScope.launch(dispatcher) {
                val loginResult = loginUseCase.execute(email, pass)
                _userSignInStatus.value = loginResult
            }
        }
    }

    companion object {
        const val EMPTY_ID = ""
        const val EMPTY_FIELDS = "All fields must be filled"
    }
}
