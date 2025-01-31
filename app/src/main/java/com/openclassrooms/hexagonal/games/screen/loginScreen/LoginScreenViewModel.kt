package com.openclassrooms.hexagonal.games.screen.loginScreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor() : ViewModel() {

    private val _loginScreenState = MutableStateFlow<LoginScreenState>(LoginScreenState.UserIsNotLoggedIn)
    val loginScreenState: StateFlow<LoginScreenState> get() = _loginScreenState

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            _loginScreenState.value = LoginScreenState.UserIsLoggedIn
        } else {
            _loginScreenState.value = LoginScreenState.UserIsNotLoggedIn
        }
    }

}