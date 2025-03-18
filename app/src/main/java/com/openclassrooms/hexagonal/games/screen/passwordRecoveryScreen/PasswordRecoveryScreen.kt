package com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
 * Displays the password recovery screen UI.
 *
 * Allows users to reset their password by entering their email address.
 * Includes input validation and provides feedback via Toasts and dialogs.
 *
 * @param passwordRecoveryScreenState The current state of the password recovery screen, managing UI and validation status.
 * @param onEmailChanged Callback triggered when the email input changes.
 * @param onButtonClicked Callback triggered when the reset button is clicked.
 * @param onDialogButtonClicked Callback triggered when the confirmation dialog button is clicked.
 * @param navigateToSplash Callback to navigate back to the splash screen.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordRecoveryScreen(
    passwordRecoveryScreenState: PasswordRecoveryScreenState,
    onEmailChanged: (email: String) -> Unit,
    onButtonClicked: (email: String) -> Unit,
    onDialogButtonClicked: () -> Unit,
    navigateToSplash: () -> Unit
) {
    val context = LocalContext.current
    val labelEmailText = when (passwordRecoveryScreenState) {
        is PasswordRecoveryScreenState.InvalidInput -> stringResource(R.string.error_email_format)
        is PasswordRecoveryScreenState.EmptyInput -> stringResource(R.string.error_email_empty)
        else -> ""
    }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    // Observes the state and displays an error message if necessary
    LaunchedEffect(passwordRecoveryScreenState) {
        when (passwordRecoveryScreenState) {
            is PasswordRecoveryScreenState.Error -> {
                Toast.makeText(
                    context,
                    R.string.toast_error,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_passwordRecovery_topAppBar)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navigateToSplash() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.backButton_topAppBar_description),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
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
                text = stringResource(R.string.title_recovery_instructions),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                isError = passwordRecoveryScreenState is PasswordRecoveryScreenState.InvalidInput ||
                        passwordRecoveryScreenState is PasswordRecoveryScreenState.EmptyInput,
                label = { Text(text = labelEmailText) },
                colors = TextFieldDefaults.colors(focusedLabelColor = MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onButtonClicked(email.text) },
                enabled = passwordRecoveryScreenState is PasswordRecoveryScreenState.ValidInput,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.title_sendEmail_button))
            }

            if (passwordRecoveryScreenState is PasswordRecoveryScreenState.ShowDialog) {
                AlertDialog(
                    onDismissRequest = { onDialogButtonClicked() },
                    confirmButton = {
                        Button(onClick = { onDialogButtonClicked() }) {
                            Text(stringResource(R.string.ok_dialog_button))
                        }
                    },
                    title = {
                        Text(stringResource(R.string.dialog_title))
                    },
                    text = {
                        Text(stringResource(R.string.dialog_message, email.text))
                    }
                )
            }

        }
    }
}

/**
 * Preview for [PasswordRecoveryScreen].
 */
@Preview
@Composable
fun PasswordRecoveryScreenPreview() {
    PasswordRecoveryScreen(
        passwordRecoveryScreenState = PasswordRecoveryScreenState.ValidInput,
        onEmailChanged = {},
        onButtonClicked = {},
        onDialogButtonClicked = {},
        navigateToSplash = {}
    )
}

/**
 * Preview for [PasswordRecoveryScreen].
 */
@Preview
@Composable
fun PasswordRecoveryScreenPreviewInvalidInput() {
    PasswordRecoveryScreen(
        passwordRecoveryScreenState = PasswordRecoveryScreenState.InvalidInput,
        onEmailChanged = {},
        onButtonClicked = {},
        onDialogButtonClicked = {},
        navigateToSplash = {}
    )
}