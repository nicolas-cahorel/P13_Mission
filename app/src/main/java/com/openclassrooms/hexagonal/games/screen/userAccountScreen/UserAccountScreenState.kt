package com.openclassrooms.hexagonal.games.screen.userAccountScreen

sealed interface UserAccountScreenState {

    data object OnStart : UserAccountScreenState

    data object UserSignedOut : UserAccountScreenState

    data object UserDeleted : UserAccountScreenState

    data class Error (val message: String) : UserAccountScreenState

}