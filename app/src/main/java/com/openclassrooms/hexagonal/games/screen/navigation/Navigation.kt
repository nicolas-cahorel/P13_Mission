package com.openclassrooms.hexagonal.games.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openclassrooms.hexagonal.games.screen.addScreen.AddScreen
import com.openclassrooms.hexagonal.games.screen.homeScreen.HomeScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.LoginScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.PasswordRecoveryScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.SignInOrUpScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.SignInScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.SignUpScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.UserAccountScreen
import com.openclassrooms.hexagonal.games.screen.settingsScreen.SettingsScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "LoginScreen"
    ) {

        composable(Routes.LoginScreen.route) {
            LoginScreen(
                onUserLoggedIn = { user ->
                    if (user != null) {
                        navController.navigate(Routes.HomeScreen.route) {
                            popUpTo(Routes.LoginScreen.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.SignInOrUpScreen.route) {
                            popUpTo(Routes.LoginScreen.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(route = Routes.SignInOrUpScreen.route) {
            SignInOrUpScreen(
                onEmailEntered = { accountExists, email ->
                    if (accountExists) {
                        navController.navigate("SignInScreen/$email") {
                            popUpTo(Routes.SignInOrUpScreen.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate("SignUpScreen/$email") {
                            popUpTo(Routes.SignInOrUpScreen.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(route = Routes.SignInScreen.route) { backStackEntry ->
            val email =
                backStackEntry.arguments?.getString(Routes.SignInScreen.ARGUMENT)
            if (!email.isNullOrEmpty()) {
                SignInScreen(
                    email = email,
                    onSignInSuccess = { isSignInSuccessful ->
                        if (isSignInSuccessful) {
                            navController.navigate(Routes.HomeScreen.route) {
                                popUpTo(Routes.SignInScreen.route) { inclusive = true }
                            }
                        }
                    },
                    onHelpClicked = {
                        navController.navigate("PasswordRecoveryScreen") {
                            popUpTo(Routes.SignInScreen.route) { inclusive = true }
                        }
                    },
                    onBackButtonClicked = {
                        navController.navigate(Routes.LoginScreen.route) {
                            popUpTo(Routes.SignInScreen.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(route = Routes.SignUpScreen.route) { backStackEntry ->
            val email =
                backStackEntry.arguments?.getString(Routes.SignUpScreen.ARGUMENT)
            if (!email.isNullOrEmpty()) {
                SignUpScreen(
                    email = email,
                    onSignUpSuccess = { isSignUpSuccessful ->
                        if (isSignUpSuccessful) {
                            navController.navigate(Routes.HomeScreen.route) {
                                popUpTo(Routes.SignInScreen.route) { inclusive = true }
                            }
                        }
                    },
                    onBackButtonClicked = {
                        navController.navigate(Routes.LoginScreen.route) {
                            popUpTo(Routes.SignUpScreen.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(route = Routes.PasswordRecoveryScreen.route) {
            PasswordRecoveryScreen(
                onBackButtonClicked = {
                    navController.navigate(Routes.LoginScreen.route) {
                        popUpTo(Routes.PasswordRecoveryScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.UserAccountScreen.route) {
            UserAccountScreen(
                onBackButtonClicked = {
                    navController.popBackStack()
                },
                onSignOut = { isSignedOut ->
                    if (isSignedOut) {
                        navController.navigate(Routes.LoginScreen.route) {
                            popUpTo(Routes.UserAccountScreen.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(route = Routes.HomeScreen.route) {
            HomeScreen(
                onPostClick = {
                    //TODO
                },
                onSettingsClick = {
                    navController.navigate(Routes.SettingsScreen.route)
                },
                onFABClick = {
                    navController.navigate(Routes.AddScreen.route)
                }
            )
        }

        composable(route = Routes.AddScreen.route) {
            AddScreen(
                onBackClick = { navController.navigateUp() },
                onSaveClick = { navController.navigateUp() }
            )
        }

        composable(route = Routes.SettingsScreen.route) {
            SettingsScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
