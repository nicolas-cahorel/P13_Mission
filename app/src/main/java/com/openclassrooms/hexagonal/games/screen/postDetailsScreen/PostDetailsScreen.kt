package com.openclassrooms.hexagonal.games.screen.postDetailsScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.util.DebugLogger
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User

/**
 * Screen displaying the details of a post. This screen allows users to view a post's content, including its title, author, description, and associated comments.
 * Users can also navigate back to the home screen or add a new comment if they are logged in and have an internet connection.
 *
 * @param modifier Modifier to apply to the root element of the composable.
 * @param postDetailsScreenState The [PostDetailsScreenState] that holds the current state of the post details screen.
 * @param navigateToHome A lambda function to navigate back to the home screen.
 * @param navigateToAddComment A lambda function to navigate to the add comment screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    modifier: Modifier = Modifier,
    postDetailsScreenState: PostDetailsScreenState,
    navigateToHome: () -> Unit,
    navigateToAddComment: () -> Unit
) {

    val context = LocalContext.current
    val topAppBarTitle: String =
        when (postDetailsScreenState) {
            is PostDetailsScreenState.DisplayPostDetails -> postDetailsScreenState.post.title
            else -> stringResource(id = R.string.title_postDetailsScreen)
        }

    // Scaffold layout with a top bar, menu, and floating action button
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(topAppBarTitle) },
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
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (postDetailsScreenState.isInternetAvailable) {
                        if (postDetailsScreenState.isUserLoggedIn) {
                            navigateToAddComment()
                        } else {
                            Toast.makeText(
                                context,
                                R.string.toast_user_not_logged_in_comment,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            R.string.toast_no_network,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.description_button_add),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { contentPadding ->

        Column(modifier = modifier.padding(contentPadding)) {

            when (postDetailsScreenState) {
                is PostDetailsScreenState.DisplayPostDetails -> {

                    if (postDetailsScreenState.comments.isNotEmpty()) {
                        PostCell(
                            modifier = modifier,
                            post = postDetailsScreenState.post
                        )
                        CommentList(
                            modifier = modifier,
                            comments = postDetailsScreenState.comments
                        )
                    } else {
                        PostCell(
                            modifier = modifier,
                            post = postDetailsScreenState.post
                        )
                        ElevatedCard(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                modifier = modifier.padding(4.dp),
                                text = stringResource(id = R.string.post_details_screen_no_comment),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                    }
                }

                is PostDetailsScreenState.ErrorWhileFetchingData -> {
                    Text(
                        text = postDetailsScreenState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                else -> {
                    Text(
                        text = stringResource(id = R.string.post_details_screen_error),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

        }


    }
}

/**
 * Displays a single post in a structured layout.
 *
 * @param modifier Modifier to apply to the layout.
 * @param post The post to display.
 */
@Composable
private fun PostCell(
    modifier: Modifier,
    post: Post
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {

        Text(
            text = stringResource(
                id = R.string.by,
                post.author?.firstname ?: "",
                post.author?.lastname ?: ""
            ),
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = modifier.height(8.dp))

        Text(
            text = post.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = modifier.height(8.dp))

        AsyncImage(
            modifier = modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .aspectRatio(ratio = 16 / 9f),
            model = post.photoUrl,
            imageLoader = LocalContext.current.imageLoader.newBuilder()
                .logger(DebugLogger())
                .build(),
            placeholder = ColorPainter(Color.DarkGray),
            contentDescription = "image",
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = modifier.height(8.dp))

        Text(
            text = post.description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Displays a list of comments associated with a post.
 *
 * @param modifier Modifier to apply to the layout.
 * @param comments List of comments to display.
 */
@Composable
private fun CommentList(
    modifier: Modifier,
    comments: List<Comment>
) {
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(comments) { comment ->
            CommentCell(
                modifier = modifier,
                comment = comment
            )
        }
    }
}

/**
 * Displays a single comment in a structured layout.
 *
 * @param modifier Modifier to apply to the layout.
 * @param comment The comment to display.
 */
@Composable
private fun CommentCell(
    modifier: Modifier,
    comment: Comment
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { }
    ) {
        Column(
            modifier = modifier.padding(8.dp),
        ) {
            Text(
                text = stringResource(
                    id = R.string.by,
                    comment.author?.firstname ?: "",
                    comment.author?.lastname ?: ""
                ),
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment.content,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

/**
 * Preview for [PostDetailsScreen].
 */
@Preview
@Composable
fun PreviewPostDetailsScreen() {
    val samplePost = Post(
        id = "1",
        title = "Sample Post Title",
        description = "This is a sample post description with some details.",
        photoUrl = "https://via.placeholder.com/600x400",
        author = User(
            id = "1",
            firstname = "John",
            lastname = "Doe",
            email = "john.doe@example.com"
        )
    )

    val sampleComments = listOf(
        Comment(
            id = "1",
            content = "This is a sample comment.",
            author = User(firstname = "Jane", lastname = "Smith")
        ),
        Comment(
            id = "2",
            content = "Another insightful comment here!",
            author = User(firstname = "Bob", lastname = "Johnson")
        )
    )

    PostDetailsScreen(
        postDetailsScreenState = PostDetailsScreenState.DisplayPostDetails(
            post = samplePost,
            comments = sampleComments,
            isUserLoggedIn = true,
            isInternetAvailable = true
        ),
        navigateToHome = {},
        navigateToAddComment = {}
    )
}

/**
 * Preview for [PostDetailsScreen].
 */
@Preview
@Composable
fun PreviewPostDetailsScreenNoComment() {
    val samplePost = Post(
        id = "1",
        title = "Sample Post Title",
        description = "This is a sample post description with some details.",
        photoUrl = "https://via.placeholder.com/600x400",
        author = User(
            id = "1",
            firstname = "John",
            lastname = "Doe",
            email = "john.doe@example.com"
        )
    )

    PostDetailsScreen(
        postDetailsScreenState = PostDetailsScreenState.DisplayPostDetails(
            post = samplePost,
            comments = emptyList(),
            isUserLoggedIn = true,
            isInternetAvailable = true
        ),
        navigateToHome = {},
        navigateToAddComment = {}
    )
}