package com.openclassrooms.hexagonal.games.domain.model

import java.io.Serializable

/**
 * This class represents a Post data object. It holds information about a post, including its
 * ID, title, description, photo URL, creation timestamp, and the author (User object).
 * The class implements Serializable to allow for potential serialization needs.
 */
data class Post(
    /**
     * Unique identifier for the Post.
     */
    var id: String = "",

    /**
     * Title of the Post.
     */
    var title: String = "",

    /**
     * Optional description for the Post.
     */
    var description: String? = null,

    /**
     * URL of an image associated with the Post, if any.
     */
    var photoUrl: String = "",

    /**
     * Timestamp representing the creation date and time of the Post in milliseconds since epoch.
     */
    var timestamp: Long = 0,

    /**
     * User object representing the author of the Post.
     */
    var author: User? = null

) : Serializable