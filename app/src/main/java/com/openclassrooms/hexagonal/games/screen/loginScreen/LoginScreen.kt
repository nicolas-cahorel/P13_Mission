package com.openclassrooms.hexagonal.games.screen.loginScreen

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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.openclassrooms.hexagonal.games.R

@Composable
fun LoginScreen(
    onButtonClicked: () -> Unit,
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
                    onButtonClicked()
                }) {
                Text(stringResource(R.string.title_signIn_button))
            }
        }
    }
}

