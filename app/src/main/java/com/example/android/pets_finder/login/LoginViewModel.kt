package com.example.android.pets_finder.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.common.Resource
import com.example.android.domain.usecases.login.LoginUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dispatcher: CoroutineDispatcher,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _userSignInStatus: MutableStateFlow<LoginStatus> =
        MutableStateFlow(LoginStatus.IsNotAuthorized)
    val userSignInStatus: StateFlow<LoginStatus> = _userSignInStatus
    private val userLoggedIn: Boolean
        get() = firebaseAuth.currentUser?.uid.isNullOrEmpty()

    init {
        // проверка на авторизацию для того, чтобы сразу "перенести" авторизованного пользователя
        // в фрагмент просмотра объявлений
        if (!userLoggedIn) {
            _userSignInStatus.value = LoginStatus.Success
        }
    }

    // лоигин пользователя
    fun login(email: String, pass: String) = viewModelScope.launch(dispatcher) {
        when {
            email.isEmpty() -> _userSignInStatus.value = LoginStatus.EmptyEmail
            pass.isEmpty() -> _userSignInStatus.value = LoginStatus.EmptyPassword
            else -> {
                _userSignInStatus.value = LoginStatus.Loading
                executeLogin(email, pass)
            }
        }
    }

    private suspend fun executeLogin(email: String, pass: String) {
        when (loginUseCase.execute(email, pass)) {
            is Resource.Success -> _userSignInStatus.value = LoginStatus.Success
            is Resource.Error -> _userSignInStatus.value = LoginStatus.Error
        }
    }

    companion object {
        const val EMPTY_ID = ""
    }
}
