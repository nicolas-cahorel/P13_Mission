package com.openclassrooms.hexagonal.games.screen.addScreen

sealed interface AddScreenState {

    data class InvalidInput (val titleTextFieldLabel: String, val passwordTextFieldLabel: String, val isTitleValid: Boolean, val isPasswordValid: Boolean): AddScreenState

    data object ValidInput : AddScreenState

    data object SignUpSuccess : AddScreenState

    data class SignUpError (val message: String) : AddScreenState





}