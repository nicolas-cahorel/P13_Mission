package com.openclassrooms.hexagonal.games.screen.addPostScreen

/**
 * Represents the different states that the Add Post screen can be in.
 * The states manage the UI behavior based on user actions or results of the post creation process.
 */
sealed interface AddPostScreenState {

    /**
     * Represents the state where the input provided by the user is invalid.
     * This includes validation errors for both the title and password fields.
     *
     * @property titleTextFieldLabel Label for the title text field.
     * @property passwordTextFieldLabel Label for the password text field.
     * @property isTitleValid Boolean indicating whether the title field is valid.
     * @property isPasswordValid Boolean indicating whether the password field is valid.
     */
    data class InvalidInput(
        val titleTextFieldLabel: String,
        val passwordTextFieldLabel: String,
        val isTitleValid: Boolean,
        val isPasswordValid: Boolean
    ) : AddPostScreenState

    /**
     * Represents the state when the post has been successfully created.
     */
    data object AddPostSuccess : AddPostScreenState

    /**
     * Represents the state when there is no internet connection while trying to add a post.
     */
    data object AddPostNoInternet : AddPostScreenState

    /**
     * Represents the state when there was an error while trying to add a post.
     */
    data object AddPostError : AddPostScreenState

}