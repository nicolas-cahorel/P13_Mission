package com.openclassrooms.hexagonal.games.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserProfileChangeRequest
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.domain.model.User
import com.openclassrooms.hexagonal.games.utils.ResourceProvider
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    private val auth = FirebaseAuth.getInstance()

    // fonction ok
    fun createUser(user: User) = flow {
        try {

            if (user.email.isNullOrEmpty() || user.password.isNullOrEmpty()) {
                emit(
                    UserRepositoryState.CreateUserError(resourceProvider.getString(R.string.toast_create_account_error))
                )
                return@flow
            }

            auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val firebaseUser = auth.currentUser

            if (firebaseUser != null) {
                firebaseUser.updateProfile(
                    UserProfileChangeRequest
                        .Builder()
                        .setDisplayName("${user.firstname} ${user.lastname}")
                        .build()
                ).await()
                emit(UserRepositoryState.CreateUserSuccess)
            } else {
                emit(UserRepositoryState.CreateUserError(resourceProvider.getString(R.string.toast_create_account_error)))
            }

        } catch (exception: Exception) {
            emit(
                UserRepositoryState.CreateUserError(
                    exception.message ?: resourceProvider.getString(R.string.toast_create_account_error)
                )
            )
        }
    }


    // fonction ok
    fun readCurrentUser() = flow {
        try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val user = User(
                    id = currentUser.uid,
                    firstname = currentUser.displayName?.substringBefore(" "),
                    lastname = currentUser.displayName?.substringAfter(" "),
                    email = currentUser.email,
                    password = null
                )
                emit(UserRepositoryState.GetUserDataSuccess(user))
            } else {
                emit(UserRepositoryState.GetUserDataError("Unable to get user from Firebase"))
            }
        } catch (e: Exception) {
            emit(
                UserRepositoryState.GetUserDataError(
                    e.message ?: "Error while reading current user"
                )
            )
        }
    }

    // fonction ok
    fun deleteUser() = flow {
        try {
            val user = auth.currentUser
            if (user != null) {
                user.delete().await()
                signOut()
                emit(UserRepositoryState.DeleteUserSuccess)
            }
        } catch (e: Exception) {
            emit(UserRepositoryState.DeleteUserError(resourceProvider.getString(R.string.toast_error)))
        }
    }


    // fonction ok
    fun signIn(user: User) = flow {
        if (!user.email.isNullOrEmpty() && !user.password.isNullOrEmpty()) {

            try {
                auth.signInWithEmailAndPassword(user.email, user.password).await()
                emit(UserRepositoryState.SignInSuccess)
            } catch (e: Exception) {

                if (e is FirebaseAuthException && e.errorCode == "ERROR_WRONG_PASSWORD") {
                    emit(UserRepositoryState.SignInError(resourceProvider.getString(R.string.error_password_incorrect)))
                } else {
                    emit(UserRepositoryState.SignInError(resourceProvider.getString(R.string.error_unknown)))
                }
            }
        }
    }


    // fonction ok
    fun signOut() = flow {
        try {
            auth.signOut()
            emit(UserRepositoryState.SignOutSuccess)
        } catch (e: Exception) {
            emit(
                UserRepositoryState.SignOutError(
                    e.message ?: resourceProvider.getString(R.string.toast_error)
                )
            )
        }
    }

    fun doUserExistInFirebase(email: String) = flow {
        try {
            val task = auth.fetchSignInMethodsForEmail(email).await()
            if (task.signInMethods?.isNotEmpty() == true) {
                emit(UserRepositoryState.UserFoundInFirebase)
            } else {
                emit(UserRepositoryState.UserNotFoundInFirebase)
            }
        } catch (exception: Exception) {
            emit(
                UserRepositoryState.UserException(
                    resourceProvider.getString(R.string.toast_error)
                )
            )
        }
    }


    fun recoverPassword(email: String) = flow {
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(UserRepositoryState.RecoverPasswordSuccess)
        } catch (e: Exception) {
            emit(UserRepositoryState.RecoverPasswordError(resourceProvider.getString(R.string.toast_error)))
        }
    }




}