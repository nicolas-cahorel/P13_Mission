package com.openclassrooms.hexagonal.games.screen.settingsScreen

import android.os.Build
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme

/**
 * SettingsScreen composable that displays the settings screen of the app.
 *
 * This screen consists of a top app bar with a back navigation icon, and settings options
 * for enabling or disabling notifications.
 *
 * @param modifier The modifier to be applied to the screen.
 * @param viewModel The view model for managing settings logic.
 * @param navigateToPrevious Function to navigate to the previous screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToPrevious: () -> Unit
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          Text(stringResource(id = R.string.title_settingsScreen))
        },
        navigationIcon = {
          IconButton(onClick = {
            navigateToPrevious()
          }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = stringResource(id = R.string.contentDescription_go_back)
            )
          }
        }
      )
    }
  ) { contentPadding ->
    Settings(
      modifier = Modifier.padding(contentPadding),
      onNotificationDisabledClicked = { viewModel.disableNotifications() },
      onNotificationEnabledClicked = {
        viewModel.enableNotifications()
      }
    )
  }
}

/**
 * Composable function that handles the settings options for enabling or disabling notifications.
 *
 * It checks the permission status for posting notifications on devices running Android 13 and above.
 * It displays buttons for enabling and disabling notifications based on the permission status.
 *
 * @param modifier The modifier to be applied to the settings UI.
 * @param onNotificationEnabledClicked Lambda to be invoked when the enable notification button is clicked.
 * @param onNotificationDisabledClicked Lambda to be invoked when the disable notification button is clicked.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Settings(
  modifier: Modifier = Modifier,
  onNotificationEnabledClicked: () -> Unit,
  onNotificationDisabledClicked: () -> Unit
) {
  val notificationsPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    rememberPermissionState(
      android.Manifest.permission.POST_NOTIFICATIONS
    )
  } else {
    null
  }
  
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
      onClick = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          if (notificationsPermissionState?.status?.isGranted == false) {
            notificationsPermissionState.launchPermissionRequest()
          }
        }
        
        onNotificationEnabledClicked()
      }
    ) {
      Text(text = stringResource(id = R.string.title_notification_enable))
    }
    Button(
      onClick = { onNotificationDisabledClicked() }
    ) {
      Text(text = stringResource(id = R.string.title_notification_disable))
    }
  }
}

// PREVIEWS
@Preview
@Composable
private fun SettingsPreview() {
  HexagonalGamesTheme {
    Settings(
      onNotificationEnabledClicked = { },
      onNotificationDisabledClicked = { }
    )
  }
}