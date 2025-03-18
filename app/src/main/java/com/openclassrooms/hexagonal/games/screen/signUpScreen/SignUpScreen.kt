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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.hexagonal.games.R

/**
 * Displays the sign-up screen UI.
 *
 * This composable function allows users to sign up by providing their name and password.
 * It includes input validation and displays appropriate error messages for invalid inputs.
 * Upon successful sign-up, the user is navigated to the home screen, while sign-up failures
 * trigger an error message.
 *
 * @param signUpScreenState The current state of the sign-up screen, including UI state and validation status.
 * @param email The pre-entered email address, displayed in a non-editable TextField.
 * @param onNameChanged A lambda function invoked when the user changes their name.
 * @param onPasswordChanged A lambda function invoked when the user changes their password.
 * @param onButtonClicked A lambda function invoked when the sign-up button is clicked, passing the email.
 * @param navigateToHome A lambda function invoked to navigate to the home screen upon successful sign-up.
 * @param navigateToSplash A lambda function invoked to navigate back to the splash screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    signUpScreenState: SignUpScreenState,
    email: String,
    onNameChanged: (name: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onButtonClicked: (userEmail: String) -> Unit,
    navigateToHome: () -> Unit,
    navigateToSplash: () -> Unit
) {
    val context = LocalContext.current
    val labelNameText = when {
        signUpScreenState is SignUpScreenState.InvalidInput && signUpScreenState.isNameEmpty ->
            stringResource(R.string.error_name_empty)

        else -> ""
    }
    val labelPasswordText = when {
        signUpScreenState is SignUpScreenState.InvalidInput && signUpScreenState.isPasswordEmpty ->
            stringResource(R.string.error_password_empty)

        signUpScreenState is SignUpScreenState.InvalidInput && !signUpScreenState.isPasswordFormatCorrect ->
            stringResource(R.string.error_password_format)

        else -> ""
    }
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

            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("") },
                colors = TextFieldDefaults.colors(disabledIndicatorColor = Color.Transparent),
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.title_name),
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = name,
                onValueChange = { newValue ->
                    name = newValue
                    onNameChanged(newValue.text)
                },
                label = { Text(text = labelNameText) },
                isError = signUpScreenState is SignUpScreenState.InvalidInput && signUpScreenState.isNameEmpty,
                colors = TextFieldDefaults.colors(focusedLabelColor = MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.title_password),
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = password,
                onValueChange = { newValue ->
                    password = newValue
                    onPasswordChanged(newValue.text)
                },
                isError = signUpScreenState is SignUpScreenState.InvalidInput &&
                        (signUpScreenState.isPasswordEmpty || !signUpScreenState.isPasswordFormatCorrect),
                label = { Text(text = labelPasswordText) },
                colors = TextFieldDefaults.colors(focusedLabelColor = MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(id = if (isPasswordVisible) R.drawable.visibility_on_icon else R.drawable.visibility_off_icon),
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onButtonClicked(email) },
                enabled = signUpScreenState is SignUpScreenState.ValidInput,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.title_signUp_button))
            }
        }
    }
}

/**
 * Preview for [SignUpScreen].
 */
@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen(
        signUpScreenState = SignUpScreenState.ValidInput,
        email = "test@example.com",
        onNameChanged = { },
        onPasswordChanged = { },
        onButtonClicked = { },
        navigateToHome = { },
        navigateToSplash = { }
    )
}

/**
 * Preview for [SignUpScreen].
 */
@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreenInvalidInput() {
    SignUpScreen(
        signUpScreenState = SignUpScreenState.InvalidInput(
            isNameEmpty = false,
            isPasswordEmpty = true,
            isPasswordFormatCorrect = false
        ),
        email = "test@example.com",
        onNameChanged = { },
        onPasswordChanged = { },
        onButtonClicked = { },
        navigateToHome = { },
        navigateToSplash = { }
    )
}