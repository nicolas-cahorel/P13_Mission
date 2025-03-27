package com.openclassrooms.hexagonal.games.data.repository

import com.openclassrooms.hexagonal.games.data.service.CommentApi
import com.openclassrooms.hexagonal.games.domain.model.Comment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class responsible for handling comment-related operations.
 * This class abstracts the data source (Firebase) and provides methods to interact with comments.
 *
 * @property commentApi The API service used to fetch and store comment data.
 */
@Singleton
class CommentRepository @Inject constructor(
    private val commentApi: CommentApi
) {

    /**
     * Retrieves a list of comments associated with a specific post, ordered by their creation date.
     *
     * @param postId The ID of the post for which comments are to be fetched.
     * @return A Flow emitting the result of the fetch operation, containing the list of comments.
     */
    fun getComments(postId : String): Flow<CommentResult> = commentApi.getCommentsOrderByCreationDate(postId)

    /**
     * Adds a new comment to the data source.
     *
     * @param comment The comment object containing details such as content, author, and timestamp.
     * @return A Flow emitting the result of the add operation.
     */
    fun addComment(comment: Comment): Flow<CommentResult> = commentApi.addComment(comment)

}