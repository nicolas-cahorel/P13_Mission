package com.openclassrooms.hexagonal.games.domain.model

import java.io.Serializable

/**
 * This class represents a Comment data object. It holds information about a comment, including its
 * ID, the associated post ID, content, timestamp, and the author (User object).
 * The class implements [Serializable] to allow potential serialization and deserialization.
 */
class Comment(

    /**
     * Unique identifier for the Comment.
     */
    val id: String = "",

    /**
     * Identifier that links the Comment to a Post.
     */
    val postId: String = "",

    /**
     * Content of the Comment.
     */
    val content: String = "",

    /**
     * Timestamp representing the creation date and time of the Comment in milliseconds since epoch.
     */
    val timestamp: Long = 0,

    /**
     * User object representing the author of the Post.
     */
    val author: User? = null

) : Serializable