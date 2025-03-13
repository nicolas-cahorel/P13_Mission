package com.openclassrooms.hexagonal.games.screen.signInScreen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.openclassrooms.hexagonal.games.R

/**
 * SignInScreen composable function for displaying the sign-in screen UI.
 *
 * This screen allows users to input their email and password to sign in.
 * It shows the sign-in state (success or error) and manages interactions like
 * password visibility toggle, and navigation to other screens like password recovery or splash screen.
 *
 * @param viewModel The view model managing the state and logic of the sign-in screen.
 * @param navigateToHome Lambda function to navigate to the home screen upon successful sign-in.
 * @param navigateToPasswordRecovery Lambda function to navigate to the password recovery screen.
 * @param navigateToSplash Lambda function to navigate back to the splash screen.
 * @param email The email address of the user, displayed in the welcome message.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: SignInScreenViewModel,
    navigateToHome: () -> Unit,
    navigateToPasswordRecovery: () -> Unit,
    navigateToSplash: () -> Unit,
    email: String
) {
    val context = LocalContext.current
    val signInScreenState by viewModel.signInScreenState.collectAsState()
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(signInScreenState) {
        when (signInScreenState) {
            is SignInScreenState.SignInSuccess -> navigateToHome()
            is SignInScreenState.SignInError -> {
                Toast.makeText(
                    context,
                    R.string.error_unknown,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_signIn_topAppBar)) },
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
                text = stringResource(R.string.title_welcome, email),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { newValue ->
                    password = newValue
                    viewModel.onPasswordChanged(newValue.text)
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = when (signInScreenState) {
                            is SignInScreenState.InvalidInput -> stringResource(R.string.error_password_empty)
                            else -> stringResource(R.string.title_label_password)
                        }
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = if (signInScreenState is SignInScreenState.InvalidInput) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(id = if (isPasswordVisible) R.drawable.visibility_on_icon else R.drawable.visibility_off_icon),
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onButtonClicked(email, password.text) },
                enabled = signInScreenState is SignInScreenState.ValidInput,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.title_connect_button))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navigateToPasswordRecovery()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.title_help_button))
            }

        }
    }
}

//PREVIEW A FAIRE