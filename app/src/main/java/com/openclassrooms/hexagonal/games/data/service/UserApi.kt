package com.openclassrooms.hexagonal.games.data.service

import com.openclassrooms.hexagonal.games.data.repository.UserResult
import com.openclassrooms.hexagonal.games.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining user-related operations for authentication and user management.
 */
interface UserApi {

    /**
     * Creates a new user in the authentication system.
     * @param user The user data containing email, password, and other details.
     * @return A [Flow] emitting [UserResult] indicating success or failure.
     */
    fun createUser(user: User): Flow<UserResult>

    /**
     * Retrieves the currently authenticated user.
     * @return A [Flow] emitting [UserResult] with the user data if found, or an error if not.
     */
    fun readUser(): Flow<UserResult>

    /**
     * Deletes the currently authenticated user.
     * @return A [Flow] emitting [UserResult] indicating success, failure, or user not found.
     */
    fun deleteUser(): Flow<UserResult>

    /**
     * Signs in a user with the given email and password.
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Flow] emitting [UserResult] indicating success or failure.
     */
    fun signIn(email: String, password: String): Flow<UserResult>

    /**
     * Signs out the currently authenticated user.
     * @return A [Flow] emitting [UserResult] indicating whether the sign-out was successful.
     */
    fun signOut(): Flow<UserResult>

    /**
     * Checks if a user exists in Firebase authentication.
     * @param email The email address to check.
     * @return A [Flow] emitting [UserResult] indicating if the user was found or not.
     */
    fun doUserExistInFirebase(email: String): Flow<UserResult>

    /**
     * Sends a password recovery email to the specified email address.
     * @param email The email address for password recovery.
     * @return A [Flow] emitting [UserResult] indicating success or failure.
     */
    fun recoverPassword(email: String): Flow<UserResult>

}