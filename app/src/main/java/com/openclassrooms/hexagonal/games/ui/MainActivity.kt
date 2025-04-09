package com.openclassrooms.hexagonal.games.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.firebase.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.AppCheckToken
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.openclassrooms.hexagonal.games.screen.navigation.Navigation
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Main activity for the application. This activity serves as the entry point and container for the navigation
 * fragment. It handles setting up the toolbar, navigation controller, and action bar behavior.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is created. It initializes Firebase and configures Firebase App Check for
     * the appropriate environment (Debug or Production). It also sets the content view and navigation structure.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // Initialize Firebase App Check based on the build type
        val appCheck = FirebaseAppCheck.getInstance()

        if (BuildConfig.DEBUG) {
            // Debug mode: Use test tokens
            appCheck.installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
        } else {
            // Production mode: Use Play Integrity
            appCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance())
        }

        // Call the function to get App Check token with retries and delay
        getAppCheckTokenWithDelay()

        setContent {
            val navController = rememberNavController()

            HexagonalGamesTheme {
                Navigation(navController = navController)
            }
        }
    }

    /**
     * Attempts to retrieve the App Check token with retries and an increasing delay between attempts.
     * It makes up to 3 attempts, with the delay time doubling after each attempt.
     */
    private fun getAppCheckTokenWithDelay() {
        var attempts = 0
        val maxAttempts = 3
        var delayTime = 1000L // Initial delay of 1 second

        // Use lifecycleScope to launch a coroutine tied to the activity's lifecycle
        lifecycleScope.launch {
            while (attempts < maxAttempts) {
                try {
                    // Attempt to retrieve App Check token
                    val appCheck = FirebaseAppCheck.getInstance()
                    val token: AppCheckToken =
                        appCheck.getAppCheckToken(true).await() // Wait for the token asynchronously

                    // Token retrieved successfully
                    break
                } catch (e: Exception) {
                    attempts++
                    if (attempts < maxAttempts) {
                        Thread.sleep(delayTime) // Wait before retrying
                        delayTime *= 2  // Increase delay time between attempts
                    } else {
                        // Handle failure after several attempts
                    }
                }
            }
        }
    }

}