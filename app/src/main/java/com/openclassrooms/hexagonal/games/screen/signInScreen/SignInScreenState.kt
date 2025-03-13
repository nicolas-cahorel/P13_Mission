package com.openclassrooms.hexagonal.games.screen.signInScreen

/**
 * Sealed interface representing the different states of the SignInScreen.
 *
 * This sealed interface is used to manage the UI state of the Sign-In screen based on
 * user input, sign-in success, or error.
 */
sealed interface SignInScreenState {

    /**
     * Represents a state where the user input is valid.
     *
     * This state is used when the user has entered a valid input (e.g., valid email/password).
     */
    data object ValidInput : SignInScreenState

    /**
     * Represents a state where the user input is invalid.
     *
     * This state is used when the user input is invalid (e.g., empty fields or incorrect format).
     */
    data object InvalidInput : SignInScreenState

    /**
     * Represents a state where the sign-in operation was successful.
     *
     * This state is used when the sign-in operation completes successfully and the user can be redirected to the home screen.
     */
    data object SignInSuccess : SignInScreenState

    /**
     * Represents a state where the sign-in operation failed.
     *
     * This state is used when the sign-in operation encounters an error (e.g., incorrect credentials).
     */
    data object SignInError : SignInScreenState

}