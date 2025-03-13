package com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
 * Composable function for the password recovery screen.
 *
 * This screen allows users to reset their password by entering their email address.
 * It provides input validation and displays feedback messages using Toasts and dialogs.
 *
 * @param viewModel The ViewModel managing the password recovery logic.
 * @param navigateToSplash Callback function to navigate back to the splash screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordRecoveryScreen(
    viewModel: PasswordRecoveryScreenViewModel,
    navigateToSplash: () -> Unit
) {
    val context = LocalContext.current
    val passwordRecoveryScreenState by viewModel.passwordRecoveryScreenState.collectAsState()
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
            TextField(
                value = email,
                onValueChange = { newValue ->
                    email = newValue
                    viewModel.onEmailChanged(newValue.text)
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = when (passwordRecoveryScreenState) {
                            is PasswordRecoveryScreenState.InvalidInput -> stringResource(R.string.error_email_format)
                            is PasswordRecoveryScreenState.EmptyInput -> stringResource(R.string.error_email_empty)
                            else -> ""
                        }
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = if (passwordRecoveryScreenState is PasswordRecoveryScreenState.InvalidInput) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onButtonClicked(email.text) },
                enabled = passwordRecoveryScreenState is PasswordRecoveryScreenState.ValidInput,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.title_sendEmail_button))
            }

            if (passwordRecoveryScreenState is PasswordRecoveryScreenState.ShowDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.onDialogButtonClicked() },
                    confirmButton = {
                        Button(onClick = { viewModel.onDialogButtonClicked() }) {
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

//PREVIEW A FAIRE