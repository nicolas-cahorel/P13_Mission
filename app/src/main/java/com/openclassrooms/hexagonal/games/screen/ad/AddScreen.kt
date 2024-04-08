package com.openclassrooms.hexagonal.games.screen.ad

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
  modifier: Modifier = Modifier,
  viewModel: AddViewModel = hiltViewModel(),
  onBackClick: () -> Unit,
  onSaveClick: () -> Unit
) {
  val scope = rememberCoroutineScope()
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }
  
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          Text(stringResource(id = R.string.add_fragment_label))
        },
        navigationIcon = {
          IconButton(onClick = {
            onBackClick()
          }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = stringResource(id = R.string.contentDescription_go_back)
            )
          }
        }
      )
    },
    snackbarHost = {
      SnackbarHost(hostState = snackbarHostState)
    },
    floatingActionButtonPosition = FabPosition.Center,
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = {
          if (viewModel.addPost()) {
            onSaveClick()
          }
        }
      ) {
        Text(
          text = stringResource(id = R.string.action_save)
        )
      }
    }
  ) { contentPadding ->
    val post by viewModel.post.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle(initialValue = null)
    
    if (error != null) {
      LaunchedEffect(Unit) {
        scope.launch {
          snackbarHostState.showSnackbar(context.getString(error!!.messageRes))
        }
      }
    }
    
    CreatePost(
      modifier = Modifier.padding(contentPadding),
      title = post.title,
      onTitleChanged = { viewModel.onAction(FormEvent.TitleChanged(it)) },
      description = post.description ?: "",
      onDescriptionChanged = { viewModel.onAction(FormEvent.DescriptionChanged(it)) }
    )
  }
}

@Composable
private fun CreatePost(
  modifier: Modifier = Modifier,
  title: String,
  onTitleChanged: (String) -> Unit,
  description: String,
  onDescriptionChanged: (String) -> Unit
) {
  val scrollState = rememberScrollState()
  
  Column(
    modifier = modifier
      .padding(bottom = 88.dp, top = 16.dp, start = 16.dp, end = 16.dp)
      .fillMaxSize()
      .verticalScroll(scrollState)
  ) {
    OutlinedTextField(
      modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth(),
      value = title,
      onValueChange = { onTitleChanged(it) },
      label = { Text(stringResource(id = R.string.hint_title)) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
      singleLine = true
    )
    OutlinedTextField(
      modifier = Modifier
        .padding(top = 16.dp)
        .fillMaxWidth(),
      value = description,
      onValueChange = { onDescriptionChanged(it) },
      label = { Text(stringResource(id = R.string.hint_description)) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
  }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun CreatePostPreview() {
  HexagonalGamesTheme(dynamicColor = false) {
    //CreatePost()
  }
}