package com.openclassrooms.hexagonal.games.screen.splashScreen

import android.util.Log
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
 * ViewModel for managing the state of the splash screen.
 *
 * This ViewModel is responsible for determining if the user is logged in or not
 * by interacting with the UserRepository. Based on the result, it updates the
 * SplashScreenState which is observed by the UI to navigate to the appropriate screen.
 */
@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Mutable state flow to hold the current splash screen state
    private val _splashScreenState =
        MutableStateFlow<SplashScreenState>(SplashScreenState.UserIsNotLoggedIn)

    // Exposed state flow to be observed by the UI
    val splashScreenState: StateFlow<SplashScreenState> get() = _splashScreenState

    init {
        viewModelScope.launch {
            userRepository.readUser().collect { userResult ->
                when (userResult) {
                    is UserResult.ReadUserSuccess -> {
                        _splashScreenState.value = SplashScreenState.UserIsLoggedIn
                        Log.d("Nicolas", "UserResult: ${userResult.user}")
                    }

                    is UserResult.ReadUserError ->
                        _splashScreenState.value = SplashScreenState.UserIsNotLoggedIn

                    else -> _splashScreenState.value = SplashScreenState.UserIsNotLoggedIn
                }
            }
        }
    }

}