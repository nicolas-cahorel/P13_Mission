package com.openclassrooms.hexagonal.games.screen.postDetailsScreen

import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.Post

/**
 * Represents the different states of the Post Details Screen.
 * This sealed class ensures type safety and enables efficient UI state management.
 */
sealed class PostDetailsScreenState {

    /**
     * Indicates whether an internet connection is available.
     */
    abstract val isInternetAvailable: Boolean

    /**
     * Indicates whether the user is logged in.
     */
    abstract val isUserLoggedIn: Boolean

    /**
     * Represents the loading state of the Post Details Screen.
     * This state is used while fetching data.
     *
     * @property isInternetAvailable Indicates if an internet connection is available.
     * @property isUserLoggedIn Indicates if the user is logged in.
     */
    data class Loading(
        override val isInternetAvailable: Boolean,
        override val isUserLoggedIn: Boolean
    ) : PostDetailsScreenState()

    /**
     * Represents the state when post details and comments are successfully loaded and displayed.
     *
     * @property isInternetAvailable Indicates if an internet connection is available.
     * @property isUserLoggedIn Indicates if the user is logged in.
     * @property post The post to be displayed.
     * @property comments The list of comments associated with the post.
     */
    data class DisplayPostDetails(
        override val isInternetAvailable: Boolean,
        override val isUserLoggedIn: Boolean,
        val post: Post,
        val comments: List<Comment>
    ) : PostDetailsScreenState()

    /**
     * Represents the state when an error occurs while fetching post details or comments.
     *
     * @property isInternetAvailable Indicates if an internet connection is available.
     * @property isUserLoggedIn Indicates if the user is logged in.
     * @property errorMessage A message describing the error.
     */
    data class ErrorWhileFetchingData(
        override val isInternetAvailable: Boolean,
        override val isUserLoggedIn: Boolean,
        val errorMessage: String
    ) : PostDetailsScreenState()

}