package com.openclassrooms.hexagonal.games.screen.signUpScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserResult
import com.openclassrooms.hexagonal.games.domain.model.User
import com.openclassrooms.hexagonal.games.utils.InternetUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the sign-up screen logic.
 *
 * This ViewModel is responsible for validating the user's input, interacting with the user repository
 * to create a new user, and updating the UI state accordingly. It also checks for internet connectivity.
 */
@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val internetUtils: InternetUtils,
    private val userRepository: UserRepository
) : ViewModel() {

    // StateFlow representing the current state of the sign-up screen.
    private val _signUpScreenState =
        MutableStateFlow<SignUpScreenState>(SignUpScreenState.InvalidInput(true, true, false))
    val signUpScreenState: StateFlow<SignUpScreenState> get() = _signUpScreenState

    // StateFlow for storing the user's information during sign-up.
    private val _newUser = MutableStateFlow(
        User(
            id = "",
            firstname = "",
            lastname = "",
            email = "",
            password = ""
        )
    )
    val newUser: StateFlow<User> get() = _newUser

    // Variables to track input validation states
    private var isNameEmpty: Boolean = true
    private var isPasswordEmpty: Boolean = true
    private var isPasswordFormatCorrect: Boolean = false
    private var name: String = ""
    private var password: String = ""

    /**
     * Called when the user changes the name input.
     * It updates the name and triggers input validation.
     *
     * @param newName The new name input from the user.
     */
    fun onNameChanged(newName: String) {
        name = newName
        validateInputs()
    }

    /**
     * Called when the user changes the password input.
     * It updates the password and triggers input validation.
     *
     * @param newPassword The new password input from the user.
     */
    fun onPasswordChanged(newPassword: String) {
        password = newPassword
        validateInputs()
    }

    /**
     * Validates the user's name and password.
     * Updates the screen state based on the validity of the inputs.
     */
    private fun validateInputs() {
        validateName()
        validatePassword()
        if (!isNameEmpty && !isPasswordEmpty && isPasswordFormatCorrect) {
            _signUpScreenState.value = SignUpScreenState.ValidInput
            Log.d("Nicolas", "SignUpScreenState is now : ${_signUpScreenState.value}")
        } else {
            _signUpScreenState.value = SignUpScreenState.InvalidInput(
                isNameEmpty,
                isPasswordEmpty,
                isPasswordFormatCorrect
            )
            Log.d("Nicolas", "SignUpScreenState is now : ${_signUpScreenState.value}")
        }
    }

    /**
     * Validates the name input.
     * Updates the `isNameEmpty` flag based on whether the name is empty.
     */
    private fun validateName() {
        isNameEmpty = name.isEmpty()
    }

    /**
     * Validates the password input.
     * Checks if the password is non-empty and follows a valid format.
     */
    private fun validatePassword() {
        if (password.isNotEmpty()) {
            isPasswordEmpty = false
            val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"
            isPasswordFormatCorrect = password.trim().matches(passwordPattern.toRegex())
        } else {
            isPasswordEmpty = true
        }
    }

    /**
     * Called when the user clicks the sign-up button.
     * It collects the input data, checks for internet connectivity, and attempts to create the user.
     *
     * @param email The email address of the user.
     */
    fun onButtonClicked(email: String) {

        _newUser.value = _newUser.value.copy(
            email = email,
            firstname = name.substringBefore(" ").trim(),
            lastname = name.substringAfter(" ", "").trim(),
            password = password.trim()
        )

        if (internetUtils.isInternetAvailable()) {

            viewModelScope.launch {

                userRepository.createUser(_newUser.value).collect { userResult ->
                    when (userResult) {
                        is UserResult.CreateUserSuccess ->
                            _signUpScreenState.value = SignUpScreenState.SignUpSuccess

                        is UserResult.CreateUserError ->
                            _signUpScreenState.value = SignUpScreenState.SignUpError

                        else -> _signUpScreenState.value = SignUpScreenState.SignUpError
                    }
                }
            }
        } else {
            _signUpScreenState.value = SignUpScreenState.NoNetwork
        }
    }
}