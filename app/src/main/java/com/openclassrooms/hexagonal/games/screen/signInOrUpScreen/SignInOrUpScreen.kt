package com.openclassrooms.hexagonal.games.screen.signInOrUpScreen

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.openclassrooms.hexagonal.games.R

/**
 * Composable function that displays the Sign In or Sign Up screen.
 *
 * This screen presents a text field for entering an email, a button to proceed, and a top app bar.
 * Based on the current state, it navigates either to the sign-in or sign-up screen or displays
 * error messages via a Toast.
 *
 * @param viewModel The [SignInOrUpScreenViewModel] that handles the business logic and state management for the screen.
 * @param signInOrUpScreenState The [SignInOrUpScreenState] that holds the current UI state of the screen.
 * @param navigateToSignUp A lambda function to navigate to the sign-up screen, passing the entered email.
 * @param navigateToSignIn A lambda function to navigate to the sign-in screen, passing the entered email.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInOrUpScreen(
    viewModel: SignInOrUpScreenViewModel,
    signInOrUpScreenState: SignInOrUpScreenState,
    navigateToSignUp: (email: String) -> Unit,
    navigateToSignIn: (email: String) -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(signInOrUpScreenState) {
        when (signInOrUpScreenState) {
            is SignInOrUpScreenState.AccountExists -> navigateToSignIn(email.text)
            is SignInOrUpScreenState.AccountDoNotExists -> navigateToSignUp(email.text)
            is SignInOrUpScreenState.Error -> {
                Toast.makeText(
                    context,
                    R.string.toast_error,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is SignInOrUpScreenState.NoNetwork -> {
                Toast.makeText(
                    context,
                    R.string.toast_no_internet,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

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
                onValueChange = { newValue ->
                    email = newValue
                    viewModel.onEmailChanged(newValue.text)
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = when (signInOrUpScreenState) {
                            is SignInOrUpScreenState.ValidInput -> ""
                            is SignInOrUpScreenState.InvalidInput -> stringResource(R.string.error_email_format)
                            else -> ""
                        }
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = if (signInOrUpScreenState is SignInOrUpScreenState.InvalidInput) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onButtonClicked(email.text) },
                enabled = signInOrUpScreenState is SignInOrUpScreenState.ValidInput,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.signInOrUp_button))
            }

        }
    }
}

//PREVIEW A FAIRE