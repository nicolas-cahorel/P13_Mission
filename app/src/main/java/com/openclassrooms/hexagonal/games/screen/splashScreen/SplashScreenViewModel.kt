package com.openclassrooms.hexagonal.games.screen.splashScreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor() : ViewModel() {

    private val _splashScreenState = MutableStateFlow<SplashScreenState>(SplashScreenState.UserIsNotLoggedIn)
    val splashScreenState: StateFlow<SplashScreenState> get() = _splashScreenState

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            _splashScreenState.value = SplashScreenState.UserIsLoggedIn
        } else {
            _splashScreenState.value = SplashScreenState.UserIsNotLoggedIn
        }
    }

}