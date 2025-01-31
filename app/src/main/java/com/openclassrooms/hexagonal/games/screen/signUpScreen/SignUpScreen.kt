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
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.screen.signInOrUpScreen.isInternetAvailable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    email: String,
    onSignUpSuccess: (Boolean) -> Unit,
    onBackButtonClicked: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val errorNameEmpty = stringResource(R.string.error_name_empty)
    val errorPasswordEmpty = stringResource(R.string.error_password_empty)
    val errorPasswordFormat = stringResource(R.string.error_password_format)
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var nameTextFieldLabel by remember { mutableStateOf(errorNameEmpty) }
    var passwordTextFieldLabel by remember { mutableStateOf(errorPasswordEmpty) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_signUp_topAppBar)) },
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
                onValueChange = {
                    name = it
                    if (name.text.isNotEmpty()) {
                        nameTextFieldLabel = ""
                        isButtonEnabled = true
                    } else {
                        nameTextFieldLabel = errorNameEmpty
                        isButtonEnabled = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(nameTextFieldLabel) },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = if (!isButtonEnabled) Color.Red else MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = if (isButtonEnabled) Color.Red else MaterialTheme.colorScheme.onSurface,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.title_password),
                style = MaterialTheme.typography.titleLarge
            )
            TextField(
                value = password,
                onValueChange = {
                    password = it
                    if (password.text.isNotEmpty()) {
                        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"
                        if (password.text.trim().matches(passwordPattern.toRegex())) {
                            passwordTextFieldLabel = ""
                            isButtonEnabled = true
                        } else {
                            passwordTextFieldLabel = errorPasswordFormat
                            isButtonEnabled = false
                        }
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
                    if (isInternetAvailable(context)) {
                        val firebaseAuth = FirebaseAuth.getInstance()
                        firebaseAuth.createUserWithEmailAndPassword(email, password.text)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    onSignUpSuccess(true)
                                } else {
                                    onSignUpSuccess(false)
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.toast_create_account_error),
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
                Text(stringResource(R.string.title_signUp_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen(
        email = "test@example.com",
        onSignUpSuccess = { _ -> },
        onBackButtonClicked = { _ -> }
    )
}