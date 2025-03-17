package com.openclassrooms.hexagonal.games.domain.model

import java.io.Serializable

/**
 * This class represents a User data object. It holds basic information about a user, including
 * their ID, first name, and last name. The class implements Serializable to allow for potential
 * serialization needs.
 */
data class User(
    /**
     * Unique identifier for the User.
     */
    var id: String = "",

    /**
     * User's first name.
     */
    var firstname: String = "",

    /**
     * User's last name.
     */
    var lastname: String = "",

    /**
     * User's email address.
     */
    var email: String = "",

    /**
     * User's password.
     */
    var password: String = ""

) : Serializable