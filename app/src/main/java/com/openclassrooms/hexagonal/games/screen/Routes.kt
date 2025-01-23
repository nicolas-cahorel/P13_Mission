package com.openclassrooms.hexagonal.games.screen

import androidx.navigation.NamedNavArgument

sealed class Routes(
  val route: String,
  val navArguments: List<NamedNavArgument> = emptyList()
) {
  data object LoginScreen : Routes("LoginScreen")

  data object SignInOrUpScreen : Routes("SignInOrUpScreen")

  data object HomeScreen : Routes("HomeScreen")

  data object AddScreen : Routes("AddScreen")
  
  data object SettingsScreen : Routes("SettingsScreen")
}