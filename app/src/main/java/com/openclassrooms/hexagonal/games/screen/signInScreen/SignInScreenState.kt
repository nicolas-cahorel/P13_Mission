package com.openclassrooms.hexagonal.games.screen.signInScreen

sealed interface SignInScreenState {

    data class InvalidInput (val textFieldLabel: String): SignInScreenState

    data object ValidInput : SignInScreenState

    data object SignInSuccess : SignInScreenState

    data class SignInError (val message: String) : SignInScreenState





}