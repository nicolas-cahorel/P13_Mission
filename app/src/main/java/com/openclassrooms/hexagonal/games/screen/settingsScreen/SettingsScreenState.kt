package com.openclassrooms.hexagonal.games.screen.settingsScreen

/**
 * Represents the state of the settings screen, specifically related to notification preferences.
 */
sealed interface SettingsScreenState {

    /**
     * State representing that notifications are enabled.
     */
    data object NotificationsAreEnabled : SettingsScreenState

    /**
     * State representing that notifications are disabled.
     */
    data object NotificationAreDisabled : SettingsScreenState

}