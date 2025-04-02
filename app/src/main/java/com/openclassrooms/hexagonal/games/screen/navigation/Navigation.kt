package com.openclassrooms.hexagonal.games.screen.navigation

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.openclassrooms.hexagonal.games.screen.addCommentScreen.AddCommentScreen
import com.openclassrooms.hexagonal.games.screen.addCommentScreen.AddCommentScreenViewModel
import com.openclassrooms.hexagonal.games.screen.addPostScreen.AddPostScreen
import com.openclassrooms.hexagonal.games.screen.addPostScreen.AddPostScreenViewModel
import com.openclassrooms.hexagonal.games.screen.addPostScreen.FormEvent
import com.openclassrooms.hexagonal.games.screen.homeScreen.HomeScreen
import com.openclassrooms.hexagonal.games.screen.homeScreen.HomeScreenViewModel
import com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen.PasswordRecoveryScreen
import com.openclassrooms.hexagonal.games.screen.passwordRecoveryScreen.PasswordRecoveryScreenViewModel
import com.openclassrooms.hexagonal.games.screen.postDetailsScreen.PostDetailsScreen
import com.openclassrooms.hexagonal.games.screen.postDetailsScreen.PostDetailsScreenViewModel
import com.openclassrooms.hexagonal.games.screen.settingsScreen.SettingsScreen
import com.openclassrooms.hexagonal.games.screen.settingsScreen.SettingsScreenViewModel
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
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "SplashScreen"
    ) {

        composable(Routes.SplashScreen.route) {
            val splashScreenViewModel: SplashScreenViewModel = hiltViewModel()
            val splashScreenState by splashScreenViewModel.splashScreenState.collectAsState()
            SplashScreen(
                splashScreenState = splashScreenState,
                navigateToHome = { navController.navigate(Routes.HomeScreen.route) },
                navigateToLoginOrSignUp = { navController.navigate(Routes.SignInOrUpScreen.route) }
            )
        }

        composable(route = Routes.SignInOrUpScreen.route) {
            val signInOrUpScreenViewModel: SignInOrUpScreenViewModel = hiltViewModel()
            val signInOrUpScreenState by signInOrUpScreenViewModel.signInOrUpScreenState.collectAsState()
            SignInOrUpScreen(
                signInOrUpScreenState = signInOrUpScreenState,
                onEmailChanged = { email -> signInOrUpScreenViewModel.onEmailChanged(email) },
                onButtonClicked = { email -> signInOrUpScreenViewModel.onButtonClicked(email) },
                navigateToSignIn = { email -> navController.navigate("SignInScreen/$email") },
                navigateToSignUp = { email -> navController.navigate("SignUpScreen/$email") }
            )
        }

        composable(route = Routes.SignInScreen.route) { backStackEntry ->
            val email =
                backStackEntry.arguments?.getString(Routes.SignInScreen.ARGUMENT)
            val signInScreenViewModel: SignInScreenViewModel = hiltViewModel()
            val signInScreenState by signInScreenViewModel.signInScreenState.collectAsState()
            if (!email.isNullOrEmpty()) {
                SignInScreen(
                    signInScreenState = signInScreenState,
                    email = email,
                    onPasswordChanged = { password -> signInScreenViewModel.onPasswordChanged(password) },
                    onButtonClicked = { userEmail, password -> signInScreenViewModel.onButtonClicked(userEmail, password) },
                    navigateToHome = { navController.navigate(Routes.HomeScreen.route) },
                    navigateToPasswordRecovery = { navController.navigate("PasswordRecoveryScreen") },
                    navigateToSplash = { navController.navigate(Routes.SplashScreen.route) }
                )
            }
        }

        composable(route = Routes.SignUpScreen.route) { backStackEntry ->
            val email =
                backStackEntry.arguments?.getString(Routes.SignUpScreen.ARGUMENT)
            val signUpScreenViewModel: SignUpScreenViewModel = hiltViewModel()
            val signUpScreenState by signUpScreenViewModel.signUpScreenState.collectAsState()
            if (!email.isNullOrEmpty()) {
                SignUpScreen(
                    signUpScreenState = signUpScreenState,
                    email = email,
                    onNameChanged = { name -> signUpScreenViewModel.onNameChanged(name) },
                    onPasswordChanged = { password -> signUpScreenViewModel.onPasswordChanged(password) },
                    onButtonClicked = { userEmail -> signUpScreenViewModel.onButtonClicked(userEmail) },
                    navigateToHome = { navController.navigate(Routes.HomeScreen.route) },
                    navigateToSplash = { navController.navigate(Routes.SplashScreen.route) }
                )
            }
        }

        composable(route = Routes.PasswordRecoveryScreen.route) {
            val passwordRecoveryScreenViewModel: PasswordRecoveryScreenViewModel = hiltViewModel()
            val passwordRecoveryScreenState by passwordRecoveryScreenViewModel.passwordRecoveryScreenState.collectAsState()
            PasswordRecoveryScreen(
                passwordRecoveryScreenState = passwordRecoveryScreenState,
                onEmailChanged = { email -> passwordRecoveryScreenViewModel.onEmailChanged(email) },
                onButtonClicked = { email -> passwordRecoveryScreenViewModel.onButtonClicked(email) },
                onDialogButtonClicked = { passwordRecoveryScreenViewModel.onDialogButtonClicked() },
                navigateToSplash = { navController.navigate(Routes.SplashScreen.route) }
            )
        }

        composable(route = Routes.UserAccountScreen.route) {
            val userAccountScreenViewModel: UserAccountScreenViewModel = hiltViewModel()
            val userAccountScreenState by userAccountScreenViewModel.userAccountScreenState.collectAsState()
            UserAccountScreen(
                userAccountScreenState = userAccountScreenState,
                onSignOutButtonClicked = { userAccountScreenViewModel.onSignOutButtonClicked() },
                onDeleteButtonClicked = { userAccountScreenViewModel.onDeleteButtonClicked() },
                navigateToPrevious = { navController.navigateUp() },
                navigateToSplash = { navController.navigate(Routes.SplashScreen.route) }
            )
        }

        composable(route = Routes.HomeScreen.route) {
            val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
            val homeScreenState by homeScreenViewModel.homeScreenState.collectAsState()
            HomeScreen(
                homeScreenState = homeScreenState,
                navigateToPostDetails = { postId -> navController.navigate("PostDetailsScreen/$postId") },
                navigateToSettings = { navController.navigate(Routes.SettingsScreen.route) },
                navigateToAddPost = { navController.navigate(Routes.AddPostScreen.route) },
                navigateToUserAccount = { navController.navigate(Routes.UserAccountScreen.route) },
                navigateToSplash = { navController.navigate(Routes.SplashScreen.route) }
            )
        }

        composable(route = Routes.AddPostScreen.route) {
            val addPostScreenViewModel: AddPostScreenViewModel = hiltViewModel()
            val addPostScreenState by addPostScreenViewModel.addPostScreenState.collectAsState()
            val post by addPostScreenViewModel.post.collectAsStateWithLifecycle()
            val error by addPostScreenViewModel.error.collectAsStateWithLifecycle()
            AddPostScreen(
                addPostScreenState = addPostScreenState,
                post = post,
                error = error,
                onPhotoChanged = { uri ->
                    addPostScreenViewModel.onAction(FormEvent.PhotoChanged(uri)) },
                onTitleChanged = { title ->
                    addPostScreenViewModel.onAction(FormEvent.TitleChanged(title)) },
                onDescriptionChanged = { description ->
                    addPostScreenViewModel.onAction(FormEvent.DescriptionChanged(description)) },
                onSaveClicked = { addPostScreenViewModel.addPost() },
                navigateToHome = { navController.navigate(Routes.HomeScreen.route) }
            )
        }

        composable(route = Routes.PostDetailsScreen.route) {
            backStackEntry ->
            val postId = backStackEntry.arguments?.getString(Routes.PostDetailsScreen.ARGUMENT)
            val postDetailsScreenViewModel: PostDetailsScreenViewModel = hiltViewModel()
            val postDetailsScreenState by postDetailsScreenViewModel.postDetailsScreenState.collectAsState()
            PostDetailsScreen(
                postDetailsScreenState = postDetailsScreenState,
                navigateToHome = { navController.navigate(Routes.HomeScreen.route) },
                navigateToAddComment = {navController.navigate("AddCommentScreen/$postId")}
            )
        }

        composable(route = Routes.AddCommentScreen.route) { backStackEntry ->
            val postId =
                backStackEntry.arguments?.getString(Routes.AddCommentScreen.ARGUMENT)
            val addCommentScreenViewModel: AddCommentScreenViewModel = hiltViewModel()
            val addCommentScreenState by addCommentScreenViewModel.addCommentScreenState.collectAsState()
            val comment by addCommentScreenViewModel.comment.collectAsStateWithLifecycle()
            AddCommentScreen(
                addCommentScreenState = addCommentScreenState,
                comment = comment,
                onCommentChanged = { content -> addCommentScreenViewModel.onContentChanged(content)},
                onSaveClicked = { addCommentScreenViewModel.addComment()},
                navigateToPostDetails = { navController.navigate("PostDetailsScreen/$postId") },
            )

        }

        composable(route = Routes.SettingsScreen.route) {
            val settingsScreenViewModel: SettingsScreenViewModel = hiltViewModel()
            val settingsScreenState by settingsScreenViewModel.settingsScreenState.collectAsState()
            val notificationsPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                rememberPermissionState(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } else {
                null
            }
            SettingsScreen(
                settingsScreenState = settingsScreenState,
                onNotificationEnabledClicked = { settingsScreenViewModel.enableNotifications(notificationsPermissionState) },
                onNotificationDisabledClicked = { settingsScreenViewModel.disableNotifications() },
                navigateToHome = { navController.navigate(Routes.HomeScreen.route) }

            )
        }

    }
}