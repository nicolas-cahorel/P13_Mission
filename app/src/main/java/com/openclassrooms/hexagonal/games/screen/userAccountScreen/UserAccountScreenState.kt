package com.openclassrooms.hexagonal.games.screen.userAccountScreen

/**
 * Sealed interface representing the different states of the UserAccountScreen.
 *
 * This sealed interface defines various states that represent the possible outcomes
 * of user account actions such as signing out or deleting the account. The states
 * can be used to update the UI and trigger navigation or display error messages.
 */
sealed interface UserAccountScreenState {

    /**
     * State representing the initial state of the user account screen.
     * Typically used when the screen is loading or waiting for user input.
     */
    data object OnStart : UserAccountScreenState

    /**
     * State indicating the user has successfully signed out.
     * This state is triggered after a successful sign-out operation.
     */
    data object SignOutUserSuccess : UserAccountScreenState

    /**
     * State indicating an error occurred during the sign-out process.
     * This state is triggered when there is an issue signing out the user.
     */
    data object SignOutUserError : UserAccountScreenState

    /**
     * State indicating the user has successfully deleted their account.
     * This state is triggered after a successful account deletion operation.
     */
    data object DeleteUserSuccess : UserAccountScreenState

    /**
     * State indicating an error occurred during the account deletion process.
     * This state is triggered when there is an issue deleting the user's account.
     */
    data object DeleteUserError : UserAccountScreenState

}