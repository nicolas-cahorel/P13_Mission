package com.openclassrooms.hexagonal.games.data.repository

import com.openclassrooms.hexagonal.games.domain.model.Post

/**
 * Sealed class representing the possible results of post-related operations.
 * This allows for handling success and error states in a structured way.
 */
sealed class PostResult {

    /**
     * Represents a successful retrieval of multiple posts.
     *
     * @property posts The list of retrieved posts.
     */
    data class GetPostsSuccess(val posts: List<Post>) : PostResult()

    /**
     * Represents an error that occurred while fetching posts.
     *
     * @property exception The exception containing error details.
     */
    data class GetPostsError(val exception: Exception) : PostResult()

    /**
     * Represents the case where no posts were found.
     */
    data object GetPostsEmpty : PostResult()

    /**
     * Represents a successful photo upload for a post.
     *
     * @property downloadUri The URL of the uploaded photo.
     */
    data class AddPhotoSuccess(val downloadUri: String) : PostResult()

    /**
     * Represents an error that occurred while uploading a photo.
     *
     * @property exception The exception containing error details.
     */
    data class AddPhotoError(val exception: Exception) : PostResult()

    /**
     * Represents a successful post creation.
     */
    data object AddPostSuccess : PostResult()

    /**
     * Represents an error that occurred while adding a new post.
     *
     * @property exception The exception containing error details.
     */
    data class AddPostError(val exception: Exception) : PostResult()

    /**
     * Represents a successful retrieval of a single post.
     *
     * @property post The retrieved post.
     */
    data class GetPostSuccess(val post: Post) : PostResult()

    /**
     * Represents the case where the post was found.
     */
    data object GetPostNotFound : PostResult()

    /**
     * Represents an error that occurred while fetching a single post.
     *
     * @property exception The exception containing error details.
     */
    data class GetPostError(val exception: Exception) : PostResult()

}