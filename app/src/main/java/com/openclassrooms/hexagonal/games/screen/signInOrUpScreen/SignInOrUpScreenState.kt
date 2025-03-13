package com.openclassrooms.hexagonal.games.screen.signInOrUpScreen

/**
 * Sealed interface representing the different states of the SignInOrUpScreen.
 *
 * This interface is used to represent the various possible states of the screen during the
 * sign-in or sign-up process. Each state corresponds to a specific situation (e.g., invalid input,
 * existing account, network error, etc.).
 */
sealed interface SignInOrUpScreenState {

    /**
     * State representing an invalid input (e.g., an invalid email format).
     */
    data object InvalidInput : SignInOrUpScreenState

    /**
     * State representing a valid input (e.g., a correctly formatted email).
     */
    data object ValidInput : SignInOrUpScreenState

    /**
     * State representing the scenario where an account already exists with the provided email.
     */
    data object AccountExists : SignInOrUpScreenState

    /**
     * State representing the scenario where an account does not exist for the provided email.
     */
    data object AccountDoNotExists : SignInOrUpScreenState

    /**
     * State representing an error occurred during the sign-in or sign-up process.
     * This can be used for general error handling (e.g., server error).
     */
    data object Error : SignInOrUpScreenState

    /**
     * State representing the scenario where there is no network connection.
     * This can be used to display a message when the device is offline.
     */
    data object NoNetwork : SignInOrUpScreenState

}