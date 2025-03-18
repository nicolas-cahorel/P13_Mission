package com.openclassrooms.hexagonal.games.screen.splashScreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.openclassrooms.hexagonal.games.R

/**
 * Composable function that displays the SplashScreen.
 *
 * This screen is shown when the app is launched for the first time. It presents the app's logo and includes
 * a button that allows navigation either to the login/signup screen or the home screen, depending on the user's
 * login state.
 *
 * @param splashScreenState The [SplashScreenState] that holds the current state of the splash screen.
 * @param navigateToLoginOrSignUp A lambda function that navigates to the login/signup screen.
 * @param navigateToHome A lambda function that navigates to the home screen.
 */
@Composable
fun SplashScreen(
    splashScreenState: SplashScreenState,
    navigateToLoginOrSignUp: () -> Unit,
    navigateToHome: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column {

            AsyncImage(
                model = R.mipmap.ic_launcher_foreground,
                contentDescription = "logo Hexagonal Games",
                modifier = Modifier
                    .size(300.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_background)
            )

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    Log.d("Nicolas", "splashScreenState: $splashScreenState")
                    when (splashScreenState) {
                        is SplashScreenState.UserIsLoggedIn -> navigateToHome()
                        is SplashScreenState.UserIsNotLoggedIn -> navigateToLoginOrSignUp()
                    }
                }) {
                Text(stringResource(R.string.title_signIn_button))
            }
        }
    }
}

/**
 * Preview for [SplashScreen].
 */
@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(
        splashScreenState = SplashScreenState.UserIsLoggedIn,
        navigateToLoginOrSignUp = { },
        navigateToHome = { }
    )
}