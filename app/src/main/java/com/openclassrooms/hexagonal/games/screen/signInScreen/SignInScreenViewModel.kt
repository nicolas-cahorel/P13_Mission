package com.openclassrooms.hexagonal.games.screen.signInScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserRepositoryState
import com.openclassrooms.hexagonal.games.domain.model.User
import com.openclassrooms.hexagonal.games.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _signInScreenState =
        MutableStateFlow<SignInScreenState>(SignInScreenState.InvalidInput(""))
    val signInScreenState: StateFlow<SignInScreenState> get() = _signInScreenState

    private val _repositoryState = MutableStateFlow<UserRepositoryState?>(null)
    val repositoryState: StateFlow<UserRepositoryState?> get() = _repositoryState

    fun onPasswordChanged(newPassword: String) {
        if (newPassword.isNotEmpty()) {
            _signInScreenState.value =
                SignInScreenState.ValidInput
        } else {
            _signInScreenState.value =
                SignInScreenState.InvalidInput(
                    textFieldLabel = resourceProvider.getString(R.string.error_password_empty)
                )
        }
    }

    fun onButtonClicked(email: String, password: String) {
        viewModelScope.launch {

            val user = User(
                id = null,
                firstname = "",
                lastname = "",
                email = email,
                password = password
            )

            userRepository.signIn(user).collect { userRepositoryState ->
                _repositoryState.value = userRepositoryState

                _signInScreenState.value = when (userRepositoryState) {
                    is UserRepositoryState.SignInSuccess -> SignInScreenState.SignInSuccess
                    is UserRepositoryState.SignInError -> SignInScreenState.SignInError(
                        userRepositoryState.message
                    )

                    else -> SignInScreenState.SignInError(resourceProvider.getString(R.string.error_unknown))
                }
            }
        }
    }

}