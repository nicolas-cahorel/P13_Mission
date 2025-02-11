package com.openclassrooms.hexagonal.games.data.repository

import com.openclassrooms.hexagonal.games.domain.model.User

sealed class UserRepositoryState {

    data object Waiting : UserRepositoryState()

    data object CreateUserSuccess : UserRepositoryState()

    data class  CreateUserError(val message: String) : UserRepositoryState()

    data class GetUserDataSuccess(val user: User) : UserRepositoryState()

    data class GetUserDataError(val message: String) : UserRepositoryState()

    data object DeleteUserSuccess: UserRepositoryState()

    data class DeleteUserError(val message: String) : UserRepositoryState()

    data object UserFoundInFirebase : UserRepositoryState()

    data object UserNotFoundInFirebase : UserRepositoryState()

    data class UserException(val message: String) : UserRepositoryState()

    data object SignInSuccess :UserRepositoryState()

    data class  SignInError(val message: String) : UserRepositoryState()

    data object  SignOutSuccess: UserRepositoryState()

    data class SignOutError(val message: String) : UserRepositoryState()

    data object RecoverPasswordSuccess : UserRepositoryState()

    data class  RecoverPasswordError(val message: String) : UserRepositoryState()



}