package com.openclassrooms.hexagonal.games.data.service

import com.openclassrooms.hexagonal.games.data.repository.PostResult
import com.openclassrooms.hexagonal.games.domain.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for post-related operations.
 */
interface PostApi {

    /**
     * Retrieves posts ordered by their creation date in descending order.
     *
     * @return A [Flow] emitting a [PostResult] containing the list of posts or an error.
     */
    fun getPostsOrderByCreationDate(): Flow<PostResult>

    /**
     * Uploads a photo associated with a post.
     *
     * @param post The post containing the photo URL to be uploaded.
     * @return A [Flow] emitting a [PostResult] indicating the success or failure of the upload.
     */
    fun addPhoto(post: Post): Flow<PostResult>

    /**
     * Adds a new post to the database.
     *
     * @param post The post to be added.
     * @return A [Flow] emitting a [PostResult] indicating the success or failure of the operation.
     */
    fun addPost(post: Post): Flow<PostResult>

    /**
     * Retrieves a specific post.
     *
     * @return A [Flow] emitting a [PostResult] containing the requested post or an error.
     * @note This method might need a parameter like `postId` to specify which post to retrieve.
     */
    fun getPost(postId: String): Flow<PostResult>

}