package com.openclassrooms.hexagonal.games.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.hexagonal.games.screen.addScreen.AddScreen
import com.openclassrooms.hexagonal.games.screen.homeScreen.HomeScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.LoginScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.PasswordRecoveryScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.SignInOrUpScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.SignInScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.SignUpScreen
import com.openclassrooms.hexagonal.games.screen.loginScreens.UserAccountScreen
import com.openclassrooms.hexagonal.games.screen.navigation.Navigation
import com.openclassrooms.hexagonal.games.screen.navigation.Routes
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
                Navigation(navController = navController)
            }
        }
    }
}

