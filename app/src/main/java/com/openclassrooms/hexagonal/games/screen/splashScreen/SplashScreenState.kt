package com.openclassrooms.hexagonal.games.screen.splashScreen

/**
 * Sealed interface representing the possible states of the splash screen.
 *
 * This interface defines the different states the splash screen can be in based on whether
 * the user is logged in or not. It helps manage the UI state and decide which screen to
 * navigate to (login/signup or home).
 */
sealed interface SplashScreenState {

    /**
     * Represents the state when the user is logged in.
     */
    data object UserIsLoggedIn : SplashScreenState

    /**
     * Represents the state when the user is not logged in.
     */
    data object UserIsNotLoggedIn : SplashScreenState

}