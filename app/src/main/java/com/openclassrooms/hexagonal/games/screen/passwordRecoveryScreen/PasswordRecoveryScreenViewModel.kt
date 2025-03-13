package com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling the logic of the password recovery screen.
 *
 * This ViewModel manages the state of the screen and interacts with the UserRepository
 * to handle password recovery requests.
 *
 * @property userRepository The repository used to perform password recovery operations.
 */
@HiltViewModel
class PasswordRecoveryScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Mutable state holding the current state of the password recovery screen
    private val _passwordRecoveryScreenState =
        MutableStateFlow<PasswordRecoveryScreenState>(
            PasswordRecoveryScreenState.InvalidInput
        )

    /** Public state exposed to observe the password recovery screen state */
    val passwordRecoveryScreenState: StateFlow<PasswordRecoveryScreenState> get() = _passwordRecoveryScreenState

    /**
     * Handles changes in the email input field.
     *
     * This method validates the email format and updates the state accordingly.
     *
     * @param newEmail The new email entered by the user.
     */
    fun onEmailChanged(newEmail: String) {
        if (newEmail.isNotEmpty()) {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
            if (newEmail.trim().matches(emailPattern.toRegex())) {
                _passwordRecoveryScreenState.value =
                    PasswordRecoveryScreenState.ValidInput
            } else {
                _passwordRecoveryScreenState.value =
                    PasswordRecoveryScreenState.InvalidInput
            }
        } else {
            _passwordRecoveryScreenState.value =
                PasswordRecoveryScreenState.EmptyInput

        }
    }

    /**
     * Handles the password recovery button click event.
     *
     * This method triggers a password recovery request through the UserRepository
     * and updates the screen state based on the result.
     *
     * @param email The email address entered by the user.
     */
    fun onButtonClicked(email: String) {
        viewModelScope.launch {
            userRepository.recoverPassword(email).collect { userRepositoryState ->
                _passwordRecoveryScreenState.value = when (userRepositoryState) {
                    is UserResult.RecoverPasswordSuccess -> PasswordRecoveryScreenState.ShowDialog
                    is UserResult.RecoverPasswordError -> PasswordRecoveryScreenState.Error
                    else -> PasswordRecoveryScreenState.Error
                }
            }
        }
    }

    /**
     * Handles the confirmation button click in the dialog.
     *
     * This resets the state to allow another password recovery attempt if needed.
     */
    fun onDialogButtonClicked() {
        _passwordRecoveryScreenState.value =
            PasswordRecoveryScreenState.ValidInput
    }

}