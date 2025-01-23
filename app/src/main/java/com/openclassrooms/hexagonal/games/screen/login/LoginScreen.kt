package com.openclassrooms.hexagonal.games.screen.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.screen.Routes

@Composable
fun LoginScreen(
    navController: NavController,
    onSignInSuccess: (FirebaseUser) -> Unit
) {
    val auth = FirebaseAuth.getInstance()


    // Vérifie si l'utilisateur est déjà connecté
    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Naviguer directement vers l'écran d'accueil
            onSignInSuccess(currentUser) // Appelle la fonction de succès
        }
    }

    // Interface de l'écran de connexion
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            navController.navigate(Routes.SignInOrUpScreen.route) {
                popUpTo(Routes.SignInOrUpScreen.route) { inclusive = true }
            }
        }) {
            Text("Sign in / Sign up")
        }
    }
}
