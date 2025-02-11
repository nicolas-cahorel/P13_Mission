package com.openclassrooms.hexagonal.games.screen.signInOrUpScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserRepositoryState
import com.openclassrooms.hexagonal.games.utils.InternetUtils
import com.openclassrooms.hexagonal.games.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInOrUpScreenViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val internetUtils: InternetUtils,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _signInOrUpScreenState =
        MutableStateFlow<SignInOrUpScreenState>(SignInOrUpScreenState.InvalidInput("", false))
    val signInOrUpScreenState: StateFlow<SignInOrUpScreenState> get() = _signInOrUpScreenState

    private val _repositoryState = MutableStateFlow<UserRepositoryState?>(null)
    val repositoryState: StateFlow<UserRepositoryState?> get() = _repositoryState

    fun onEmailChanged(newEmail: String) {
        if (newEmail.isNotEmpty()) {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
            if (newEmail.trim().matches(emailPattern.toRegex())) {
                _signInOrUpScreenState.value =
                    SignInOrUpScreenState.ValidInput(
                        textFieldLabel = "",
                        isButtonEnabled = true
                    )
            } else {
                _signInOrUpScreenState.value =
                    SignInOrUpScreenState.InvalidInput(
                        textFieldLabel = resourceProvider.getString(R.string.error_email_format),
                        isButtonEnabled = false
                    )
            }
        } else {
            _signInOrUpScreenState.value =
                SignInOrUpScreenState.InvalidInput(
                    textFieldLabel = resourceProvider.getString(R.string.error_email_empty),
                    isButtonEnabled = false
                )
        }
    }

    fun onButtonClicked(email: String) {
        if (internetUtils.isInternetAvailable()) {
            viewModelScope.launch {
                userRepository.doUserExistInFirebase(email).collect { userRepositoryState ->
                    _repositoryState.value = userRepositoryState

                    _signInOrUpScreenState.value = when (userRepositoryState) {
                        is UserRepositoryState.UserFoundInFirebase -> SignInOrUpScreenState.AccountExists
                        is UserRepositoryState.UserNotFoundInFirebase -> SignInOrUpScreenState.AccountDoNotExists
                        is UserRepositoryState.UserException -> SignInOrUpScreenState.Error(
                            userRepositoryState.message
                        )

                        else -> SignInOrUpScreenState.Error(resourceProvider.getString(R.string.toast_error))
                    }
                }
            }
        } else {
            _signInOrUpScreenState.value =
                SignInOrUpScreenState.Error(resourceProvider.getString(R.string.toast_no_internet))
        }
    }
}