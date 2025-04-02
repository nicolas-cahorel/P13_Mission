package com.openclassrooms.hexagonal.games.screen.addCommentScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.openclassrooms.hexagonal.games.data.repository.CommentRepository
import com.openclassrooms.hexagonal.games.data.repository.CommentResult
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserResult
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.User
import com.openclassrooms.hexagonal.games.utils.InternetUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state of the Add Comment screen and handling the comment creation process.
 * It interacts with the [UserRepository] to fetch the user details and [CommentRepository] to add a new comment.
 * Additionally, it verifies network availability through [InternetUtils] before attempting the comment creation.
 */
@HiltViewModel
class AddCommentScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val internetUtils: InternetUtils,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    /**
     * MutableStateFlow representing the current state of the Add Comment screen.
     * It holds information about input validation and comment creation status.
     */
    private val _addCommentScreenState =
        MutableStateFlow<AddCommentScreenState>(AddCommentScreenState.InvalidInput)
    val addCommentScreenState: StateFlow<AddCommentScreenState> get() = _addCommentScreenState

    /**
     * MutableStateFlow holding the current comment object.
     * The comment includes details such as content, post ID, timestamp, and author.
     */
    private val _comment = MutableStateFlow(
        Comment(
            id = "",
            postId = "",
            content = "",
            timestamp = Timestamp.now().seconds,
            author = null
        )
    )
    val comment: StateFlow<Comment> get() = _comment

    /**
     * Updates the comment content and validates the input.
     *
     * @param content The new content of the comment.
     */
    fun onContentChanged(content: String) {
        _comment.value = _comment.value.copy(content = content)

        if (content.isNotEmpty()) {
            _addCommentScreenState.value = AddCommentScreenState.ValidInput
        } else {
            _addCommentScreenState.value = AddCommentScreenState.InvalidInput
        }
    }


    /**
     * Attempts to add a comment to the repository after retrieving the user as the author.
     *
     * The function verifies network availability and retrieves the post ID before creating the comment.
     * If any step fails, the screen state is updated accordingly.
     */
    fun addComment() {

        // Check if internet is available before attempting to add the comment
        if (internetUtils.isInternetAvailable()) {

            // Retrieve postId from navigation argument
            val postId: String? = savedStateHandle["postId"]
            if (!postId.isNullOrEmpty()) {

                // Prepare the comment with a new ID and timestamp
                _comment.value = _comment.value.copy(
                    id = UUID.randomUUID().toString(),
                    postId = postId,
                    timestamp = Timestamp.now().seconds
                )

                // Start the comment creation process in a coroutine
                viewModelScope.launch {

                    // Fetch the user details and add the user as the post author
                    userRepository.readUser().collect { userResult ->
                        when (userResult) {

                            // User successfully fetched an added to comment as author
                            is UserResult.ReadUserSuccess -> {
                                _comment.value = _comment.value.copy(author = userResult.user)

                                // Attempt to add the comment to Firestore DB
                                commentRepository.addComment(_comment.value).collect { commentResult ->
                                        when (commentResult) {

                                            // Comment successfully added to Firestore DB
                                            is CommentResult.AddCommentSuccess -> {
                                                _addCommentScreenState.value =
                                                    AddCommentScreenState.AddCommentSuccess
                                                Log.d(
                                                    "Nicolas",
                                                    "comment successfully added to Firestore DB"
                                                )
                                            }

                                            // Error while adding the comment to Firestore DB
                                            else -> {
                                                _addCommentScreenState.value =
                                                    AddCommentScreenState.AddCommentError
                                                Log.d(
                                                    "Nicolas",
                                                    "Error while adding the comment to Firestore DB"
                                                )
                                            }
                                        }
                                }
                            }

                            // Error fetching user
                            else -> {
                                _addCommentScreenState.value = AddCommentScreenState.AddCommentError
                                Log.d("Nicolas", "could not fetch user to add comments author")
                            }
                        }
                    }
                }

                // Error while retrieving postId from navigation argument
            } else {
                _addCommentScreenState.value = AddCommentScreenState.AddCommentError
                Log.d("Nicolas", "Error while retrieving postId from navigation argument")
            }

            // No internet connection available
        } else {
            _addCommentScreenState.value = AddCommentScreenState.AddCommentNoInternet
            Log.d("Nicolas", "No internet connection available")
        }
    }

    /**
     * Refactored version of [addComment] for improved error handling and state management.
     */
    fun addCommentRefactorized() {
        if (!internetUtils.isInternetAvailable()) {
            _addCommentScreenState.value = AddCommentScreenState.AddCommentNoInternet
            Log.d("Nicolas", "No internet connection available")
            return
        }

        val postId: String? = savedStateHandle["postId"]
        if (postId.isNullOrEmpty()) {
            _addCommentScreenState.value = AddCommentScreenState.AddCommentError
            Log.d("Nicolas", "Error while retrieving postId from navigation argument")
            return
        }

        viewModelScope.launch {

            var user: User? = null
            userRepository.readUser().collect { userResult ->
                user = when (userResult) {
                    is UserResult.ReadUserSuccess -> userResult.user
                    else -> null
                }
            }

            if (user == null) {
                Log.d("Nicolas", "FirebaseCommentService - addComment(): No user retrieved after collecting.")
                _addCommentScreenState.value = AddCommentScreenState.AddCommentError
                return@launch
            }

            _comment.value = _comment.value.copy(
                id = UUID.randomUUID().toString(),
                postId = postId,
                timestamp = Timestamp.now().seconds,
                author = user
            )

            commentRepository.addComment(_comment.value).collect { commentResult ->
                when (commentResult) {

                    is CommentResult.AddCommentSuccess -> {
                        _addCommentScreenState.value =
                            AddCommentScreenState.AddCommentSuccess
                        Log.d(
                            "Nicolas",
                            "comment successfully added to Firestore DB"
                        )
                    }

                    else -> {
                        _addCommentScreenState.value =
                            AddCommentScreenState.AddCommentError
                        Log.d(
                            "Nicolas",
                            "Error while adding the comment to Firestore DB"
                        )
                    }
                }
            }
        }
    }
}