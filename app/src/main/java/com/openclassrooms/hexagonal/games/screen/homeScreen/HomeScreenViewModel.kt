package com.openclassrooms.hexagonal.games.screen.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
 * ViewModel responsible for managing the state of the Home Screen.
 * It retrieves user data and posts from repositories, ensuring that the UI reflects the correct state.
 *
 * This ViewModel uses a `MutableStateFlow` to expose the current UI state (`HomeScreenState`),
 * which is updated based on network availability and repository responses.
 */
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    internetUtils: InternetUtils,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // Holds the current state of the home screen
    private val _homeScreenState =
        MutableStateFlow<HomeScreenState>(HomeScreenState.Loading(false))

    /**
     * Exposes the home screen state as an immutable StateFlow to be observed by the UI.
     */
    val homeScreenState: StateFlow<HomeScreenState> get() = _homeScreenState

    /**
     * Initializes the ViewModel by checking internet availability and fetching user and post data.
     * If the internet is available, it attempts to read the user data, then fetches posts accordingly.
     * If no internet is available, it sets the state to `InternetUnavailable`.
     */
    init {

        if (internetUtils.isInternetAvailable()) {
            viewModelScope.launch {
                // Fetch user information from the repository
                userRepository.readUser().collect { userResult ->
                    when (userResult) {
                        is UserResult.ReadUserSuccess -> {
                            // User is logged in, now fetch posts
                            postRepository.getPosts().collect { postResult ->
                                when (postResult) {
                                    // Successfully retrieved posts, update UI state
                                    is PostResult.GetPostsSuccess -> {
                                        _homeScreenState.value =
                                            HomeScreenState.DisplayPosts(postResult.posts, true)
                                        Log.d("Nicolas", "posts : ${postResult.posts}")
                                    }
                                    // No posts available, update UI state
                                    else -> _homeScreenState.value =
                                        HomeScreenState.NoPostToDisplay(true)
                                }
                            }

                        }
                        // User data retrieval failed, set UI state accordingly
                        else -> _homeScreenState.value = HomeScreenState.NoPostToDisplay(false)
                    }

                }

            }
            // No internet connection, update UI state
        } else {
            _homeScreenState.value = HomeScreenState.InternetUnavailable(false)
        }
    }
}