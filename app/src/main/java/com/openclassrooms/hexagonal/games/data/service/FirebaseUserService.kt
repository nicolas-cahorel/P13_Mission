package com.openclassrooms.hexagonal.games.data.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.openclassrooms.hexagonal.games.data.repository.UserResult
import com.openclassrooms.hexagonal.games.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

/**
 * FirebaseUserService is an implementation of [UserApi] that provides methods
 * for handling user authentication and management with Firebase Authentication.
 */
class FirebaseUserService : UserApi {

    private val auth = FirebaseAuth.getInstance()

    /**
     * Creates a new user in Firebase Authentication.
     *
     * @param user The user object containing email, password, first name, and last name.
     * @return A Flow emitting [UserResult] indicating success or failure.
     */
    override fun createUser(user: User): Flow<UserResult> = flow {
        try {
            auth.createUserWithEmailAndPassword(user.email, user.password).await()

            auth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName("${user.firstname} ${user.lastname}")
                    .build()
            )?.await()

            emit(UserResult.CreateUserSuccess)
            Log.d("Nicolas", "User successfully created in Firebase and Firestore")

        } catch (exception: Exception) {
            emit(UserResult.CreateUserError(exception))
            Log.d("Nicolas", "User could not be created in Firebase and Firestore")
        }
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return A Flow emitting [UserResult] containing user details or an error.
     */
    override fun readUser(): Flow<UserResult> = flow {
        try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val user = User(
                    id = currentUser.uid,
                    firstname = currentUser.displayName?.substringBefore(" ") ?: "",
                    lastname = currentUser.displayName?.substringAfter(" ") ?: "",
                    email = currentUser.email ?: "",
                    password = ""
                )
                emit(UserResult.ReadUserSuccess(user))
            } else {
                emit(UserResult.ReadUserNotFound)
            }

        } catch (exception: Exception) {
            emit(UserResult.ReadUserError(exception))
            Log.d(
                "Nicolas",
                "exception caught while fetching users data from Firebase and Firestore"
            )
        }
    }

    /**
     * Deletes the currently authenticated user.
     *
     * @return A Flow emitting [UserResult] indicating success or failure.
     */
    override fun deleteUser(): Flow<UserResult> = flow {
        val user = auth.currentUser
        if (user != null) {
            try {
                user.delete().await()
                signOut()
                emit(UserResult.DeleteUserSuccess)
            } catch (exception: Exception) {
                emit(UserResult.DeleteUserError(exception))
            }
        } else {
            emit(UserResult.DeleteUserNotFound)
        }
    }

    /**
     * Authenticates a user with email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A Flow emitting [UserResult] indicating success or failure.
     */
    override fun signIn(email: String, password: String): Flow<UserResult> = flow {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            emit(UserResult.SignInSuccess)
        } catch (exception: Exception) {
            emit(UserResult.SignInError(exception))
        }
    }

    /**
     * Signs out the currently authenticated user.
     *
     * @return A Flow emitting [UserResult] indicating success or failure.
     */
    override fun signOut(): Flow<UserResult> = flow {
        try {
            auth.signOut()
            emit(UserResult.SignOutSuccess)
        } catch (exception: Exception) {
            emit(UserResult.SignOutError(exception))
        }
    }

    /**
     * Checks if a user exists in Firebase Authentication.
     *
     * @param email The email to check.
     * @return A Flow emitting [UserResult] indicating whether the user exists or not.
     */
    override fun doUserExistInFirebase(email: String): Flow<UserResult> = flow {
        try {
            val signInMethods = auth.fetchSignInMethodsForEmail(email).await().signInMethods
            if (signInMethods.isNullOrEmpty()) {
                emit(UserResult.UserInFirebaseNotFound)
            } else {
                emit(UserResult.UserInFirebaseFound)
            }
        } catch (exception: Exception) {
            emit(UserResult.UserInFirebaseError(exception))
        }
    }

    /**
     * Sends a password reset email to the given email address.
     *
     * @param email The email to send the reset link to.
     * @return A Flow emitting [UserResult] indicating success or failure.
     */
    override fun recoverPassword(email: String): Flow<UserResult> = flow {
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(UserResult.RecoverPasswordSuccess)
        } catch (exception: Exception) {
            emit(UserResult.RecoverPasswordError(exception))
        }
    }
}