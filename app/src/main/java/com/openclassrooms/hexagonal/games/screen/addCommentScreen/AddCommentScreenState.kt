package com.openclassrooms.hexagonal.games.screen.addCommentScreen

/**
 * Represents the different states that the Add Comment screen can be in.
 * These states dictate the UI behavior based on user actions and the comment submission process.
 */
sealed interface AddCommentScreenState {

    /**
     * Represents the state where the user input is invalid.
     * This may occur when the comment content is empty or does not meet validation criteria.
     */
    data object InvalidInput : AddCommentScreenState

    /**
     * Represents the state where the user input is valid.
     * This indicates that the comment content meets all validation requirements.
     */
    data object ValidInput : AddCommentScreenState

    /**
     * Represents the state when a comment has been successfully added.
     */
    data object AddCommentSuccess : AddCommentScreenState

    /**
     * Represents the state when there is no internet connection while attempting to add a comment.
     */
    data object AddCommentNoInternet : AddCommentScreenState

    /**
     * Represents the state when an error occurs while trying to add a comment.
     * This could be due to network issues, Firestore errors, or an unexpected failure.
     */
    data object AddCommentError : AddCommentScreenState

}