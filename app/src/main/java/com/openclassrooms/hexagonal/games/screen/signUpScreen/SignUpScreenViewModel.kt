package com.openclassrooms.hexagonal.games.screen.signUpScreen

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
class SignUpScreenViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private val _signUpScreenState =
        MutableStateFlow<SignUpScreenState>(SignUpScreenState.InvalidInput("", "", false,false))
    val signUpScreenState: StateFlow<SignUpScreenState> get() = _signUpScreenState

    private var name: String = ""
    private var nameTextFieldLabel: String = ""
    private var nameIsValid: Boolean = false
    private var password: String = ""
    private var passwordTextFieldLabel: String = ""
    private var passwordIsValid: Boolean = false


    fun onNameChanged(newName: String) {
        name = newName
        validateInputs()
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword
        validateInputs()
    }

    private fun validateInputs() {
        validateName(name)
        validatePassword(password)

        if (nameIsValid && passwordIsValid) {
            _signUpScreenState.value = SignUpScreenState.ValidInput
        } else {
            _signUpScreenState.value = SignUpScreenState.InvalidInput(
                nameTextFieldLabel = nameTextFieldLabel,
                passwordTextFieldLabel = passwordTextFieldLabel,
                isNameValid = nameIsValid,
                isPasswordValid = passwordIsValid
            )
        }
    }

    private fun validateName(name: String) {
        if (name.isNotEmpty()) {
            nameTextFieldLabel = ""
            nameIsValid = true
        } else {
            nameTextFieldLabel = application.getString(R.string.error_name_empty)
            nameIsValid = false
        }
    }

    private fun validatePassword(password: String) {
        if (password.isNotEmpty()) {
            val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"
            if (password.trim().matches(passwordPattern.toRegex())) {
                passwordTextFieldLabel = ""
                passwordIsValid = true
            } else {
                passwordTextFieldLabel = application.getString(R.string.error_password_format)
                passwordIsValid = false
            }
        } else {
            passwordTextFieldLabel = application.getString(R.string.error_password_empty)
            passwordIsValid = false
        }
    }

    fun onButtonClicked(email: String, password: String) {
        if (isInternetAvailable(application)) {
            val firebaseAuth = FirebaseAuth.getInstance()
            viewModelScope.launch {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _signUpScreenState.value =
                                SignUpScreenState.SignUpSuccess
                        } else {
                            _signUpScreenState.value =
                                SignUpScreenState.SignUpError(application.getString(R.string.toast_create_account_error))
                        }
                    }
            }
        }else {
            _signUpScreenState.value =
                SignUpScreenState.SignUpError(application.getString(R.string.toast_no_internet))
        }
    }
}

private fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}
