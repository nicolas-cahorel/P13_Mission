package com.openclassrooms.hexagonal.games.screen.userAccountScreen

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
 * ViewModel for the User Account Screen, responsible for handling user account actions.
 *
 * This ViewModel interacts with the UserRepository to manage user account actions like
 * signing out and deleting the account. It maintains the state of the UserAccountScreen
 * and emits updates based on the success or failure of these actions.
 */
@HiltViewModel
class UserAccountScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Mutable state flow that holds the current state of the UserAccountScreen.
     * The initial state is OnStart, indicating the screen is being initialized.
     */
    private val _userAccountScreenState =
        MutableStateFlow<UserAccountScreenState>(UserAccountScreenState.OnStart)

    /**
     * Exposed state flow for the UserAccountScreenState, allowing the UI to observe changes.
     */
    val userAccountScreenState: StateFlow<UserAccountScreenState> get() = _userAccountScreenState

    /**
     * Handles the deletion of the user's account.
     *
     * This function triggers the deletion process via the UserRepository and updates
     * the screen's state based on whether the deletion succeeds or fails.
     */
    fun onDeleteButtonClicked() {
        viewModelScope.launch {
            userRepository.deleteUser().collect { userResult ->
                when (userResult) {
                    is UserResult.DeleteUserSuccess ->
                        _userAccountScreenState.value = UserAccountScreenState.DeleteUserSuccess

                    is UserResult.DeleteUserError ->
                        _userAccountScreenState.value = UserAccountScreenState.DeleteUserError

                    else -> _userAccountScreenState.value = UserAccountScreenState.DeleteUserError
                }
            }
        }
    }

    /**
     * Handles the sign-out of the user.
     *
     * This function triggers the sign-out process via the UserRepository and updates
     * the screen's state based on whether the sign-out succeeds or fails.
     */
    fun onSignOutButtonClicked() {
        viewModelScope.launch {
            userRepository.signOut().collect { userResult ->
                when (userResult) {
                    is UserResult.SignOutSuccess ->
                        _userAccountScreenState.value = UserAccountScreenState.SignOutUserSuccess

                    is UserResult.SignOutError ->
                        _userAccountScreenState.value = UserAccountScreenState.SignOutUserError

                    else -> _userAccountScreenState.value = UserAccountScreenState.SignOutUserError
                }
            }
        }
    }

}