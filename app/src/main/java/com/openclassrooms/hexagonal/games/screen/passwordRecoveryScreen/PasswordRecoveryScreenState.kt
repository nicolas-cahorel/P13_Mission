package com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen

sealed interface PasswordRecoveryScreenState {

    data class InvalidInput (val textFieldLabel: String): PasswordRecoveryScreenState

    data object ValidInput : PasswordRecoveryScreenState

    data object ShowDialog : PasswordRecoveryScreenState

    data class Error (val message: String) : PasswordRecoveryScreenState





}