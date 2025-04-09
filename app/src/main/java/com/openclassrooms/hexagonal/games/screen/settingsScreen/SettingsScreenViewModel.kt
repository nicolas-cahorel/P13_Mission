package com.openclassrooms.hexagonal.games.screen.settingsScreen

import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel responsible for managing user settings, specifically notification preferences.
 * It interacts with [SharedPreferences] to persist the user's notification settings.
 */
@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _settingsScreenState =
        MutableStateFlow<SettingsScreenState>(SettingsScreenState.NotificationsAreEnabled)
    val settingsScreenState: StateFlow<SettingsScreenState> get() = _settingsScreenState

    init {
        val isEnabled = sharedPreferences.getBoolean("notifications_enabled", false)
        _settingsScreenState.value = if (isEnabled) {
            SettingsScreenState.NotificationsAreEnabled
        } else {
            SettingsScreenState.NotificationAreDisabled
        }
    }

    /**
     * Enables notifications for the application.
     * If the device runs Android 13+ (API 33+), it requests the required permission before enabling notifications.
     *
     * @param notificationsPermissionState The permission state for posting notifications (required for Android 13+).
     */
    @OptIn(ExperimentalPermissionsApi::class)
    fun enableNotifications(notificationsPermissionState: PermissionState?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (notificationsPermissionState?.status?.isGranted == false) {
                notificationsPermissionState.launchPermissionRequest()
            }
        }

        sharedPreferences.edit { putBoolean("notifications_enabled", true) }
        _settingsScreenState.value = SettingsScreenState.NotificationsAreEnabled
    }


    /**
     * Disables notifications for the application.
     * Updates the shared preferences and modifies the screen state accordingly.
     */
    fun disableNotifications() {
        sharedPreferences.edit { putBoolean("notifications_enabled", false) }
        _settingsScreenState.value = SettingsScreenState.NotificationAreDisabled
    }
}
