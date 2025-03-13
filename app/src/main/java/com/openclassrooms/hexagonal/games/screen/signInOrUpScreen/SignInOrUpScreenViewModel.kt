package com.openclassrooms.hexagonal.games.screen.signInOrUpScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserResult
import com.openclassrooms.hexagonal.games.utils.InternetUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the SignIn or SignUp screen's logic.
 * This ViewModel is responsible for handling the email input validation and determining whether
 * the user should sign in or sign up based on the existence of the user's account.
 */
@HiltViewModel
class SignInOrUpScreenViewModel @Inject constructor(
    private val internetUtils: InternetUtils,
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Mutable state flow to hold the current state of the sign-in or sign-up screen.
     * This state is observed by the UI to determine which state to display.
     */
    private val _signInOrUpScreenState =
        MutableStateFlow<SignInOrUpScreenState>(SignInOrUpScreenState.InvalidInput)

    /**
     * Exposed state flow for observing the current state of the screen.
     * This is a read-only property to ensure immutability from outside the ViewModel.
     */
    val signInOrUpScreenState: StateFlow<SignInOrUpScreenState> get() = _signInOrUpScreenState

    /**
     * Updates the screen state based on the email input.
     * Validates the email format and updates the state to reflect whether the input is valid or invalid.
     *
     * @param newEmail The new email entered by the user.
     */
    fun onEmailChanged(newEmail: String) {
        if (newEmail.isNotEmpty()) {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
            if (newEmail.trim().matches(emailPattern.toRegex())) {
                _signInOrUpScreenState.value =
                    SignInOrUpScreenState.ValidInput
            } else {
                _signInOrUpScreenState.value =
                    SignInOrUpScreenState.InvalidInput
            }
        } else {
            _signInOrUpScreenState.value =
                SignInOrUpScreenState.InvalidInput
        }
    }

    /**
     * Handles the button click event to either sign in or sign up the user based on the email.
     * It checks if the device has an internet connection and then calls the repository to check
     * if the user exists in Firebase.
     *
     * If the email is found, it updates the state to reflect that the account exists or doesn't exist.
     * If there is an error or no network, it updates the state accordingly.
     *
     * @param email The email entered by the user to check if the account exists.
     */
    fun onButtonClicked(email: String) {
        if (internetUtils.isInternetAvailable()) {
            viewModelScope.launch {
                userRepository.doUserExistInFirebase(email).collect { userRepositoryState ->
                    _signInOrUpScreenState.value = when (userRepositoryState) {
                        is UserResult.UserInFirebaseFound -> SignInOrUpScreenState.AccountExists
                        is UserResult.UserInFirebaseNotFound -> SignInOrUpScreenState.AccountDoNotExists
                        is UserResult.UserInFirebaseError -> SignInOrUpScreenState.Error

                        else -> SignInOrUpScreenState.Error
                    }
                }
            }
        } else {
            _signInOrUpScreenState.value =
                SignInOrUpScreenState.NoNetwork
        }
    }
}