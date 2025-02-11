package com.openclassrooms.hexagonal.games.screen.userAccountScreen

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
class UserAccountScreenViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userAccountScreenState =
        MutableStateFlow<UserAccountScreenState>(UserAccountScreenState.OnStart)
    val userAccountScreenState: StateFlow<UserAccountScreenState> get() = _userAccountScreenState

    private val _repositoryState = MutableStateFlow<UserRepositoryState?>(null)
    val repositoryState: StateFlow<UserRepositoryState?> get() = _repositoryState

    fun onDeleteButtonClicked() {
        viewModelScope.launch {
            userRepository.deleteUser().collect { userRepositoryState ->
                _repositoryState.value = userRepositoryState

                _userAccountScreenState.value = when (userRepositoryState) {
                    is UserRepositoryState.DeleteUserSuccess -> UserAccountScreenState.UserDeleted
                    is UserRepositoryState.DeleteUserError -> UserAccountScreenState.Error(userRepositoryState.message)
                    else -> UserAccountScreenState.Error(resourceProvider.getString(R.string.toast_error))
                }
            }
        }
    }

    fun onSignOutButtonClicked() {
        viewModelScope.launch {
            userRepository.signOut().collect { userRepositoryState ->
                _repositoryState.value = userRepositoryState

                _userAccountScreenState.value = when (userRepositoryState) {
                    is UserRepositoryState.SignOutSuccess -> UserAccountScreenState.UserSignedOut
                    is UserRepositoryState.SignOutError -> UserAccountScreenState.Error(userRepositoryState.message)
                    else -> UserAccountScreenState.Error(resourceProvider.getString(R.string.toast_error))
                }
            }
        }
    }

}