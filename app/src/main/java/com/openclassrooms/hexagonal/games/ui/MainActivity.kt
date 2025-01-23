package com.openclassrooms.hexagonal.games.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.screen.Routes
import com.openclassrooms.hexagonal.games.screen.addScreen.AddScreen
import com.openclassrooms.hexagonal.games.screen.homeScreen.HomeScreen
import com.openclassrooms.hexagonal.games.screen.login.LoginScreen
import com.openclassrooms.hexagonal.games.screen.login.SignInOrUpScreen
import com.openclassrooms.hexagonal.games.screen.settingsScreen.SettingsScreen
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the application. This activity serves as the entry point and container for the navigation
 * fragment. It handles setting up the toolbar, navigation controller, and action bar behavior.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    setContent {
      val navController = rememberNavController()
      
      HexagonalGamesTheme {
        HexagonalGamesNavHost(navHostController = navController)
      }
    }
  }
  
}

@Composable
fun HexagonalGamesNavHost(navHostController: NavHostController) {
  NavHost(
    navController = navHostController,
    startDestination = Routes.LoginScreen.route
  ) {

    composable(route = Routes.LoginScreen.route) {
      LoginScreen(
        navController = navHostController
      ) {
        navHostController.navigate(Routes.HomeScreen.route) {
          popUpTo(Routes.LoginScreen.route) { inclusive = true }
        }
      }
    }

    composable(route = Routes.SignInOrUpScreen.route) {
      SignInOrUpScreen(
        onSignInSuccess = {
          // Naviguer vers l'écran d'accueil après une connexion réussie
          navHostController.navigate(Routes.HomeScreen.route) {
            popUpTo(Routes.SignInOrUpScreen.route) { inclusive = true }
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
          navHostController.navigate(Routes.SettingsScreen.route)
        },
        onFABClick = {
          navHostController.navigate(Routes.AddScreen.route)
        }
      )
    }

    composable(route = Routes.AddScreen.route) {
      AddScreen(
        onBackClick = { navHostController.navigateUp() },
        onSaveClick = { navHostController.navigateUp() }
      )
    }

    composable(route = Routes.SettingsScreen.route) {
      SettingsScreen(
        onBackClick = { navHostController.navigateUp() }
      )
    }
  }
}
