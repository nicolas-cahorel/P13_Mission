package com.openclassrooms.hexagonal.games

import com.openclassrooms.hexagonal.games.data.repository.CommentRepository
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserResult
import com.openclassrooms.hexagonal.games.data.service.CommentApi
import com.openclassrooms.hexagonal.games.data.service.UserApi
import com.openclassrooms.hexagonal.games.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Unit tests for [CommentRepository].
 * These tests verify the behavior of the repository when interacting with the [CommentApi].
 */
@ExperimentalCoroutinesApi
class TestUserRepository {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockUserApi: UserApi
    private lateinit var userRepository: UserRepository

    /**
     * Sets up the test environment before each test.
     * Initializes the mock objects and sets the main coroutine dispatcher.
     */
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        userRepository = UserRepository(mockUserApi)
    }

    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Tests [UserRepository.createUser] when the API returns a successful result.
     */
    @Test
    fun createUser_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakeUser = User(
            id = "1",
            firstname = "Jim",
            lastname = "Nastyk",
            email = "jim.nastyk@gmail.com",
            password = "1234"
        )
        val expectedResult = UserResult.CreateUserSuccess
        // Mocking dependencies
        `when`(mockUserApi.createUser(fakeUser)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.createUser(fakeUser).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.createUser] when the API returns an error result.
     */
    @Test
    fun createUser_ReturnsError() = runTest {
        // ARRANGE
        val fakeUser = User(
            id = "1",
            firstname = "Jim",
            lastname = "Nastyk",
            email = "jim.nastyk@gmail.com",
            password = "1234"
        )
        val expectedResult = UserResult.CreateUserError(
            Exception("error while creating new user")
        )
        // Mocking dependencies
        `when`(mockUserApi.createUser(fakeUser)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.createUser(fakeUser).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.readUser] when the API returns a successful result.
     */
    @Test
    fun readUser_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakeUser = User(
            id = "1",
            firstname = "Jim",
            lastname = "Nastyk",
            email = "jim.nastyk@gmail.com",
            password = "1234"
        )
        val expectedResult = UserResult.ReadUserSuccess(fakeUser)
        // Mocking dependencies
        `when`(mockUserApi.readUser()).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.readUser().first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.readUser] when the API returns an error result.
     */
    @Test
    fun readUser_ReturnsError() = runTest {
        // ARRANGE
        val expectedResult = UserResult.ReadUserError(
            Exception("error while fetching user data")
        )
        // Mocking dependencies
        `when`(mockUserApi.readUser()).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.readUser().first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.deleteUser] when the API returns a successful result.
     */
    @Test
    fun deleteUser_ReturnsSuccess() = runTest {
        // ARRANGE
        val expectedResult = UserResult.DeleteUserSuccess
        // Mocking dependencies
        `when`(mockUserApi.deleteUser()).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.deleteUser().first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.deleteUser] when the API returns an error result.
     */
    @Test
    fun deleteUser_ReturnsError() = runTest {
        // ARRANGE
        val expectedResult = UserResult.DeleteUserError(
            Exception("error while deleting user")
        )
        // Mocking dependencies
        `when`(mockUserApi.deleteUser()).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.deleteUser().first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.signIn] when the API returns a successful result.
     */
    @Test
    fun signIn_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakeEmail = "jim.nastyk@gmail.com"
        val fakePassword = "1234"
        val expectedResult = UserResult.SignInSuccess
        // Mocking dependencies
        `when`(mockUserApi.signIn(fakeEmail, fakePassword)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.signIn(fakeEmail, fakePassword).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.signIn] when the API returns an error result.
     */
    @Test
    fun signIn_ReturnsError() = runTest {
        // ARRANGE
        val fakeEmail = "jim.nastyk@gmail.com"
        val fakePassword = "1234"
        val expectedResult = UserResult.SignInError(
            Exception("error while signing in")
        )
        // Mocking dependencies
        `when`(mockUserApi.signIn(fakeEmail, fakePassword)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.signIn(fakeEmail, fakePassword).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.signOut] when the API returns a successful result.
     */
    @Test
    fun signOut_ReturnsSuccess() = runTest {
        // ARRANGE
        val expectedResult = UserResult.SignOutSuccess
        // Mocking dependencies
        `when`(mockUserApi.signOut()).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.signOut().first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.signOut] when the API returns an error result.
     */
    @Test
    fun signOut_ReturnsError() = runTest {
        // ARRANGE
        val expectedResult = UserResult.SignOutError(
            Exception("error while signing out")
        )
        // Mocking dependencies
        `when`(mockUserApi.signOut()).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.signOut().first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.doUserExistInFirebase] when the API returns a successful result.
     */
    @Test
    fun doUserExistInFirebase_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakeEmail = "jim.nastyk@gmail.com"
        val expectedResult = UserResult.UserInFirebaseFound
        // Mocking dependencies
        `when`(mockUserApi.doUserExistInFirebase(fakeEmail)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.doUserExistInFirebase(fakeEmail).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.doUserExistInFirebase] when the API returns an error result.
     */
    @Test
    fun doUserExistInFirebase_ReturnsError() = runTest {
        // ARRANGE
        val fakeEmail = "jim.nastyk@gmail.com"
        val expectedResult = UserResult.UserInFirebaseNotFound
        // Mocking dependencies
        `when`(mockUserApi.doUserExistInFirebase(fakeEmail)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.doUserExistInFirebase(fakeEmail).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.recoverPassword] when the API returns a successful result.
     */
    @Test
    fun recoverPassword_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakeEmail = "jim.nastyk@gmail.com"
        val expectedResult = UserResult.RecoverPasswordSuccess
        // Mocking dependencies
        `when`(mockUserApi.recoverPassword(fakeEmail)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.recoverPassword(fakeEmail).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [UserRepository.recoverPassword] when the API returns an error result.
     */
    @Test
    fun recoverPassword_ReturnsError() = runTest {
        // ARRANGE
        val fakeEmail = "jim.nastyk@gmail.com"
        val expectedResult = UserResult.RecoverPasswordError(
            Exception("error while recovering password")
        )
        // Mocking dependencies
        `when`(mockUserApi.recoverPassword(fakeEmail)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = userRepository.recoverPassword(fakeEmail).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

}