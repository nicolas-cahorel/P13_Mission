package com.openclassrooms.hexagonal.games.screen.userAccountScreen

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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.openclassrooms.hexagonal.games.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountScreen(
    viewModel: UserAccountScreenViewModel,
    navigateToPrevious: () -> Unit,
    navigateToSplash: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.userAccountScreenState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is UserAccountScreenState.UserDeleted -> navigateToSplash()
            is UserAccountScreenState.UserSignedOut -> navigateToSplash()
                is UserAccountScreenState.Error -> {
                    Toast.makeText(
                        context,
                        (uiState as UserAccountScreenState.Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Unit
            }
        }

        Scaffold(

            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.title_userAccount_topAppBar)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navigateToPrevious() }) {
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

                Button(
                    onClick = { viewModel.onSignOutButtonClicked() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.title_signOut_button))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.onDeleteButtonClicked() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.title_deleteAccount_button))
                }
            }
        }
    }