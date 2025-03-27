package com.openclassrooms.hexagonal.games.data.repository

import com.openclassrooms.hexagonal.games.domain.model.Comment

/**
 * Sealed class representing the possible results of comment-related operations.
 * This allows for handling success and error states in a structured way.
 */
sealed class CommentResult {

    /**
     * Represents a successful retrieval of multiple comments.
     *
     * @property comments The list of retrieved comments.
     */
    data class GetCommentsSuccess(val comments: List<Comment>) : CommentResult()

    /**
     * Represents an error that occurred while fetching comments.
     *
     * @property exception The exception containing error details.
     */
    data class GetCommentsError(val exception: Exception) : CommentResult()

    /**
     * Represents the case where no comments were found for the given post.
     */
    data object GetCommentsEmpty : CommentResult()

    /**
     * Represents a successful comment creation.
     */
    data object AddCommentSuccess : CommentResult()

    /**
     * Represents an error that occurred while adding a new comment.
     *
     * @property exception The exception containing error details.
     */
    data class AddCommentError(val exception: Exception) : CommentResult()

}