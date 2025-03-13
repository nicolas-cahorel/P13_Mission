package com.openclassrooms.hexagonal.games.data.repository

import com.openclassrooms.hexagonal.games.domain.model.User

/**
 * Sealed class representing the different results that can occur
 * when performing user-related operations.
 *
 * This class is used to encapsulate success, failure, and edge cases
 * for user operations such as creation, retrieval, deletion, authentication,
 * and password recovery.
 */
sealed class UserResult {

    /**
     * Represents a successful user creation.
     */
    data object CreateUserSuccess : UserResult()

    /**
     * Represents an error that occurred during user creation.
     *
     * @param exception The exception detailing the cause of the failure.
     */
    data class CreateUserError(val exception: Exception) : UserResult()

    /**
     * Represents a successful retrieval of user data.
     *
     * @param user The retrieved user data.
     */
    data class ReadUserSuccess(val user: User) : UserResult()

    /**
     * Indicates that the requested user was not found in the database.
     */
    data object ReadUserNotFound : UserResult()

    /**
     * Represents an error that occurred during user retrieval.
     *
     * @param exception The exception detailing the cause of the failure.
     */
    data class ReadUserError(val exception: Exception) : UserResult()

    /**
     * Represents a successful user deletion.
     */
    data object DeleteUserSuccess : UserResult()

    /**
     * Indicates that the user to be deleted was not found.
     */
    data object DeleteUserNotFound : UserResult()

    /**
     * Represents an error that occurred during user deletion.
     *
     * @param exception The exception detailing the cause of the failure.
     */
    data class DeleteUserError(val exception: Exception) : UserResult()

    /**
     * Represents a successful user sign-in.
     */
    data object SignInSuccess : UserResult()

    /**
     * Represents an error that occurred during user sign-in.
     *
     * @param exception The exception detailing the cause of the failure.
     */
    data class SignInError(val exception: Exception) : UserResult()

    /**
     * Represents a successful user sign-out.
     */
    data object SignOutSuccess : UserResult()

    /**
     * Represents an error that occurred during user sign-out.
     *
     * @param exception The exception detailing the cause of the failure.
     */
    data class SignOutError(val exception: Exception) : UserResult()

    /**
     * Indicates that a user was found in Firebase by their email.
     */
    data object UserInFirebaseFound : UserResult()

    /**
     * Indicates that a user was not found in Firebase by their email.
     */
    data object UserInFirebaseNotFound : UserResult()

    /**
     * Represents an error that occurred while checking user existence in Firebase.
     *
     * @param exception The exception detailing the cause of the failure.
     */
    data class UserInFirebaseError(val exception: Exception) : UserResult()

    /**
     * Represents a successful password recovery operation.
     */
    data object RecoverPasswordSuccess : UserResult()

    /**
     * Represents an error that occurred during password recovery.
     *
     * @param exception The exception detailing the cause of the failure.
     */
    data class RecoverPasswordError(val exception: Exception) : UserResult()

}