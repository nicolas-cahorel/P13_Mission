package com.openclassrooms.hexagonal.games.screen.addPostScreen

import android.os.Build
import android.util.Log
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme


/**
 * Displays the AddPost screen, allowing users to create a new post.
 *
 * This composable function manages the UI state, interacts with the ViewModel, and handles
 * navigation based on the outcome of user actions. It manages various scenarios, including
 * no internet connection, error states, and successful post creation.
 *
 * @param modifier The modifier for customizing the layout appearance and behavior.
 * @param viewModel The ViewModel managing the logic and state of the AddPost screen.
 * @param addPostScreenState The current state of the AddPost screen, including UI state and action status.
 * @param navigateToHome Lambda function invoked to navigate to the home screen after a successful post creation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    modifier: Modifier = Modifier,
    viewModel: AddPostScreenViewModel,
    addPostScreenState: AddPostScreenState,
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
                    Text(stringResource(id = R.string.title_addScreen))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigateToHome()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.contentDescription_go_back)
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        val post by viewModel.post.collectAsStateWithLifecycle()
        val error by viewModel.error.collectAsStateWithLifecycle()


        /// Check if the Photo Picker is available for Android 13 and later
        val isPhotoPickerAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

        // Image Picker for Android 13 and later (API 33+)
        val pickVisualMediaLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                Log.d("PhotoPicker", "Selected URI: $uri")
                viewModel.onAction(FormEvent.PhotoChanged(uri.toString()))
            } ?: Log.d("PhotoPicker", "No media selected")
        }

        // Image Picker for Android 12 and earlier (API 32-)
        val getContentLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                Log.d("PhotoPicker", "Selected URI: $uri")
                viewModel.onAction(FormEvent.PhotoChanged(uri.toString()))
            } ?: Log.d("PhotoPicker", "No media selected")
        }

        CreatePost(
            modifier = Modifier.padding(contentPadding),
            error = error,
            title = post.title,
            onTitleChanged = { viewModel.onAction(FormEvent.TitleChanged(it)) },
            description = post.description ?: "",
            onDescriptionChanged = { viewModel.onAction(FormEvent.DescriptionChanged(it)) },
            onSaveClicked = { viewModel.addPost() },
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
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                value = title,
                isError = error is FormError.TitleError,
                onValueChange = { onTitleChanged(it) },
                label = { Text(stringResource(id = R.string.hint_title)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            if (error is FormError.TitleError) {
                Text(
                    text = stringResource(id = error.messageRes),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                value = description,
                isError = error is FormError.DescriptionError,
                onValueChange = { onDescriptionChanged(it) },
                label = { Text(stringResource(id = R.string.hint_description)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
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


// PREVIEWS

//@Preview
//@Composable
//private fun AddPostScreenPreview() {
//    val fakePost = remember { mutableStateOf(Post("", "", "")) }
//    val fakeError = remember { mutableStateOf<FormError?>(null) }
//
//    // Fake ViewModel Simulation
//    val fakeViewModel = object {
//        val post: State<Post> = fakePost
//        val error: State<FormError?> = fakeError
//
//        fun onAction(action: FormEvent) {}
//        fun addPost() {}
//    }
//
//    HexagonalGamesTheme {
//        AddPostScreen(
//            viewModel = null, // On ne passe pas de ViewModel réel !
//            addPostScreenState = AddPostScreenState.InvalidInput(
//                titleTextFieldLabel = stringResource(R.string.error_title),
//                descriptionTextFieldLabel = stringResource(R.string.error_description),
//                isTitleValid = false,
//                isDescriptionValid = false
//            ),
//            navigateToHome = {}
//        )
//    }
//}



@Preview
@Composable
private fun CreatePostPreview() {
    HexagonalGamesTheme {
        CreatePost(
            title = "test",
            onTitleChanged = { },
            description = "description",
            onDescriptionChanged = { },
            onSaveClicked = { },
            error = null,
            onSelectPhotoClicked = { },
            photoUrl = ""
        )
    }
}

@Preview
@Composable
private fun CreatePostErrorPreview() {
    HexagonalGamesTheme {
        CreatePost(
            title = "test",
            onTitleChanged = { },
            description = "description",
            onDescriptionChanged = { },
            onSaveClicked = { },
            error = FormError.TitleError,
            onSelectPhotoClicked = { },
            photoUrl = ""
        )
    }
}