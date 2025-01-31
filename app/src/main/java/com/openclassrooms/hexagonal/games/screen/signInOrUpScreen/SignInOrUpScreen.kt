package com.openclassrooms.hexagonal.games.screen.signInOrUpScreen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.hexagonal.games.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInOrUpScreen(
    onEmailEntered: (Boolean, String) -> Unit
) {
    val context = LocalContext.current
    val errorEmailFormat = stringResource(R.string.error_email_format)
    val errorEmailEmpty = stringResource(R.string.error_email_empty)
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var accountExists by remember { mutableStateOf(false) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var emailTextFieldLabel by remember { mutableStateOf(errorEmailEmpty) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_signInOrUp_topAppBar)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(R.string.title_email),
                style = MaterialTheme.typography.titleLarge
            )

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    if (email.text.isNotEmpty()) {
                        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
                        if (email.text.trim().matches(emailPattern.toRegex())) {
                            emailTextFieldLabel = ""
                            isButtonEnabled = true
                        } else {
                            emailTextFieldLabel = errorEmailFormat
                            isButtonEnabled = false
                        }
                    } else {
                        emailTextFieldLabel = errorEmailEmpty
                        isButtonEnabled = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(emailTextFieldLabel) },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = if (!isButtonEnabled) Color.Red else MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = if (isButtonEnabled) Color.Red else MaterialTheme.colorScheme.onSurface,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isInternetAvailable(context)) {
                        val firebaseAuth = FirebaseAuth.getInstance()
                        firebaseAuth.fetchSignInMethodsForEmail(email.text.trim())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    accountExists =
                                        task.result?.signInMethods?.isNotEmpty() == true
                                    onEmailEntered(accountExists, email.text.trim())
                                } else {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.toast_error),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_no_internet),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.signInOrUp_button))
            }

        }
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}

@Preview(showBackground = true)
@Composable
fun PreviewSignInOrUpScreen() {
    SignInOrUpScreen { _, _ -> }
}
