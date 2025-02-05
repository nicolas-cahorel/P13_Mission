package com.openclassrooms.hexagonal.games.screen.signInScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.openclassrooms.hexagonal.games.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private val _signInScreenState =
        MutableStateFlow<SignInScreenState>(SignInScreenState.InvalidInput(""))
    val signInScreenState: StateFlow<SignInScreenState> get() = _signInScreenState

    fun onPasswordChanged(newPassword: String) {
        if (newPassword.isNotEmpty()) {
            _signInScreenState.value =
                SignInScreenState.ValidInput
        } else {
            _signInScreenState.value =
                SignInScreenState.InvalidInput(
                    textFieldLabel = application.getString(R.string.error_password_empty)
                )
        }
    }

    fun onButtonClicked(email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        viewModelScope.launch {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _signInScreenState.value =
                            SignInScreenState.SignInSuccess
                    } else {
                        val exception = task.exception
                        if (exception is FirebaseAuthException && exception.errorCode == "ERROR_WRONG_PASSWORD") {
                            _signInScreenState.value =
                                SignInScreenState.SignInError(application.getString(R.string.error_password_incorrect))
                        } else {
                            _signInScreenState.value =
                                SignInScreenState.SignInError(application.getString(R.string.error_unknown))
                        }
                    }
                }
        }
    }
}