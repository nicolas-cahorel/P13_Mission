package com.openclassrooms.hexagonal.games.screen.signUpScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserRepositoryState
import com.openclassrooms.hexagonal.games.domain.model.User
import com.openclassrooms.hexagonal.games.utils.InternetUtils
import com.openclassrooms.hexagonal.games.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val internetUtils: InternetUtils,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _signUpScreenState =
        MutableStateFlow<SignUpScreenState>(SignUpScreenState.InvalidInput("", "", false,false))
    val signUpScreenState: StateFlow<SignUpScreenState> get() = _signUpScreenState

    private val _repositoryState = MutableStateFlow<UserRepositoryState?>(null)
    val repositoryState: StateFlow<UserRepositoryState?> get() = _repositoryState

    private var name: String = ""
    private var nameTextFieldLabel: String = ""
    private var nameIsValid: Boolean = false
    private var password: String = ""
    private var passwordTextFieldLabel: String = ""
    private var passwordIsValid: Boolean = false


    fun onNameChanged(newName: String) {
        name = newName
        validateInputs()
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword
        validateInputs()
    }

    private fun validateInputs() {
        validateName(name)
        validatePassword(password)

        if (nameIsValid && passwordIsValid) {
            _signUpScreenState.value = SignUpScreenState.ValidInput
        } else {
            _signUpScreenState.value = SignUpScreenState.InvalidInput(
                nameTextFieldLabel = nameTextFieldLabel,
                passwordTextFieldLabel = passwordTextFieldLabel,
                isNameValid = nameIsValid,
                isPasswordValid = passwordIsValid
            )
        }
    }

    private fun validateName(name: String) {
        if (name.isNotEmpty()) {
            nameTextFieldLabel = ""
            nameIsValid = true
        } else {
            nameTextFieldLabel = resourceProvider.getString(R.string.error_name_empty)
            nameIsValid = false
        }
    }

    private fun validatePassword(password: String) {
        if (password.isNotEmpty()) {
            val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"
            if (password.trim().matches(passwordPattern.toRegex())) {
                passwordTextFieldLabel = ""
                passwordIsValid = true
            } else {
                passwordTextFieldLabel = resourceProvider.getString(R.string.error_password_format)
                passwordIsValid = false
            }
        } else {
            passwordTextFieldLabel = resourceProvider.getString(R.string.error_password_empty)
            passwordIsValid = false
        }
    }

    fun onButtonClicked(email: String, password: String, name: String) {
        if (internetUtils.isInternetAvailable()) {
            viewModelScope.launch {
                val user = User(
                    id = null,
                    firstname = name.substringBefore(" "),
                    lastname = name.substringAfter(" "),
                    email = email,
                    password = password
                )
                userRepository.createUser(user).collect { userRepositoryState ->
                    _repositoryState.value = userRepositoryState

                    _signUpScreenState.value = when (userRepositoryState) {
                        is UserRepositoryState.CreateUserSuccess -> SignUpScreenState.SignUpSuccess
                        is UserRepositoryState.CreateUserError -> SignUpScreenState.SignUpError(
                            userRepositoryState.message
                        )

                        else -> SignUpScreenState.SignUpError(resourceProvider.getString(R.string.toast_create_account_error))
                    }
                }
            }
        } else {
            _signUpScreenState.value =
                SignUpScreenState.SignUpError(resourceProvider.getString(R.string.toast_no_internet))
        }
    }
}
