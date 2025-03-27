package com.openclassrooms.hexagonal.games.data.service

import com.openclassrooms.hexagonal.games.data.repository.CommentResult
import com.openclassrooms.hexagonal.games.domain.model.Comment
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for comment-related operations.
 * This interface provides methods for fetching and adding comments.
 */
interface CommentApi {

    /**
     * Retrieves comments for a specific post, ordered by their creation date in ascending order.
     *
     * @param postId The ID of the post for which comments are to be retrieved.
     * @return A [Flow] emitting a [CommentResult] containing the list of comments or an error.
     */
    fun getCommentsOrderByCreationDate(postId: String): Flow<CommentResult>

    /**
     * Adds a new comment to the database.
     *
     * @param comment The comment to be added.
     * @return A [Flow] emitting a [CommentResult] indicating the success or failure of the operation.
     */
    fun addComment(comment: Comment): Flow<CommentResult>

}