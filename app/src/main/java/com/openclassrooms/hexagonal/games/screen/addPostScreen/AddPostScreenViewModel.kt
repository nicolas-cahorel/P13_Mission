package com.openclassrooms.hexagonal.games.screen.addPostScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.data.repository.PostResult
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserResult
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.utils.InternetUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * This ViewModel is responsible for managing the state of the Add Post screen and handling the post creation process.
 * It interacts with the PostRepository and UserRepository to add a new post and fetch the associated user.
 * It also uses the InternetUtils to check for network availability before attempting the post creation.
 */
@HiltViewModel
class AddPostScreenViewModel @Inject constructor(
    private val internetUtils: InternetUtils,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    /**
     * MutableStateFlow that represents the current state of the Add Post screen.
     * It holds information about input validation and post creation status.
     */
    private val _addPostScreenState =
        MutableStateFlow<AddPostScreenState>(AddPostScreenState.InvalidInput("", "", false, false))

    /**
     * Exposed immutable StateFlow that reflects the Add Post screen state.
     */
    val addPostScreenState: StateFlow<AddPostScreenState> get() = _addPostScreenState

    /**
     * MutableStateFlow that holds the current post object.
     * The post includes details such as title, description, photo URL, and author.
     */
    private val _post = MutableStateFlow(
        Post(
            id = "",
            title = "",
            description = "",
            photoUrl = "",
            timestamp = Timestamp.now().seconds,
            author = null
        )
    )

    /**
     * Exposed immutable StateFlow that reflects the current post object.
     */
    val post: StateFlow<Post> get() = _post

    /**
     * StateFlow derived from the post that emits a FormError if the title is empty, or null if valid.
     * It is used to validate the form fields.
     */
    val error = post.map { verifyPost() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    /**
     * Verifies mandatory fields of the post
     * and returns a corresponding FormError if so.
     *
     * @return A FormError.TitleError if title is empty, null otherwise.
     */
    private fun verifyPost(): FormError? {
        return when {
            _post.value.title.isEmpty() -> FormError.TitleError
            _post.value.description.isNullOrEmpty() -> FormError.DescriptionError
            _post.value.photoUrl.isEmpty() -> FormError.PhotoError
            else -> null
        }
    }

    /**
     * Handles form events like title and description changes.
     *
     * @param formEvent The form event to be processed.
     */
    fun onAction(formEvent: FormEvent) {
        when (formEvent) {
            is FormEvent.DescriptionChanged -> {
                _post.value = _post.value.copy(description = formEvent.description)
            }

            is FormEvent.TitleChanged -> {
                _post.value = _post.value.copy(title = formEvent.title)
            }

            is FormEvent.PhotoChanged -> {
                _post.value = _post.value.copy(photoUrl = formEvent.photoUrl)
            }
        }
    }

    /**
     * Attempts to add the current post to the repository after assigning the author to the post.
     * The function checks for network availability, uploads the photo, and then saves the post to Firestore.
     * If any step fails, the state is updated to reflect the error.
     */
    fun addPost() {
        // Check if internet is available before attempting to add the post
        if (internetUtils.isInternetAvailable()) {

            // Prepare the post with a new ID and timestamp
            _post.value = _post.value.copy(
                id = UUID.randomUUID().toString(),
                timestamp = Timestamp.now().seconds
            )

            // Start the post creation process in a coroutine
            viewModelScope.launch {

                // Attempt to upload the photo to Firebase Storage
                postRepository.addPhoto(_post.value).collect { postResult ->
                    when (postResult) {
                        is PostResult.AddPhotoSuccess -> {
                            _post.value =
                                _post.value.copy(photoUrl = postResult.downloadUri)

                            // Fetch the user details and add the user as the post author
                            userRepository.readUser().collect { userResult ->
                                when (userResult) {
                                    is UserResult.ReadUserSuccess -> {
                                        _post.value =
                                            _post.value.copy(author = userResult.user)

                                        // Attempt to add the post to Firestore
                                        postRepository.addPost(_post.value).collect { postResult ->
                                            when (postResult) {

                                                // Post successfully added to Firestore
                                                is PostResult.AddPostSuccess -> {
                                                    _addPostScreenState.value =
                                                        AddPostScreenState.AddPostSuccess
                                                }

                                                // Error while adding the post
                                                else -> {
                                                    _addPostScreenState.value =
                                                        AddPostScreenState.AddPostError
                                                }
                                            }
                                        }
                                    }

                                    // Error fetching user
                                    else -> {
                                        _addPostScreenState.value = AddPostScreenState.AddPostError
                                    }

                                }
                            }
                        }

                        // Error adding photo to Firebase Storage
                        else -> {
                            _addPostScreenState.value = AddPostScreenState.AddPostError
                        }
                    }
                }
            }

        // No internet connection available
        } else {
            _addPostScreenState.value = AddPostScreenState.AddPostNoInternet
        }
    }
}