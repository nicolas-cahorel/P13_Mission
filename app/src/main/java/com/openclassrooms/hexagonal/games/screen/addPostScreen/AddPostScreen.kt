package com.openclassrooms.hexagonal.games.screen.addPostScreen

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme

/**
 * Displays the screen for adding a new post.
 *
 * This composable allows users to create a new post by providing a title, description, and an optional photo.
 * It handles form validation, user interactions, and navigation events.
 *
 * @param modifier Modifier to customize the layout appearance and behavior.
 * @param addPostScreenState The current UI state of the AddPost screen.
 * @param post The post object containing the current title, description, and photo URL.
 * @param error The form validation error, if any.
 * @param onPhotoChanged Callback invoked when the user selects a new photo.
 * @param onTitleChanged Callback invoked when the title input changes.
 * @param onDescriptionChanged Callback invoked when the description input changes.
 * @param onSaveClicked Callback invoked when the user clicks the save button.
 * @param navigateToHome Callback invoked to navigate back to the home screen after successful post creation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    modifier: Modifier = Modifier,
    addPostScreenState: AddPostScreenState,
    post: Post,
    error: FormError?,
    onPhotoChanged: (String) -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    navigateToHome: () -> Unit
) {

    val context = LocalContext.current

    // Effect to show toast messages or navigate based on the post state.
    LaunchedEffect(addPostScreenState) {
        when (addPostScreenState) {
            is AddPostScreenState.AddPostNoInternet -> {
                Toast.makeText(
                    context,
                    R.string.toast_no_network,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is AddPostScreenState.AddPostError -> {
                Toast.makeText(
                    context,
                    R.string.toast_add_post_error,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is AddPostScreenState.AddPostSuccess -> {
                navigateToHome()
            }

            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.title_addPostScreen))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navigateToHome()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.contentDescription_go_back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { contentPadding ->

        /// Check if the Photo Picker is available for Android 13 and later
        val isPhotoPickerAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

        // Image Picker for Android 13 and later (API 33+)
        val pickVisualMediaLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                onPhotoChanged(uri.toString())
            }
        }

        // Image Picker for Android 12 and earlier (API 32-)
        val getContentLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                onPhotoChanged(uri.toString())
            }
        }

        CreatePost(
            modifier = Modifier.padding(contentPadding),
            error = error,
            title = post.title,
            onTitleChanged = { onTitleChanged(it) },
            description = post.description,
            onDescriptionChanged = { onDescriptionChanged(it) },
            onSaveClicked = { onSaveClicked() },
            onSelectPhotoClicked = {
                if (isPhotoPickerAvailable) {
                    pickVisualMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    getContentLauncher.launch("image/*")
                }
            },
            photoUrl = post.photoUrl
        )
    }
}

/**
 * Composable that represents the form for creating a post.
 * It includes input fields for title and description, an image picker, and a save button.
 */
@Composable
private fun CreatePost(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onSelectPhotoClicked: () -> Unit,
    error: FormError?,
    photoUrl: String
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { onTitleChanged(it) },
                isError = error is FormError.TitleError,
                label = { Text(stringResource(id = R.string.hint_title)) },
                colors = TextFieldDefaults.colors(focusedLabelColor = MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
            )
            if (error is FormError.TitleError) {
                Text(
                    text = stringResource(id = error.messageRes),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            OutlinedTextField(
                value = description,
                onValueChange = { onDescriptionChanged(it) },
                isError = error is FormError.DescriptionError,
                label = { Text(stringResource(id = R.string.hint_description)) },
                colors = TextFieldDefaults.colors(focusedLabelColor = MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            if (error is FormError.DescriptionError) {
                Text(
                    text = stringResource(id = error.messageRes),
                    color = MaterialTheme.colorScheme.error,
                )
            }


            AsyncImage(
                model = photoUrl.takeIf { it.isNotEmpty() } ?: R.drawable.ic_launcher_background,
                contentDescription = "Image selected by user",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(300.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_background)
            )
            if (error is FormError.PhotoError) {
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 40.dp),
                    text = stringResource(id = error.messageRes),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Button(
                onClick = { onSelectPhotoClicked() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(id = R.string.title_select_photo_button)
                )
            }

        }
        Button(
            enabled = error == null,
            onClick = { onSaveClicked() }
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(id = R.string.title_save_button)
            )
        }
    }
}

/**
 * Preview for [AddPostScreen].
 */
@Preview(showBackground = true)
@Composable
private fun AddPostScreenPreview() {
    HexagonalGamesTheme {
        AddPostScreen(
            modifier = Modifier,
            addPostScreenState = AddPostScreenState.AddPostSuccess,
            post = Post(
                title = "",
                description = "",
                photoUrl = ""
            ),
            error = null,
            onPhotoChanged = {},
            onTitleChanged = {},
            onDescriptionChanged = {},
            onSaveClicked = {},
            navigateToHome = {}
        )
    }
}

/**
 * Preview for [AddPostScreen].
 */
@Preview(showBackground = true)
@Composable
private fun AddPostScreenInvalidInputPreview() {
    HexagonalGamesTheme {
        AddPostScreen(
            modifier = Modifier,
            addPostScreenState = AddPostScreenState.InvalidInput(
                titleTextFieldLabel = stringResource(R.string.error_title),
                descriptionTextFieldLabel = "",
                isTitleValid = false,
                isDescriptionValid = true
            ),
            post = Post(
                title = "",
                description = "",
                photoUrl = ""
            ),
            error = FormError.TitleError,
            onPhotoChanged = {},
            onTitleChanged = {},
            onDescriptionChanged = {},
            onSaveClicked = {},
            navigateToHome = {}
        )
    }
}