package com.openclassrooms.hexagonal.games.screen.navigation

/**
 * Sealed class representing the different navigation routes in the application.
 * Each route is defined as an object with its corresponding path.
 *
 * @property route The string representation of the navigation route.
 */
sealed class Routes(val route: String) {

  /** Represents the splash screen route. */
  data object SplashScreen : Routes("SplashScreen")

  /** Represents the sign-in or sign-up selection screen route. */
  data object SignInOrUpScreen : Routes("SignInOrUpScreen")

  /**
   * Represents the sign-in screen route with an email argument.
   *
   * @property ARGUMENT The argument name for the email parameter.
   */
  data object SignInScreen : Routes("SignInScreen/{email}"){
    const val ARGUMENT = "email"
  }

  /**
   * Represents the sign-up screen route with an email argument.
   *
   * @property ARGUMENT The argument name for the email parameter.
   */
  data object SignUpScreen : Routes("SignUpScreen/{email}"){
    const val ARGUMENT = "email"
  }

  /** Represents the password recovery screen route. */
  data object PasswordRecoveryScreen : Routes("PasswordRecoveryScreen")

  /** Represents the user account screen route. */
  data object UserAccountScreen : Routes("UserAccountScreen")

  /** Represents the home screen route. */
  data object HomeScreen : Routes("HomeScreen")

  /** Represents the screen for adding a new post. */
  data object AddPostScreen : Routes("AddPostScreen")

  /** Represents the settings screen route. */
  data object SettingsScreen : Routes("SettingsScreen")

  /**
   * Represents the post details screen route with a post ID argument.
   *
   * @property ARGUMENT The argument name for the post ID parameter.
   */
  data object PostDetailsScreen : Routes("PostDetailsScreen/{postId}"){
    const val ARGUMENT = "postId"
  }

}