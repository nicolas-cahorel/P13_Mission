package com.openclassrooms.hexagonal.games.screen.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserRepositoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _splashScreenState = MutableStateFlow<SplashScreenState>(SplashScreenState.UserIsNotLoggedIn)
    val splashScreenState: StateFlow<SplashScreenState> get() = _splashScreenState

    private val _repositoryState = MutableStateFlow<UserRepositoryState?>(null)
    val repositoryState: StateFlow<UserRepositoryState?> get() = _repositoryState

    init {
        viewModelScope.launch {
            userRepository.readCurrentUser().collect { userRepositoryState ->
                _repositoryState.value = userRepositoryState

                _splashScreenState.value = when (userRepositoryState) {
                    is UserRepositoryState.GetUserDataSuccess -> SplashScreenState.UserIsLoggedIn
                    is UserRepositoryState.GetUserDataError -> SplashScreenState.UserIsNotLoggedIn
                    else -> SplashScreenState.UserIsNotLoggedIn
                }
            }
        }
    }

}