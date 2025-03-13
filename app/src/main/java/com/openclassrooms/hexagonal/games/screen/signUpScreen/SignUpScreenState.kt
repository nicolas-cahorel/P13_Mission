package com.openclassrooms.hexagonal.games.screen.signUpScreen

/**
 * Sealed interface representing the different states of the SignUpScreen.
 *
 * This interface helps manage the different UI states during the user sign-up process.
 */
sealed interface SignUpScreenState {

    /**
     * Represents a valid input state, where the user has provided valid information.
     */
    data object ValidInput : SignUpScreenState

    /**
     * Represents an invalid input state, where the user has provided invalid information.
     *
     * @property isNameEmpty Indicates if the name field is empty.
     * @property isPasswordEmpty Indicates if the password field is empty.
     * @property isPasswordFormatCorrect Indicates if the password format is correct.
     */
    data class InvalidInput(
        val isNameEmpty: Boolean,
        val isPasswordEmpty: Boolean,
        val isPasswordFormatCorrect: Boolean
    ) : SignUpScreenState

    /**
     * Represents a successful sign-up state, meaning the user has successfully registered.
     */
    data object SignUpSuccess : SignUpScreenState

    /**
     * Represents a sign-up error state, indicating that an error occurred during the sign-up process.
     */
    data object SignUpError : SignUpScreenState

    /**
     * Represents a "no network" state, meaning the device is not connected to the internet.
     */
    data object NoNetwork : SignUpScreenState

}