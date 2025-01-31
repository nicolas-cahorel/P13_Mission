package com.openclassrooms.hexagonal.games.screen.loginScreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor() : ViewModel() {

    private val _isUserLoggedInState = MutableStateFlow<Boolean>(false)
    val isUserLoggedInState: StateFlow<Boolean> get() = _isUserLoggedInState

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        _isUserLoggedInState.value = currentUser != null
    }

}