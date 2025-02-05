package com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen

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
class PasswordRecoveryScreenViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private val _passwordRecoveryScreenState =
        MutableStateFlow<PasswordRecoveryScreenState>(
            PasswordRecoveryScreenState.InvalidInput("")
        )
    val passwordRecoveryScreenState: StateFlow<PasswordRecoveryScreenState> get() = _passwordRecoveryScreenState

    fun onEmailChanged(newEmail: String) {
        if (newEmail.isNotEmpty()) {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
            if (newEmail.trim().matches(emailPattern.toRegex())) {
                _passwordRecoveryScreenState.value =
                    PasswordRecoveryScreenState.ValidInput
            } else {
                _passwordRecoveryScreenState.value =
                    PasswordRecoveryScreenState.InvalidInput(
                        textFieldLabel = application.getString(R.string.error_email_format),
                    )
            }
        } else {
            _passwordRecoveryScreenState.value =
                PasswordRecoveryScreenState.InvalidInput(
                    textFieldLabel = application.getString(R.string.error_email_empty),
                )
        }
    }

        fun onButtonClicked(email: String) {
            val firebaseAuth = FirebaseAuth.getInstance()
            viewModelScope.launch {
                firebaseAuth.sendPasswordResetEmail(email.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _passwordRecoveryScreenState.value =
                                PasswordRecoveryScreenState.ShowDialog
                        } else {
                            _passwordRecoveryScreenState.value =
                                PasswordRecoveryScreenState.Error(application.getString(R.string.toast_error))
                        }
                    }
            }
        }

    fun onDialogButtonClicked() {
        _passwordRecoveryScreenState.value =
            PasswordRecoveryScreenState.ValidInput
    }

}