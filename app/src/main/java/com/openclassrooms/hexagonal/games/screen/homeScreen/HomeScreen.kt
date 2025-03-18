package com.openclassrooms.hexagonal.games.screen.homeScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.util.DebugLogger
import com.google.firebase.Timestamp
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme
import kotlin.random.Random

/**
 * Home screen displaying a list of posts. This screen allows users to navigate to different sections such as
 * post details, settings, user account, or the screen to add a new post.
 *
 * @param modifier Modifier to apply to the root element of the composable.
 * @param homeScreenState The [HomeScreenState] that holds the current state of the home screen.
 * @param navigateToPostDetails A lambda function to navigate to the post details screen, taking the post ID as a parameter.
 * @param navigateToSettings A lambda function to navigate to the settings screen.
 * @param navigateToAdd A lambda function to navigate to the add post screen.
 * @param navigateToUserAccount A lambda function to navigate to the user account screen.
 * @param navigateToSplash A lambda function to navigate to the splash screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenState: HomeScreenState,
    navigateToPostDetails: (String) -> Unit = {},
    navigateToSettings: () -> Unit = {},
    navigateToAdd: () -> Unit = {},
    navigateToUserAccount: () -> Unit = {},
    navigateToSplash: () -> Unit = {}
) {

    val context = LocalContext.current
    var showMenu by rememberSaveable { mutableStateOf(false) }

    // Show toast messages based on the state of the home screen
    LaunchedEffect(homeScreenState) {
        when (homeScreenState) {
            is HomeScreenState.Loading -> Unit
            is HomeScreenState.DisplayPosts -> Unit
            is HomeScreenState.NoPostToDisplay -> {
                Toast.makeText(
                    context,
                    R.string.toast_no_posts,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is HomeScreenState.InternetUnavailable -> {
                Toast.makeText(
                    context,
                    R.string.toast_no_internet,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Scaffold layout with a top bar, menu, and floating action button
    Scaffold(
        modifier = modifier,
        topBar = {

            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.title_homeScreen))
                },
                actions = {

                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(id = R.string.contentDescription_more)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {

                        DropdownMenuItem(
                            onClick = {
                                navigateToSettings()
                                showMenu = false
                            },
                            text = {
                                Text(
                                    text = stringResource(id = R.string.title_settingsScreen)
                                )
                            }
                        )

                        DropdownMenuItem(
                            onClick = {
                                showMenu = false
                                if (homeScreenState.isUserLoggedIn) {
                                    navigateToUserAccount()
                                } else {
                                    navigateToSplash()
                                }
                            },
                            text = {
                                Text(
                                    stringResource(id = R.string.title_my_account)
                                )
                            }
                        )

                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (homeScreenState.isUserLoggedIn) {
                        navigateToAdd()
                    } else {
                        Toast.makeText(
                            context,
                            R.string.toast_user_not_logged_in,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.description_button_add)
                )
            }
        }
    ) { contentPadding ->
        val posts = when (homeScreenState) {
            is HomeScreenState.DisplayPosts -> homeScreenState.posts
            else -> emptyList()
        }

        FeedList(
            modifier = modifier.padding(contentPadding),
            posts = posts,
            onPostClick = navigateToPostDetails
        )
    }
}

/**
 * Displays a list of posts in a LazyColumn.
 *
 * @param modifier Modifier to apply to the list.
 * @param posts List of posts to display.
 * @param onPostClick Function to handle post click events.
 */
@Composable
private fun FeedList(
    modifier: Modifier = Modifier,
    posts: List<Post>,
    onPostClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(posts) { post ->
            FeedCell(
                post = post,
                onPostClick = onPostClick
            )
        }
    }
}

/**
 * Displays a single post in a card layout.
 *
 * @param post The post to display.
 * @param onPostClick Function to handle click events for the post.
 */
@Composable
private fun FeedCell(
    post: Post,
    onPostClick: (String) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onPostClick(post.id)
        }) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = stringResource(
                    id = R.string.by,
                    post.author?.firstname ?: "",
                    post.author?.lastname ?: ""
                ),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge
            )
            if (post.photoUrl.isNotEmpty()) {
                AsyncImage(
                    modifier = Modifier
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
                    contentScale = ContentScale.Crop,
                )
            }
            if (post.description.isNotEmpty()) {
                Text(
                    text = post.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// A helper function to generate a valid random timestamp
fun generateValidTimestamp(): Timestamp {
    val validSeconds = Random.nextLong(-62135596800, 253402300799)
    return Timestamp(validSeconds, 0)
}

// PREVIEWS

@Preview
@Composable
private fun FeedCellPreview() {
    HexagonalGamesTheme {
        FeedCell(
            post = Post(
                id = "1",
                title = "title",
                description = "description",
                photoUrl = "",
                timestamp = generateValidTimestamp().seconds,
                author = User(
                    id = "1",
                    firstname = "firstname",
                    lastname = "lastname",
                    email = ""
                )
            ),
            onPostClick = {}
        )
    }
}

@Preview
@Composable
private fun FeedCellImagePreview() {
    HexagonalGamesTheme {
        FeedCell(
            post = Post(
                id = "1",
                title = "title",
                description = "description",
                photoUrl = "https://picsum.photos/id/85/1080/",
                timestamp = generateValidTimestamp().seconds,
                author = User(
                    id = "1",
                    firstname = "firstname",
                    lastname = "lastname",
                    email = ""
                )
            ),
            onPostClick = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val dummyPosts = listOf(
        Post(
            id = "1",
            title = "Sample Post 1",
            description = "This is a sample description for post 1.",
            photoUrl = "https://picsum.photos/200/300",
            timestamp = generateValidTimestamp().seconds,
            author = User(
                id = "1",
                firstname = "John",
                lastname = "Doe",
                email = "john.doe@example.com"
            )
        ),
        Post(
            id = "2",
            title = "Sample Post 2",
            description = "This is a sample description for post 2.",
            photoUrl = "",
            timestamp = generateValidTimestamp().seconds,
            author = User(
                id = "2",
                firstname = "Jane",
                lastname = "Smith",
                email = "jane.smith@example.com"
            )
        )
    )


    HexagonalGamesTheme {
        HomeScreen(homeScreenState = HomeScreenState.DisplayPosts(dummyPosts, true))
    }
}

