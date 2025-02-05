package com.openclassrooms.hexagonal.games.screen.signUpScreen

sealed interface SignUpScreenState {

    data class InvalidInput (val nameTextFieldLabel: String, val passwordTextFieldLabel: String, val isNameValid: Boolean, val isPasswordValid: Boolean): SignUpScreenState

    data object ValidInput : SignUpScreenState

    data object SignUpSuccess : SignUpScreenState

    data class SignUpError (val message: String) : SignUpScreenState





}