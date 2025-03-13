package com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen

/**
 * Represents the different states of the password recovery screen.
 *
 * This sealed interface defines various possible states that the UI can be in,
 * depending on user input and the password recovery process.
 */
sealed interface PasswordRecoveryScreenState {

    /**
     * Represents a state where the email input is valid.
     * The recovery process can proceed.
     */
    data object ValidInput : PasswordRecoveryScreenState

    /**
     * Represents a state where the email input is invalid.
     * This could be due to an incorrect email format.
     */
    data object InvalidInput : PasswordRecoveryScreenState

    /**
     * Represents a state where the email input is empty.
     * The user must enter an email address to proceed.
     */
    data object EmptyInput : PasswordRecoveryScreenState

    /**
     * Represents a state where a confirmation dialog should be displayed.
     * This typically happens after a successful password recovery request.
     */
    data object ShowDialog : PasswordRecoveryScreenState

    /**
     * Represents a state where an error has occurred.
     * This could be due to a network issue or an unexpected failure.
     */
    data object Error : PasswordRecoveryScreenState

}