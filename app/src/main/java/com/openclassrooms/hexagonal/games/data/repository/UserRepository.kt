package com.openclassrooms.hexagonal.games.data.repository

import com.openclassrooms.hexagonal.games.data.service.UserApi
import com.openclassrooms.hexagonal.games.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing user-related operations.
 * This class provides an abstraction layer between the data source (UserApi)
 * and the rest of the application, ensuring clean architecture principles.
 * It is marked as a Singleton to ensure a single instance is used throughout the app.
 *
 * @param userApi The API interface for handling user operations.
 */
@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi
) {

    /**
     * Creates a new user in Firebase.
     *
     * @param user The user data to be created.
     * @return A [Flow] emitting the result of the user creation operation.
     */
    fun createUser(user: User): Flow<UserResult> = userApi.createUser(user)

    /**
     * Reads the currently authenticated user from Firebase.
     *
     * @return A [Flow] emitting the result of the user retrieval operation.
     */
    fun readUser(): Flow<UserResult> = userApi.readUser()

    /**
     * Deletes the currently authenticated user from Firebase.
     *
     * @return A [Flow] emitting the result of the user deletion operation.
     */
    fun deleteUser(): Flow<UserResult> = userApi.deleteUser()

    /**
     * Signs in a user with the provided email and password.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @return A [Flow] emitting the result of the sign-in operation.
     */
    fun signIn(email: String, password: String): Flow<UserResult> = userApi.signIn(email, password)

    /**
     * Signs out the currently authenticated user.
     *
     * @return A [Flow] emitting the result of the sign-out operation.
     */
    fun signOut(): Flow<UserResult> = userApi.signOut()

    /**
     * Checks if a user exists in Firebase by their email.
     *
     * @param email The email to check in Firebase.
     * @return A [Flow] emitting the result of the existence check operation.
     */
    fun doUserExistInFirebase(email: String): Flow<UserResult> =
        userApi.doUserExistInFirebase(email)

    /**
     * Initiates a password recovery process for the given email.
     *
     * @param email The email of the user who wants to recover their password.
     * @return A [Flow] emitting the result of the password recovery operation.
     */
    fun recoverPassword(email: String): Flow<UserResult> = userApi.recoverPassword(email)

}