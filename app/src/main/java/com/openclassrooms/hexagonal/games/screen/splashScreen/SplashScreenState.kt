package com.openclassrooms.hexagonal.games.screen.splashScreen

sealed interface SplashScreenState {

    data object UserIsLoggedIn : SplashScreenState

    data object UserIsNotLoggedIn : SplashScreenState


}