package com.openclassrooms.hexagonal.games.data.repository

import com.openclassrooms.hexagonal.games.data.service.PostApi
import com.openclassrooms.hexagonal.games.domain.model.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class responsible for handling post-related operations.
 * This class abstracts the data source (Firebase) and provides methods to interact with posts.
 *
 * @property postApi The API service used to fetch and store post data.
 */
@Singleton
class PostRepository @Inject constructor(
    private val postApi: PostApi
) {

    /**
     * Retrieves a list of posts ordered by their creation date.
     *
     * @return A Flow emitting the result of the fetch operation.
     */
    fun getPosts(): Flow<PostResult> = postApi.getPostsOrderByCreationDate()

    /**
     * Adds a new post to the data source.
     *
     * @param post The post object containing details such as title, description, and timestamp.
     * @return A Flow emitting the result of the add operation.
     */
    fun addPost(post: Post): Flow<PostResult> = postApi.addPost(post)

    /**
     * Uploads a photo associated with a post to cloud storage.
     *
     * @param post The post containing the photo URL.
     * @return A Flow emitting the result of the upload operation.
     */
    fun addPhoto(post: Post): Flow<PostResult> = postApi.addPhoto(post)

    /**
     * Retrieves a specific post by its unique identifier.
     *
     * @param postId The unique identifier of the post to retrieve.
     * @return A Flow emitting the result of the fetch operation.
     */
    fun getPost(postId: String) : Flow<PostResult> = postApi.getPost(postId)

}