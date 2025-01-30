package com.openclassrooms.hexagonal.games.screen.navigation

import androidx.navigation.NamedNavArgument

sealed class Routes(
  val route: String,
  val navArguments: List<NamedNavArgument> = emptyList()
) {
  data object LoginScreen : Routes("LoginScreen")

  data object SignInOrUpScreen : Routes("SignInOrUpScreen")

  data object SignInScreen : Routes("SignInScreen/{email}"){
    const val ARGUMENT = "email"
  }

  data object SignUpScreen : Routes("SignUpScreen/{email}"){
    const val ARGUMENT = "email"
  }

  data object PasswordRecoveryScreen : Routes("PasswordRecoveryScreen")

  data object UserAccountScreen : Routes("UserAccountScreen")

  data object HomeScreen : Routes("HomeScreen")

  data object AddScreen : Routes("AddScreen")
  
  data object SettingsScreen : Routes("SettingsScreen")
}