package com.openclassrooms.hexagonal.games.screen.userAccountScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.hexagonal.games.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAccountScreenViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private val _userAccountScreenState =
        MutableStateFlow<UserAccountScreenState>(UserAccountScreenState.OnStart)
    val userAccountScreenState: StateFlow<UserAccountScreenState> get() = _userAccountScreenState

    fun onDeleteButtonClicked() {
        val firebaseAuth = FirebaseAuth.getInstance()
        viewModelScope.launch {
            firebaseAuth.currentUser?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseAuth.signOut()
                        _userAccountScreenState.value = UserAccountScreenState.UserDeleted
                    } else {
                        _userAccountScreenState.value =
                            UserAccountScreenState.Error(application.getString(R.string.toast_error))
                    }
                }
        }
    }

    fun onSignOutButtonClicked() {
        val firebaseAuth = FirebaseAuth.getInstance()
        viewModelScope.launch {
            try {
                firebaseAuth.signOut()
                _userAccountScreenState.value = UserAccountScreenState.UserSignedOut
            } catch (e: Exception) {
                _userAccountScreenState.value = UserAccountScreenState.Error(e.message ?: application.getString(R.string.toast_error))
            }
        }
    }

}