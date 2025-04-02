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

            Log.d("Nicolas", "FirebaseUserService - createUser() : User successfully created in Firebase.")
            emit(UserResult.CreateUserSuccess)
        } catch (exception: Exception) {
            Log.d("Nicolas", "FirebaseUserService - createUser() : Error occurred while uploading user in Firebase.", exception)
            emit(UserResult.CreateUserError(exception))
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
                Log.d("Nicolas", "FirebaseUserService - readUser() : Successfully retrieved user from Firebase.")
                emit(UserResult.ReadUserSuccess(user))
            } else {
                Log.d("Nicolas", "FirebaseUserService - readUser() : No user found in Firebase.")
                emit(UserResult.ReadUserNotFound)
            }
        } catch (exception: Exception) {
            Log.e("Nicolas", "FirebaseUserService - readUser() : Error occurred while fetching user from Firebase.", exception)
            emit(UserResult.ReadUserError(exception))
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

                Log.d("Nicolas", "FirebaseUserService - deleteUser() : Successfully deleted user from Firebase.")
                emit(UserResult.DeleteUserSuccess)
            } catch (exception: Exception) {
                Log.e("Nicolas", "FirebaseUserService - deleteUser() : Error occurred while deleting user from Firebase.", exception)
                emit(UserResult.DeleteUserError(exception))
            }
        } else {
            Log.d("Nicolas", "FirebaseUserService - deleteUser() : No user found in Firebase.")
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

            Log.d("Nicolas", "FirebaseUserService - signIn() : Successfully signed in with Firebase.")
            emit(UserResult.SignInSuccess)
        } catch (exception: Exception) {
            Log.e("Nicolas", "FirebaseUserService - signIn() : Error occurred while signing in with Firebase.", exception)
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

            Log.d("Nicolas", "FirebaseUserService - signOut() : Successfully signed out with Firebase.")
            emit(UserResult.SignOutSuccess)
        } catch (exception: Exception) {
            Log.e("Nicolas", "FirebaseUserService - signOut() : Error occurred while signing out with Firebase.", exception)
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
                Log.d("Nicolas", "FirebaseUserService - doUserExistInFirebase() : No user account found in Firebase.")
                emit(UserResult.UserInFirebaseNotFound)
            } else {
                Log.d("Nicolas", "FirebaseUserService - doUserExistInFirebase() : User account found in Firebase.")
                emit(UserResult.UserInFirebaseFound)
            }
        } catch (exception: Exception) {
            Log.e("Nicolas", "FirebaseUserService - doUserExistInFirebase() : Error checking user existence in Firebase.", exception)
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

            Log.d("Nicolas", "FirebaseUserService - recoverPassword() : Successfully send email for password recovery.")
            emit(UserResult.RecoverPasswordSuccess)
        } catch (exception: Exception) {
            Log.e("Nicolas", "FirebaseUserService - recoverPassword() : Error sending email for password recovery.", exception)
            emit(UserResult.RecoverPasswordError(exception))
        }
    }
}