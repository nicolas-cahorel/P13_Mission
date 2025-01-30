package com.openclassrooms.hexagonal.games.screen.loginScreens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.openclassrooms.hexagonal.games.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onSignInSuccess: (Boolean) -> Unit,
    onHelpClicked: () -> Unit,
    onBackButtonClicked: (Boolean) -> Unit,
    email: String
) {
    val context = LocalContext.current
    val errorPasswordEmpty = stringResource(R.string.error_password_empty)
    val labelPassword = stringResource(R.string.title_label_password)
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var passwordTextFieldLabel by remember { mutableStateOf(errorPasswordEmpty) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_signIn_topAppBar)) },
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
                text = stringResource(R.string.title_welcome, email),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    if (password.text.isNotEmpty()) {
                        passwordTextFieldLabel = labelPassword
                        isButtonEnabled = true
                    } else {
                        passwordTextFieldLabel = errorPasswordEmpty
                        isButtonEnabled = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(passwordTextFieldLabel) },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = if (!isButtonEnabled) Color.Red else MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = if (isButtonEnabled) Color.Red else MaterialTheme.colorScheme.onSurface,
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
                onClick = {
                    val auth = FirebaseAuth.getInstance()
                    auth.signInWithEmailAndPassword(email, password.text)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onSignInSuccess(true)
                            } else {
                                val exception = task.exception
                                if (exception is FirebaseAuthException && exception.errorCode == "ERROR_WRONG_PASSWORD") {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.error_password_incorrect),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.error_unknown),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        }
                },
                enabled = isButtonEnabled,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.title_connect_button))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onHelpClicked()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.title_help_button))
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    SignInScreen(
        onSignInSuccess = { _ -> },
        onHelpClicked = {},
        onBackButtonClicked = { _ -> },
        email = "test@example.com"
    )
}