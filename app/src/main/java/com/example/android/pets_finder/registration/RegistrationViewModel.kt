package com.example.android.pets_finder.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.common.Resource
import com.example.android.domain.entities.UserModel
import com.example.android.domain.usecases.registration.RegisterUserUseCase
import com.example.android.domain.usecases.user.AddUserToDBUseCase
import com.example.android.pets_finder.login.LoginViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val addUserToDBUseCase: AddUserToDBUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _userSignUpStatus: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Success(LoginViewModel.EMPTY_ID))
    val userSignUpStatus: StateFlow<Resource<String>> = _userSignUpStatus

    // метод регистрации пользователя
    fun signUpUser(
        email: String,
        userName: String,
        userPhoneNumber: String,
        password: String,
        confirmPassword: String
    ) {
        // проверка на зполненость полей формы регистрации
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() ||
            userName.isBlank() || userPhoneNumber.isBlank()
        ) {
            _userSignUpStatus.value = Resource.Error(EMPTY_FIELDS)
        } else {
            // проверка совпадения паролей
            if (password != confirmPassword) {
                _userSignUpStatus.value =
                    Resource.Error(PASS_AND_CONF_PASS_DO_NOT_MATCH)
            } else {
                _userSignUpStatus.value = Resource.Loading()
                viewModelScope.launch(dispatcher) {
                    // регистрация пользователя
                    val registerResult =
                        registerUserUseCase.execute(email, password)
                    if (registerResult.data != null) {
                        val user = UserModel(
                            id = registerResult.data.toString(),
                            userName = userName,
                            userPhoneNumber = userPhoneNumber,
                            userEmail = email
                        )
                        // добавления пользователя в бд
                        val addUserToBDResult = addUserToDBUseCase.execute(user)
                        _userSignUpStatus.value =
                            Resource.Success(addUserToBDResult.data.toString())
                    } else {
                        _userSignUpStatus.value = Resource.Error(FAILED)
                    }
                }
            }
        }
    }

    companion object {
        const val EMPTY_ID = ""
        const val EMPTY_FIELDS = "All fields must be filled"
        const val PASS_AND_CONF_PASS_DO_NOT_MATCH = "Password and Confirm Password do not match"
        const val FAILED = "Failed"
    }
}
