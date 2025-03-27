package com.openclassrooms.hexagonal.games.screen.postDetailsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.CommentRepository
import com.openclassrooms.hexagonal.games.data.repository.CommentResult
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.data.repository.PostResult
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserResult
import com.openclassrooms.hexagonal.games.utils.InternetUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state of the Post Details Screen.
 * It retrieves user data, post details, and associated comments from repositories,
 * ensuring that the UI reflects the correct state based on network availability and repository responses.
 *
 * This ViewModel utilizes `MutableStateFlow` to expose the current UI state (`PostDetailsScreenState`),
 * which is updated based on the results of data retrieval operations.
 */
@HiltViewModel
class PostDetailsScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val internetUtils: InternetUtils,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    // Holds the current state of the post details screen
    private val _postDetailsScreenState =
        MutableStateFlow<PostDetailsScreenState>(
            PostDetailsScreenState.Loading(
                isInternetAvailable = false,
                isUserLoggedIn = false
            )
        )
    val postDetailsScreenState: StateFlow<PostDetailsScreenState> get() = _postDetailsScreenState

    /**
     * Initializes the ViewModel by checking internet availability and fetching user and post details.
     * If the internet is available, it first retrieves user data and then fetches post details.
     * If no internet connection is detected, it updates the UI state accordingly.
     */
    init {

        // Récupération du postId depuis les arguments de la navigation (a traduire en anglais)
        val postId: String? = savedStateHandle["postId"]
        if (!postId.isNullOrEmpty()) {

            if (internetUtils.isInternetAvailable()) {
                viewModelScope.launch {
                    // Fetch user information from the repository
                    userRepository.readUser().collect { userResult ->
                        when (userResult) {
                            is UserResult.ReadUserSuccess -> {
                                // User is logged in, now fetch post details
                                postRepository.getPost(postId).collect { postResult ->
                                    when (postResult) {
                                        // Successfully retrieved post details, update UI state
                                        is PostResult.GetPostSuccess -> {

                                            commentRepository.getComments(postId)
                                                .collect { commentResult ->
                                                    when (commentResult) {
                                                        // Successfully retrieved comments, update UI state
                                                        is CommentResult.GetCommentsSuccess -> {
                                                            _postDetailsScreenState.value =
                                                                PostDetailsScreenState.DisplayPostDetails(
                                                                    isInternetAvailable = true,
                                                                    isUserLoggedIn = true,
                                                                    post = postResult.post,
                                                                    comments = commentResult.comments
                                                                )
                                                        }

                                                        // No post comments available, update UI state
                                                        is CommentResult.GetCommentsEmpty -> {
                                                            _postDetailsScreenState.value =
                                                                PostDetailsScreenState.DisplayPostDetails(
                                                                    isInternetAvailable = true,
                                                                    isUserLoggedIn = true,
                                                                    post = postResult.post,
                                                                    comments = emptyList()
                                                                )
                                                        }

                                                        // Error while fetching post comments, update UI state
                                                        else -> {
                                                            _postDetailsScreenState.value =
                                                                PostDetailsScreenState.ErrorWhileFetchingData(
                                                                    isInternetAvailable = true,
                                                                    isUserLoggedIn = true,
                                                                    errorMessage = "error while fetching comments"
                                                                )
                                                        }
                                                    }
                                                }
                                        }

                                        // No post details available, update UI state
                                        else -> {
                                            _postDetailsScreenState.value =
                                                PostDetailsScreenState.ErrorWhileFetchingData(
                                                    isInternetAvailable = true,
                                                    isUserLoggedIn = true,
                                                    errorMessage = "error while fetching post"
                                                )
                                        }
                                    }
                                }
                            }

                            // User data retrieval failed, set UI state accordingly
                            else -> _postDetailsScreenState.value =
                                PostDetailsScreenState.ErrorWhileFetchingData(
                                    isInternetAvailable = true,
                                    isUserLoggedIn = false,
                                    errorMessage = "error while fetching user"
                                )
                        }
                    }
                }

                // No internet connection, update UI state
            } else {
                _postDetailsScreenState.value = PostDetailsScreenState.ErrorWhileFetchingData(
                    isInternetAvailable = false,
                    isUserLoggedIn = false,
                    errorMessage = "error no internet connection"
                )
            }

            // postId retrieval from navigation failed, set UI state accordingly
        } else {
            _postDetailsScreenState.value = PostDetailsScreenState.ErrorWhileFetchingData(
                isInternetAvailable = false,
                isUserLoggedIn = false,
                errorMessage = "error could not retrieve postId from navigation"
            )
        }
    }
}