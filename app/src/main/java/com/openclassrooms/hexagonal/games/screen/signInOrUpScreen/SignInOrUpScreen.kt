package com.openclassrooms.hexagonal.games.screen.signInOrUpScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.hexagonal.games.R

/**
 * Displays the Sign In or Sign Up screen.
 *
 * This screen includes an email input field, a button to proceed, and a top app bar.
 * Based on the current state, it either navigates to the sign-in or sign-up screen
 * or displays error messages via a Toast.
 *
 * @param signInOrUpScreenState The current UI state of the screen.
 * @param onEmailChanged Callback invoked when the email input changes.
 * @param onButtonClicked Callback invoked when the proceed button is clicked.
 * @param navigateToSignUp Callback to navigate to the sign-up screen with the entered email.
 * @param navigateToSignIn Callback to navigate to the sign-in screen with the entered email.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInOrUpScreen(
    signInOrUpScreenState: SignInOrUpScreenState,
    onEmailChanged: (email: String) -> Unit,
    onButtonClicked: (email: String) -> Unit,
    navigateToSignUp: (email: String) -> Unit,
    navigateToSignIn: (email: String) -> Unit
) {
    val context = LocalContext.current
    val labelEmailText = when (signInOrUpScreenState) {
        is SignInOrUpScreenState.InvalidInput -> stringResource(R.string.error_email_format)
        else -> ""
    }
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

            OutlinedTextField(
                value = email,
                onValueChange = { newValue ->
                    email = newValue
                    onEmailChanged(newValue.text)
                },
                isError = signInOrUpScreenState is SignInOrUpScreenState.InvalidInput,
                label = { Text(text = labelEmailText) },
                colors = TextFieldDefaults.colors(focusedLabelColor = MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onButtonClicked(email.text) },
                enabled = signInOrUpScreenState is SignInOrUpScreenState.ValidInput,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.signInOrUp_button))
            }

        }
    }
}

/**
 * Preview for [SignInOrUpScreen].
 */
@Preview(showBackground = true)
@Composable
fun PreviewSignInOrUpScreen() {
    SignInOrUpScreen(
        signInOrUpScreenState = SignInOrUpScreenState.ValidInput,
        onEmailChanged = { },
        onButtonClicked = { },
        navigateToSignUp = { },
        navigateToSignIn = { }
    )
}

/**
 * Preview for [SignInOrUpScreen].
 */
@Preview(showBackground = true)
@Composable
fun PreviewSignInOrUpScreenInvalidInput() {
    SignInOrUpScreen(
        signInOrUpScreenState = SignInOrUpScreenState.InvalidInput,
        onEmailChanged = { },
        onButtonClicked = { },
        navigateToSignUp = { },
        navigateToSignIn = { }
    )
}