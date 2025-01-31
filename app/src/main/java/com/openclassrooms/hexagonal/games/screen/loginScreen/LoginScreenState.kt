package com.openclassrooms.hexagonal.games.screen.loginScreen

sealed interface LoginScreenState {

    data object UserIsLoggedIn : LoginScreenState

    data object UserIsNotLoggedIn : LoginScreenState


}