package com.openclassrooms.hexagonal.games.screen.signInScreen

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
 * ViewModel for managing the Sign-In screen state and user authentication logic.
 *
 * This ViewModel is responsible for handling user input (email/password), validating the password,
 * and interacting with the repository to perform sign-in actions. It exposes a state to the UI to reflect
 * the current status of the sign-in process.
 */
@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Mutable state to hold the current state of the Sign-In screen.
    private val _signInScreenState =
        MutableStateFlow<SignInScreenState>(SignInScreenState.InvalidInput)

    // Exposing the state as a public StateFlow for the UI to observe.
    val signInScreenState: StateFlow<SignInScreenState> get() = _signInScreenState

    /**
     * Called when the password input changes.
     *
     * This function updates the state based on the validity of the password input.
     * If the password is not empty, it sets the state to ValidInput, otherwise, it sets it to InvalidInput.
     *
     * @param newPassword The new password entered by the user.
     */
    fun onPasswordChanged(newPassword: String) {
        if (newPassword.isNotEmpty()) {
            _signInScreenState.value =
                SignInScreenState.ValidInput
        } else {
            _signInScreenState.value =
                SignInScreenState.InvalidInput
        }
    }

    /**
     * Called when the sign-in button is clicked.
     *
     * This function triggers the sign-in process by calling the repository to authenticate the user
     * with the provided email and password. The resulting state is updated based on the success or failure
     * of the sign-in attempt.
     *
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     */
    fun onButtonClicked(email: String, password: String) {
        viewModelScope.launch {
            userRepository.signIn(email, password).collect { userRepositoryState ->
                _signInScreenState.value = when (userRepositoryState) {
                    is UserResult.SignInSuccess -> SignInScreenState.SignInSuccess
                    is UserResult.SignInError -> SignInScreenState.SignInError
                    else -> SignInScreenState.SignInError
                }
            }
        }
    }

}