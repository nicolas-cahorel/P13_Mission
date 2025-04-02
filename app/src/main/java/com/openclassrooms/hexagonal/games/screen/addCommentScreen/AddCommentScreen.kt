package com.openclassrooms.hexagonal.games.screen.addCommentScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.domain.model.Comment

/**
 * Displays the screen for adding a new comment.
 *
 * This composable allows users to create a new comment by providing text input.
 * It manages form validation, user interactions, and navigation events.
 *
 * @param modifier Modifier to customize the layout appearance and behavior.
 * @param addCommentScreenState The current UI state of the AddComment screen.
 * @param comment The comment object containing the current text input.
 * @param onCommentChanged Callback invoked when the comment input changes.
 * @param onSaveClicked Callback invoked when the user clicks the save button.
 * @param navigateToPostDetails Callback invoked to navigate back to the post details screen after successful comment creation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCommentScreen(
    modifier: Modifier = Modifier,
    addCommentScreenState: AddCommentScreenState,
    comment: Comment,
    onCommentChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    navigateToPostDetails: () -> Unit
) {

    val context = LocalContext.current

    // Effect to show toast messages or navigate based on the post state.
    LaunchedEffect(addCommentScreenState) {
        when (addCommentScreenState) {
            is AddCommentScreenState.AddCommentNoInternet -> {
                Toast.makeText(
                    context,
                    R.string.toast_no_network,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is AddCommentScreenState.AddCommentError -> {
                Toast.makeText(
                    context,
                    R.string.toast_add_post_error,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is AddCommentScreenState.AddCommentSuccess -> {
                navigateToPostDetails()
            }

            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.title_addCommentScreen))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navigateToPostDetails()
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

        CreateComment(
            modifier = Modifier.padding(contentPadding),
            addCommentScreenState = addCommentScreenState,
            content = comment.content,
            onCommentChanged = { onCommentChanged(it) },
            onSaveClicked = { onSaveClicked() }
        )
    }
}

/**
 * Composable that represents the form for creating a comment.
 * It includes an input field for the comment text and a save button.
 *
 * @param modifier Modifier to customize the layout.
 * @param addCommentScreenState The current UI state affecting input validation.
 * @param content The current text of the comment.
 * @param onCommentChanged Callback invoked when the comment input changes.
 * @param onSaveClicked Callback invoked when the save button is clicked.
 */
@Composable
private fun CreateComment(
    modifier: Modifier = Modifier,
    addCommentScreenState: AddCommentScreenState,
    content: String,
    onCommentChanged: (String) -> Unit,
    onSaveClicked: () -> Unit
) {

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = content,
            onValueChange = { onCommentChanged(it) },
            isError = addCommentScreenState is AddCommentScreenState.InvalidInput,
            label = {
                Text(
                    stringResource(
                        id = if (addCommentScreenState is AddCommentScreenState.InvalidInput)
                            R.string.error_content_invalid
                        else
                            R.string.hint_comment
                    )
                )
            },
            colors = TextFieldDefaults.colors(focusedLabelColor = MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = false,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
        )

        Spacer(modifier = modifier.height(8.dp))

        Button(
            enabled = when (addCommentScreenState) {
                is AddCommentScreenState.ValidInput -> true
                else -> false
            },
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
 * Preview for [AddCommentScreen].
 */
@Preview(showBackground = true)
@Composable
fun AddCommentScreenPreview() {
    AddCommentScreen(
        addCommentScreenState = AddCommentScreenState.ValidInput,
        comment = Comment(id = "1", content = "This is a sample comment."),
        onCommentChanged = {},
        onSaveClicked = {},
        navigateToPostDetails = {}
    )
}

/**
 * Preview for [AddCommentScreen].
 */
@Preview(showBackground = true)
@Composable
fun AddCommentScreenPreviewInvalidInput() {
    AddCommentScreen(
        addCommentScreenState = AddCommentScreenState.InvalidInput,
        comment = Comment(id = "1", content = " "),
        onCommentChanged = {},
        onSaveClicked = {},
        navigateToPostDetails = {}
    )
}