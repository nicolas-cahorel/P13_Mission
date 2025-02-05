package com.openclassrooms.hexagonal.games.screen.signInOrUpScreen

sealed interface SignInOrUpScreenState {

    data class InvalidInput (val textFieldLabel: String,val isButtonEnabled: Boolean): SignInOrUpScreenState

    data class ValidInput (val textFieldLabel: String,val isButtonEnabled: Boolean): SignInOrUpScreenState

    data object AccountExists : SignInOrUpScreenState

    data object AccountDoNotExists : SignInOrUpScreenState

    data class Error (val message: String) : SignInOrUpScreenState





}