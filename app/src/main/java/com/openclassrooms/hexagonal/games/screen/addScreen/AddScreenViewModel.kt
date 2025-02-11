package com.openclassrooms.hexagonal.games.screen.addScreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserRepositoryState
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User
import com.openclassrooms.hexagonal.games.utils.InternetUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * This ViewModel manages data and interactions related to adding new posts in the AddScreen.
 * It utilizes dependency injection to retrieve a PostRepository instance for interacting with post data.
 */
@HiltViewModel
class AddScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val internetUtils: InternetUtils
) :
    ViewModel() {

    private val _repositoryState = MutableStateFlow<UserRepositoryState?>(null)
    val repositoryState: StateFlow<UserRepositoryState?> get() = _repositoryState

    /**
     * Internal mutable state flow representing the current post being edited.
     */
    private var _post = MutableStateFlow(
        Post(
            id = UUID.randomUUID().toString(),
            title = "",
            description = "",
            photoUrl = null,
            timestamp = System.currentTimeMillis(),
            author = null
        )
    )

    /**
     * Public state flow representing the current post being edited.
     * This is immutable for consumers.
     */
    val post: StateFlow<Post>
        get() = _post

    /**
     * StateFlow derived from the post that emits a FormError if the title is empty, null otherwise.
     */
    val error = post.map {
        verifyPost()
    }.stateIn(
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
            _post.value.photoUrl.isNullOrEmpty() -> FormError.PhotoError
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
            is FormEvent.DescriptionChanged -> { _post.value = _post.value.copy(description = formEvent.description) }
            is FormEvent.TitleChanged -> { _post.value = _post.value.copy(title = formEvent.title) }
            is FormEvent.PhotoChanged -> { _post.value = _post.value.copy(photoUrl = formEvent.photoUrl) }
        }
    }

    /**
     * Attempts to add the current post to the repository after setting the author.
     */
    fun addPost() {
        viewModelScope.launch {

            if (!internetUtils.isInternetAvailable()) {
                Log.e("AddPost", "Failed to access internet, aborting upload.")
                return@launch
            }

            val formError = verifyPost()
            if (formError != null) {
                Log.e("AddPost", "Post validation failed: $formError")
                return@launch
            }

            val user = getUserData().firstOrNull()
            if (user == null) {
                Log.e("AddPost", "Failed to retrieve user, aborting upload.")
                return@launch
            }

            val imageUri = _post.value.photoUrl?.let { Uri.parse(it) }
            if (imageUri == null) {
                Log.e("AddPost", "Invalid image URI, aborting upload.")
                return@launch
            }

            val newPost = _post.value.copy(author = user)

            postRepository.addPost(newPost)
            // Envoyer le post au repository

        }
    }


    /**
     * Retrieves the current user's information from Firebase as a Flow.
     */
    private fun getUserData(): Flow<User?> {
        return userRepository.readCurrentUser()
            .onEach { userRepositoryState ->
                _repositoryState.value = userRepositoryState
            }
            .map { userRepositoryState ->
                when (userRepositoryState) {
                    is UserRepositoryState.GetUserDataSuccess -> userRepositoryState.user
                    is UserRepositoryState.GetUserDataError -> null
                    else -> null
                }
            }
    }






//    private fun isPostWithPhoto(): Boolean {
//        val imageUri = _post.value.photoUrl?.let { Uri.parse(it) }
//        if (imageUri != null) {
//            return true
//        } else {
//            return false
//        }
//    }

}
