package com.openclassrooms.hexagonal.games.screen.signUpScreen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
 * Composable function for the Sign-Up Screen UI.
 *
 * This screen allows users to sign up by providing a name and password. It also includes validation for
 * the inputs and shows appropriate error messages when the inputs are invalid. The user will be navigated
 * to the home screen upon successful sign-up or shown an error message if the sign-up fails.
 *
 * @param viewModel The ViewModel to manage the state and actions for sign-up.
 * @param navigateToHome Lambda function to navigate to the home screen on successful sign-up.
 * @param navigateToSplash Lambda function to navigate back to the splash screen.
 * @param email The email that has already been entered and will be displayed in a non-editable TextField.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpScreenViewModel,
    navigateToHome: () -> Unit,
    navigateToSplash: () -> Unit,
    email: String
) {
    val context = LocalContext.current
    val signUpScreenState by viewModel.signUpScreenState.collectAsState()
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Effect to navigate or show error based on sign-up state
    LaunchedEffect(signUpScreenState) {
        when (signUpScreenState) {
            is SignUpScreenState.SignUpSuccess -> navigateToHome()
            is SignUpScreenState.SignUpError -> {
                Toast.makeText(
                    context,
                    R.string.toast_create_account_error,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }

    // Scaffold provides basic structure for the layout, including the top bar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_signUp_topAppBar)) },
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
                text = stringResource(R.string.title_email),
                style = MaterialTheme.typography.titleLarge
            )
            TextField(
                value = email,
                onValueChange = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.title_name),
                style = MaterialTheme.typography.titleLarge
            )
            TextField(
                value = name,

                onValueChange = { newValue ->
                    name = newValue
                    viewModel.onNameChanged(newValue.text)
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = when {
                            signUpScreenState is SignUpScreenState.InvalidInput && (signUpScreenState as SignUpScreenState.InvalidInput).isNameEmpty ->
                                stringResource(R.string.error_name_empty)

                            else -> ""
                        }
                    )
                },

                colors = TextFieldDefaults.colors(
                    focusedLabelColor = when {
                        signUpScreenState is SignUpScreenState.InvalidInput && (signUpScreenState as SignUpScreenState.InvalidInput).isNameEmpty ->
                            MaterialTheme.colorScheme.error

                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.title_password),
                style = MaterialTheme.typography.titleLarge
            )
            TextField(
                value = password,
                onValueChange = { newValue ->
                    password = newValue
                    viewModel.onPasswordChanged(newValue.text)
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = when {
                            signUpScreenState is SignUpScreenState.InvalidInput && (signUpScreenState as SignUpScreenState.InvalidInput).isPasswordEmpty ->
                                stringResource(R.string.error_password_empty)

                            signUpScreenState is SignUpScreenState.InvalidInput && !(signUpScreenState as SignUpScreenState.InvalidInput).isPasswordFormatCorrect ->
                                stringResource(R.string.error_password_format)

                            else -> ""
                        }
                    )
                },
                colors = TextFieldDefaults.colors(


                    focusedLabelColor = when (signUpScreenState) {
                        is SignUpScreenState.InvalidInput ->
                            when {
                                (signUpScreenState as SignUpScreenState.InvalidInput).isPasswordEmpty -> MaterialTheme.colorScheme.error
                                !(signUpScreenState as SignUpScreenState.InvalidInput).isPasswordFormatCorrect -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.primary
                            }

                        else -> MaterialTheme.colorScheme.primary
                    }
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
                onClick = { viewModel.onButtonClicked(email) },
                enabled = signUpScreenState is SignUpScreenState.ValidInput,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.title_signUp_button))
            }
        }
    }
}

//PREVIEW A FAIRE