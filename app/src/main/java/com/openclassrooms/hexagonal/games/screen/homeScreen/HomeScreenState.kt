package com.openclassrooms.hexagonal.games.screen.homeScreen

import com.openclassrooms.hexagonal.games.domain.model.Post

/**
 * Represents the different states of the Home Screen.
 * This sealed class ensures type safety and allows handling different UI states effectively.
 */
sealed class HomeScreenState {

    /**
     * Indicates whether the user is logged in.
     */
    abstract val isUserLoggedIn: Boolean

    /**
     * Represents the loading state of the Home Screen.
     * This state is used while data is being fetched.
     *
     * @property isUserLoggedIn Indicates if the user is logged in.
     */
    data class Loading(
        override val isUserLoggedIn: Boolean
    ) : HomeScreenState()

    /**
     * Represents the state when posts are successfully loaded and displayed.
     *
     * @property posts The list of posts to be displayed.
     * @property isUserLoggedIn Indicates if the user is logged in.
     */
    data class DisplayPosts(
        val posts: List<Post>,
        override val isUserLoggedIn: Boolean
    ) : HomeScreenState()

    /**
     * Represents the state when there are no posts available to display.
     *
     * @property isUserLoggedIn Indicates if the user is logged in.
     */
    data class NoPostToDisplay(
        override val isUserLoggedIn: Boolean
    ) : HomeScreenState()

    /**
     * Represents the state when the internet connection is unavailable.
     *
     * @property isUserLoggedIn Indicates if the user is logged in.
     */
    data class InternetUnavailable(
        override val isUserLoggedIn: Boolean
    ) : HomeScreenState()

}