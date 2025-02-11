package com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserRepositoryState
import com.openclassrooms.hexagonal.games.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryScreenViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _passwordRecoveryScreenState =
        MutableStateFlow<PasswordRecoveryScreenState>(
            PasswordRecoveryScreenState.InvalidInput("")
        )
    val passwordRecoveryScreenState: StateFlow<PasswordRecoveryScreenState> get() = _passwordRecoveryScreenState

    private val _repositoryState = MutableStateFlow<UserRepositoryState?>(null)
    val repositoryState: StateFlow<UserRepositoryState?> get() = _repositoryState


    fun onEmailChanged(newEmail: String) {
        if (newEmail.isNotEmpty()) {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
            if (newEmail.trim().matches(emailPattern.toRegex())) {
                _passwordRecoveryScreenState.value =
                    PasswordRecoveryScreenState.ValidInput
            } else {
                _passwordRecoveryScreenState.value =
                    PasswordRecoveryScreenState.InvalidInput(
                        textFieldLabel = resourceProvider.getString(R.string.error_email_format),
                    )
            }
        } else {
            _passwordRecoveryScreenState.value =
                PasswordRecoveryScreenState.InvalidInput(
                    textFieldLabel = resourceProvider.getString(R.string.error_email_empty),
                )
        }
    }

        fun onButtonClicked(email: String) {
            viewModelScope.launch {
                userRepository.recoverPassword(email).collect { userRepositoryState ->
                    _repositoryState.value = userRepositoryState

                    _passwordRecoveryScreenState.value = when (userRepositoryState) {
                        is UserRepositoryState.RecoverPasswordSuccess -> PasswordRecoveryScreenState.ShowDialog
                        is UserRepositoryState.RecoverPasswordError -> PasswordRecoveryScreenState.Error(userRepositoryState.message)
                        else -> PasswordRecoveryScreenState.Error(resourceProvider.getString(R.string.toast_error))
                    }
                }
            }
        }

    fun onDialogButtonClicked() {
        _passwordRecoveryScreenState.value =
            PasswordRecoveryScreenState.ValidInput
    }

}