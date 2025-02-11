package com.openclassrooms.hexagonal.games.screen.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openclassrooms.hexagonal.games.screen.addScreen.AddScreen
import com.openclassrooms.hexagonal.games.screen.homeScreen.HomeScreen
import com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen.PasswordRecoveryScreen
import com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen.PasswordRecoveryScreenViewModel
import com.openclassrooms.hexagonal.games.screen.settingsScreen.SettingsScreen
import com.openclassrooms.hexagonal.games.screen.signInOrUpScreen.SignInOrUpScreen
import com.openclassrooms.hexagonal.games.screen.signInOrUpScreen.SignInOrUpScreenViewModel
import com.openclassrooms.hexagonal.games.screen.signInScreen.SignInScreen
import com.openclassrooms.hexagonal.games.screen.signInScreen.SignInScreenViewModel
import com.openclassrooms.hexagonal.games.screen.signUpScreen.SignUpScreen
import com.openclassrooms.hexagonal.games.screen.signUpScreen.SignUpScreenViewModel
import com.openclassrooms.hexagonal.games.screen.splashScreen.SplashScreen
import com.openclassrooms.hexagonal.games.screen.splashScreen.SplashScreenViewModel
import com.openclassrooms.hexagonal.games.screen.userAccountScreen.UserAccountScreen
import com.openclassrooms.hexagonal.games.screen.userAccountScreen.UserAccountScreenViewModel

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "SplashScreen"
    ) {

        composable(Routes.SplashScreen.route) {
            val splashScreenViewModel: SplashScreenViewModel = hiltViewModel()
            SplashScreen(
                viewModel = splashScreenViewModel,
                navigateToHome = {
                    navController.navigate(Routes.HomeScreen.route) {
                        popUpTo(Routes.SplashScreen.route) { inclusive = true }

                    }
                },
                navigateToLoginOrSignUp = {
                    navController.navigate(Routes.SignInOrUpScreen.route) {
                        popUpTo(Routes.SplashScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.SignInOrUpScreen.route) {
            val signInOrUpScreenViewModel: SignInOrUpScreenViewModel = hiltViewModel()
            SignInOrUpScreen(
                viewModel = signInOrUpScreenViewModel,
                navigateToSignIn = { email ->
                    navController.navigate("SignInScreen/$email") {
                        popUpTo(Routes.SignInOrUpScreen.route) { inclusive = true }
                    }
                },
                navigateToSignUp = { email ->
                    navController.navigate("SignUpScreen/$email") {
                        popUpTo(Routes.SignInOrUpScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.SignInScreen.route) { backStackEntry ->
            val email =
                backStackEntry.arguments?.getString(Routes.SignInScreen.ARGUMENT)
            val signInScreenViewModel: SignInScreenViewModel = hiltViewModel()
            if (!email.isNullOrEmpty()) {
                SignInScreen(
                    viewModel = signInScreenViewModel,
                    email = email,
                    navigateToHome = {
                        navController.navigate(Routes.HomeScreen.route) {
                            popUpTo(Routes.SignInScreen.route) { inclusive = true }

                        }
                    },
                    navigateToPasswordRecovery = {
                        navController.navigate("PasswordRecoveryScreen") {
                            popUpTo(Routes.SignInScreen.route) { inclusive = true }
                        }
                    },
                    navigateToSplash = {
                        navController.navigate(Routes.SplashScreen.route) {
                            popUpTo(Routes.SignInScreen.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(route = Routes.SignUpScreen.route) { backStackEntry ->
            val email =
                backStackEntry.arguments?.getString(Routes.SignUpScreen.ARGUMENT)
            val signUpScreenViewModel: SignUpScreenViewModel = hiltViewModel()
            if (!email.isNullOrEmpty()) {
                SignUpScreen(
                    viewModel = signUpScreenViewModel,
                    email = email,
                    navigateToHome = {
                            navController.navigate(Routes.HomeScreen.route) {
                                popUpTo(Routes.SignUpScreen.route) { inclusive = true }
                            }
                    },
                    navigateToSplash = {
                        navController.navigate(Routes.SplashScreen.route) {
                            popUpTo(Routes.SignUpScreen.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(route = Routes.PasswordRecoveryScreen.route) {
            val passwordRecoveryScreenViewModel: PasswordRecoveryScreenViewModel = hiltViewModel()
            PasswordRecoveryScreen(
                viewModel = passwordRecoveryScreenViewModel,
                navigateToSplash = {
                    navController.navigate(Routes.SplashScreen.route) {
                        popUpTo(Routes.PasswordRecoveryScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.UserAccountScreen.route) {
            val userAccountScreenViewModel: UserAccountScreenViewModel = hiltViewModel()
            UserAccountScreen(
                viewModel = userAccountScreenViewModel,
                navigateToPrevious = {
                    navController.popBackStack()
                },
                navigateToSplash = {
                        navController.navigate(Routes.SplashScreen.route) {
                            popUpTo(Routes.UserAccountScreen.route) { inclusive = true }
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
                navigateToPrevious = { navController.navigateUp() },
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
