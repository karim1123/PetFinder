package com.example.android.pets_finder.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.common.Resource
import com.example.android.domain.entities.UserModel
import com.example.android.domain.usecases.registration.RegisterUseCase
import com.example.android.domain.usecases.user.AddUserToDBUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * ViewModel to aid with registering a user in via the [RegistrationFragment].
 */
class RegistrationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val addUserToDBUseCase: AddUserToDBUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _userSignUpStatus: MutableStateFlow<RegistrationStatus> =
        MutableStateFlow(RegistrationStatus.Initialization)
    val userSignUpStatus: StateFlow<RegistrationStatus> = _userSignUpStatus
    private val mutex = Mutex()

    /**
     * Send a register request,
     * with the credentials of [email], [userName], [userPhoneNumber] and [password].
     *
     * The result of this operation will be posted to [userSignUpStatus].
     */
    fun register(
        email: String,
        userName: String,
        userPhoneNumber: String,
        password: String,
        confirmPassword: String
    ) = viewModelScope.launch(dispatcher) {
        when {
            email.isEmpty() -> _userSignUpStatus.value =
                RegistrationStatus.EmptyEmail
            userName.isEmpty() -> _userSignUpStatus.value =
                RegistrationStatus.EmptyUserName
            userPhoneNumber.isEmpty() -> _userSignUpStatus.value =
                RegistrationStatus.EmptyPhoneNumber
            password.isEmpty() -> _userSignUpStatus.value =
                RegistrationStatus.EmptyPassword
            confirmPassword.isEmpty() -> _userSignUpStatus.value =
                RegistrationStatus.EmptyConfirmPassword
            password != confirmPassword -> _userSignUpStatus.value =
                RegistrationStatus.PassAndConfDoNotMatch
            else -> {
                _userSignUpStatus.value = RegistrationStatus.Loading
                executeRegistration(email, userName, userPhoneNumber, password)
            }
        }
    }

    private suspend fun executeRegistration(
        email: String,
        userName: String,
        userPhoneNumber: String,
        password: String
    ) {
        when (val registerResult = mutex.withLock {
            registerUseCase.execute(email, password)
        }
        ) {
            is Resource.Success -> {
                addUserToBd(
                    UserModel(
                        id = registerResult.data.toString(),
                        userName = userName,
                        userPhoneNumber = userPhoneNumber,
                        userEmail = email
                    )
                )
            }
            is Resource.Error -> _userSignUpStatus.value = RegistrationStatus.Error
        }
    }

    private suspend fun addUserToBd(user: UserModel) {
        when (mutex.withLock { addUserToDBUseCase.execute(user) }) {
            is Resource.Success -> _userSignUpStatus.value = RegistrationStatus.Success
            is Resource.Error -> _userSignUpStatus.value = RegistrationStatus.Error
        }
    }
}
