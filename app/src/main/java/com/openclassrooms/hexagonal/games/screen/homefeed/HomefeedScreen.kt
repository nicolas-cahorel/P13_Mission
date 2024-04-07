package com.openclassrooms.hexagonal.games.screen.homefeed

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.domain.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomefeedScreen(
  modifier: Modifier = Modifier,
  onPostClick: (Post) -> Unit = {},
  onFABClick: () -> Unit = {},
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          Text(stringResource(id = R.string.homefeed_fragment_label))
        }
      )
    },
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          onFABClick()
        }
      ) {
        Icon(
          imageVector = Icons.Filled.Add,
          contentDescription = stringResource(id = R.string.description_button_add)
        )
      }
    }
  ) { contentPadding ->
    HomefeedList(
      modifier = modifier.padding(contentPadding),
      onPostClick = onPostClick
    )
  }
}

@Composable
private fun HomefeedList(
  modifier: Modifier = Modifier,
  onPostClick: (Post) -> Unit,
) {
  LazyColumn(modifier) {
  
  }
}