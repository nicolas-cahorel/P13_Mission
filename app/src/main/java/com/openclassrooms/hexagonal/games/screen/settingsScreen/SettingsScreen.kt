package com.openclassrooms.hexagonal.games.screen.settingsScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme

/**
 * Displays the settings screen of the app.
 *
 * This screen includes a top app bar with a back navigation icon and options
 * to enable or disable notifications.
 *
 * @param modifier Modifier to be applied to the screen layout.
 * @param settingsScreenState The current state of the settings screen.
 * @param onNotificationDisabledClicked Callback invoked when the user disables notifications.
 * @param onNotificationEnabledClicked Callback invoked when the user enables notifications.
 * @param navigateToHome Callback to navigate to the home screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsScreenState: SettingsScreenState,
    onNotificationDisabledClicked: () -> Unit,
    onNotificationEnabledClicked: () -> Unit,
    navigateToHome: () -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.title_settingsScreen))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navigateToHome()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.contentDescription_go_back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Settings(
            modifier = Modifier.padding(contentPadding),
            settingsScreenState = settingsScreenState,
            onNotificationDisabledClicked = { onNotificationDisabledClicked() },
            onNotificationEnabledClicked = { onNotificationEnabledClicked() }
        )
    }
}

/**
 * Displays the notification settings options.
 *
 * This function renders buttons for enabling and disabling notifications,
 * based on the current notification settings state.
 *
 * @param modifier Modifier to be applied to the settings UI.
 * @param settingsScreenState The current state of the notification settings.
 * @param onNotificationEnabledClicked Callback invoked when the enable notification button is clicked.
 * @param onNotificationDisabledClicked Callback invoked when the disable notification button is clicked.
 */
@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    settingsScreenState: SettingsScreenState,
    onNotificationEnabledClicked: () -> Unit,
    onNotificationDisabledClicked: () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Icon(
            modifier = modifier.size(200.dp),
            painter = painterResource(id = R.drawable.ic_notifications),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = stringResource(id = R.string.contentDescription_notification_icon)
        )

        Button(
            enabled = settingsScreenState is SettingsScreenState.NotificationAreDisabled,
            onClick = {
                onNotificationEnabledClicked()
                Toast.makeText(context, R.string.toast_notification_enabled, Toast.LENGTH_SHORT)
                    .show()
            }
        ) {
            Text(text = stringResource(id = R.string.title_notification_enable))
        }

        Button(
            enabled = settingsScreenState is SettingsScreenState.NotificationsAreEnabled,
            onClick = {
                onNotificationDisabledClicked()
                Toast.makeText(context, R.string.toast_notification_disabled, Toast.LENGTH_SHORT)
                    .show()
            }
        ) {
            Text(text = stringResource(id = R.string.title_notification_disable))
        }
    }
}

/**
 * Preview for [SettingsScreen].
 */
@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    HexagonalGamesTheme {

        SettingsScreen(
            modifier = Modifier.fillMaxSize(),
            settingsScreenState = SettingsScreenState.NotificationsAreEnabled,
            onNotificationEnabledClicked = {},
            onNotificationDisabledClicked = {},
            navigateToHome = {}
        )
    }
}

/**
 * Preview for [SettingsScreen].
 */
@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview2() {
    HexagonalGamesTheme {

        SettingsScreen(
            modifier = Modifier.fillMaxSize(),
            settingsScreenState = SettingsScreenState.NotificationAreDisabled,
            onNotificationEnabledClicked = {},
            onNotificationDisabledClicked = {},
            navigateToHome = {}
        )
    }
}

