package com.openclassrooms.hexagonal.games.screen.signInOrUpScreen

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
class SignInOrUpScreenViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private val _signInOrUpScreenState =
        MutableStateFlow<SignInOrUpScreenState>(SignInOrUpScreenState.InvalidInput("", false))
    val signInOrUpScreenState: StateFlow<SignInOrUpScreenState> get() = _signInOrUpScreenState

    fun onEmailChanged(newEmail: String) {
        if (newEmail.isNotEmpty()) {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
            if (newEmail.trim().matches(emailPattern.toRegex())) {
                _signInOrUpScreenState.value =
                    SignInOrUpScreenState.ValidInput(
                        textFieldLabel = "",
                        isButtonEnabled = true
                    )
            } else {
                _signInOrUpScreenState.value =
                    SignInOrUpScreenState.InvalidInput(
                        textFieldLabel = application.getString(R.string.error_email_format),
                        isButtonEnabled = false
                    )
            }
        } else {
            _signInOrUpScreenState.value =
                SignInOrUpScreenState.InvalidInput(
                    textFieldLabel = application.getString(R.string.error_email_empty),
                    isButtonEnabled = false
                )
        }
    }

    fun onButtonClicked(email: String) {
        if (isInternetAvailable(application)) {
            val firebaseAuth = FirebaseAuth.getInstance()
            viewModelScope.launch {
                firebaseAuth.fetchSignInMethodsForEmail(email.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (task.result?.signInMethods?.isNotEmpty() == true) {
                                _signInOrUpScreenState.value =
                                    SignInOrUpScreenState.AccountExists
                            } else {
                                _signInOrUpScreenState.value =
                                    SignInOrUpScreenState.AccountDoNotExists
                            }
                        } else {
                            _signInOrUpScreenState.value =
                                SignInOrUpScreenState.Error(application.getString(R.string.toast_error))
                        }
                    }
            }
        } else {
            _signInOrUpScreenState.value =
                SignInOrUpScreenState.Error(application.getString(R.string.toast_no_internet))
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

}