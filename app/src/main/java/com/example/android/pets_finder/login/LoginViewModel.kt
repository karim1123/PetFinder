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

/**
 * ViewModel to aid with logging a user in via the [LoginFragment].
 */
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dispatcher: CoroutineDispatcher,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _userSignInStatus: MutableStateFlow<LoginStatus> =
        MutableStateFlow(LoginStatus.IsNotAuthorized)
    val userSignInStatus: StateFlow<LoginStatus> = _userSignInStatus

    init {
        if (!isUserAuthorized()) {
            _userSignInStatus.value = LoginStatus.Success
        }
    }

    /**
     * Send a login request, with the credentials of [email] and [password].
     *
     * The result of this operation will be posted to [userSignInStatus].
     */
    fun login(email: String, password: String) = viewModelScope.launch(dispatcher) {
        when {
            email.isEmpty() -> _userSignInStatus.value = LoginStatus.EmptyEmail
            password.isEmpty() -> _userSignInStatus.value = LoginStatus.EmptyPassword
            else -> {
                _userSignInStatus.value = LoginStatus.Loading
                executeLogin(email, password)
            }
        }
    }

    private suspend fun executeLogin(email: String, pass: String) {
        when (loginUseCase.execute(email, pass)) {
            is Resource.Success -> _userSignInStatus.value = LoginStatus.Success
            is Resource.Error -> _userSignInStatus.value = LoginStatus.Error
        }
    }

    /**
     * Check is user authorized.
     */
    private fun isUserAuthorized() = firebaseAuth.currentUser?.uid.isNullOrEmpty()
}
