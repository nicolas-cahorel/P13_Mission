package com.openclassrooms.hexagonal.games.screen.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openclassrooms.hexagonal.games.screen.addPostScreen.AddPostScreen
import com.openclassrooms.hexagonal.games.screen.addPostScreen.AddPostScreenViewModel
import com.openclassrooms.hexagonal.games.screen.homeScreen.HomeScreen
import com.openclassrooms.hexagonal.games.screen.homeScreen.HomeScreenViewModel
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

/**
 * Manages the navigation between different screens in the application.
 * Uses Jetpack Navigation with HiltViewModel to handle dependencies.
 *
 * @param navController The navigation controller that manages the app navigation stack.
 */
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
                    navController.navigate(Routes.HomeScreen.route)
                },
                navigateToLoginOrSignUp = {
                    navController.navigate(Routes.SignInOrUpScreen.route)
                }
            )
        }

        composable(route = Routes.SignInOrUpScreen.route) {
            val signInOrUpScreenViewModel: SignInOrUpScreenViewModel = hiltViewModel()
            SignInOrUpScreen(
                viewModel = signInOrUpScreenViewModel,
                navigateToSignIn = { email ->
                    navController.navigate("SignInScreen/$email")
                },
                navigateToSignUp = { email ->
                    navController.navigate("SignUpScreen/$email")
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
                        navController.navigate(Routes.HomeScreen.route)
                    },
                    navigateToPasswordRecovery = {
                        navController.navigate("PasswordRecoveryScreen")
                    },
                    navigateToSplash = {
                        navController.navigate(Routes.SplashScreen.route)
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
                        navController.navigate(Routes.HomeScreen.route)
                    },
                    navigateToSplash = {
                        navController.navigate(Routes.SplashScreen.route)
                    }
                )
            }
        }

        composable(route = Routes.PasswordRecoveryScreen.route) {
            val passwordRecoveryScreenViewModel: PasswordRecoveryScreenViewModel = hiltViewModel()
            PasswordRecoveryScreen(
                viewModel = passwordRecoveryScreenViewModel,
                navigateToSplash = {
                    navController.navigate(Routes.SplashScreen.route)
                }
            )
        }

        composable(route = Routes.UserAccountScreen.route) {
            val userAccountScreenViewModel: UserAccountScreenViewModel = hiltViewModel()
            UserAccountScreen(
                viewModel = userAccountScreenViewModel,
                navigateToPrevious = { navController.navigateUp() },
                navigateToSplash = {
                    navController.navigate(Routes.SplashScreen.route)
                }
            )
        }

        composable(route = Routes.HomeScreen.route) {
            val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
            HomeScreen(
                viewModel = homeScreenViewModel,
                navigateToPostDetails = { postId ->
                    navController.navigate("PostDetailsScreen/$postId")
                },
                navigateToSettings = {
                    navController.navigate(Routes.SettingsScreen.route)
                },
                navigateToAdd = {
                    navController.navigate(Routes.AddPostScreen.route)
                },
                navigateToUserAccount = {
                    navController.navigate(Routes.UserAccountScreen.route)
                },
                navigateToSplash = {
                    navController.navigate(Routes.SplashScreen.route)
                }
            )
        }

        composable(route = Routes.AddPostScreen.route) {
            val addPostScreenViewModel: AddPostScreenViewModel = hiltViewModel()
            AddPostScreen(
                viewModel = addPostScreenViewModel,
                navigateToHome = {
                    navController.navigate(Routes.HomeScreen.route)
                }
            )
        }

        composable(route = Routes.SettingsScreen.route) {
            SettingsScreen(
                navigateToPrevious = { navController.navigateUp() }
            )
        }

        composable(route = Routes.PostDetailsScreen.route) { backStackEntry ->
            val postId =
                backStackEntry.arguments?.getString(Routes.PostDetailsScreen.ARGUMENT)
//            val postDetailsScreenViewModel: PostDetailsScreenViewModel = hiltViewModel()
//            if (!postId.isNullOrEmpty()) {
//                PostDetailsScreen(
//                    viewModel = postDetailsScreenViewModel,
//                    postId = postId,
//                    navigateToHome = {
//                        navController.navigate(Routes.HomeScreen.route) {
//                            popUpTo(Routes.SignUpScreen.route) { inclusive = true }
//                        }
//                    },
//                    navigateToSplash = {
//                        navController.navigate(Routes.SplashScreen.route) {
//                            popUpTo(Routes.SignUpScreen.route) { inclusive = true }
//                        }
//                    }
//                )
        }


    }
}
