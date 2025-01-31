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
fun PasswordRecoveryScreen(
    onBackButtonClicked: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val errorEmailFormat = stringResource(R.string.error_email_format)
    val errorEmailEmpty = stringResource(R.string.error_email_empty)
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var emailTextFieldLabel by remember { mutableStateOf(errorEmailEmpty) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_passwordRecovery_topAppBar)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClicked(true) }) {
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
                    val firebaseAuth = FirebaseAuth.getInstance()
                    firebaseAuth.sendPasswordResetEmail(email.text.trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showDialog = true
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.toast_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.title_sendEmail_button))
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
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

@Preview(showBackground = true)
@Composable
fun PreviewPasswordRecoveryScreen() {
    PasswordRecoveryScreen(
        onBackButtonClicked = {}
    )
}