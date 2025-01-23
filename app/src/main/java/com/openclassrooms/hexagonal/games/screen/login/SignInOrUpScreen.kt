package com.openclassrooms.hexagonal.games.screen.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun SignInOrUpScreen(onSignInSuccess: (FirebaseUser) -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val navController = rememberNavController()

    // Définir le gestionnaire de résultat d'activité
    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // L'utilisateur s'est connecté avec succès
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                onSignInSuccess(it) // Passer l'utilisateur à la fonction de succès
            }
        } else {
            // Gestion des cas d'échec ou d'annulation
            println("Authentication failed or was canceled.")
        }
    }



    // Lancer directement le processus de connexion
    LaunchedEffect(Unit) {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false) // Désactiver Smart Lock si nécessaire
            .build()

        // Lancer l'intent pour la connexion
        authLauncher.launch(signInIntent)
    }

    // Interface de l'écran de connexion (même si elle est visible, la connexion se fait automatiquement)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Signing you in...")
    }
}