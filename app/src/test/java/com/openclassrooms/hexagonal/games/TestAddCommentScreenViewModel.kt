package com.openclassrooms.hexagonal.games

import androidx.lifecycle.SavedStateHandle
import com.openclassrooms.hexagonal.games.data.repository.CommentRepository
import com.openclassrooms.hexagonal.games.data.repository.CommentResult
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserResult
import com.openclassrooms.hexagonal.games.domain.model.User
import com.openclassrooms.hexagonal.games.screen.addCommentScreen.AddCommentScreenState
import com.openclassrooms.hexagonal.games.screen.addCommentScreen.AddCommentScreenViewModel
import com.openclassrooms.hexagonal.games.utils.InternetUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
import org.mockito.kotlin.any

/**
 * Unit tests for [AddCommentScreenViewModel].
 * These tests verify the behavior of the viewmodel when interacting with the [CommentRepository].
 */
@ExperimentalCoroutinesApi
class TestAddCommentScreenViewModel {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockCommentRepository: CommentRepository

    @Mock
    private lateinit var mockInternetUtils: InternetUtils

    @Mock
    private lateinit var mockUserRepository: UserRepository
    private lateinit var viewModel: AddCommentScreenViewModel

    /**
     * Sets up the test environment before each test.
     * Initializes the mock objects and sets the main coroutine dispatcher.
     */
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        val savedStateHandle = SavedStateHandle(mapOf("postId" to "123"))
        viewModel = AddCommentScreenViewModel(
            savedStateHandle = savedStateHandle,
            internetUtils = mockInternetUtils,
            userRepository = mockUserRepository,
            commentRepository = mockCommentRepository
        )
    }

    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Tests [AddCommentScreenViewModel.onContentChanged] when the repository returns a valid result.
     */
    @Test
    fun onContentChanged_ReturnsValid() = runTest {
        // ARRANGE
        val fakeContent = "This is the comment content"
        val expectedState = AddCommentScreenState.ValidInput
        // ACT
        viewModel.onContentChanged(fakeContent)
        val collectedState = viewModel.addCommentScreenState.first()
        // ASSERT
        Assertions.assertEquals(expectedState, collectedState)
    }

    /**
     * Tests [AddCommentScreenViewModel.onContentChanged] when the repository returns an invalid result.
     */
    @Test
    fun onContentChanged_ReturnsInvalid() = runTest {
        // ARRANGE
        val fakeContent = ""
        val expectedState = AddCommentScreenState.InvalidInput
        // ACT
        viewModel.onContentChanged(fakeContent)
        val collectedState = viewModel.addCommentScreenState.first()
        // ASSERT
        Assertions.assertEquals(expectedState, collectedState)
    }

    /**
     * Tests [AddCommentScreenViewModel.addComment] when the repository returns a successful result.
     */
    @Test
    fun addComment_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakeUser = User(
            id = "1",
            firstname = "Jim",
            lastname = "Nastyk",
            email = "jim.nastyk@test.com",
            password = ""
        )
        val expectedState = AddCommentScreenState.AddCommentSuccess
        // Mocking dependencies
        `when`(mockInternetUtils.isInternetAvailable()).thenReturn(true)
        `when`(mockUserRepository.readUser()).thenReturn(flow { emit(UserResult.ReadUserSuccess(fakeUser)) })
        `when`(mockCommentRepository.addComment(any())).thenReturn(flow { emit(CommentResult.AddCommentSuccess) })
        // ACT
        viewModel.addComment()
        advanceUntilIdle()
        val collectedState = viewModel.addCommentScreenState.first()
        // ASSERT
        Assertions.assertEquals(expectedState, collectedState)
    }

    /**
     * Tests [AddCommentScreenViewModel.addComment] when the repository returns an error result.
     */
    @Test
    fun addComment_ReturnsError() = runTest {
        // ARRANGE
        val fakeUser = User(
            id = "1",
            firstname = "Jim",
            lastname = "Nastyk",
            email = "jim.nastyk@test.com",
            password = ""
        )
        val fakeError = Exception("error while adding comment")
        val expectedState = AddCommentScreenState.AddCommentError
        // Mocking dependencies
        `when`(mockInternetUtils.isInternetAvailable()).thenReturn(true)
        `when`(mockUserRepository.readUser()).thenReturn(flow { emit(UserResult.ReadUserSuccess(fakeUser)) })
        `when`(mockCommentRepository.addComment(any())).thenReturn(flow { emit(CommentResult.AddCommentError(fakeError)) })
        // ACT
        viewModel.addComment()
        advanceUntilIdle()
        val collectedState = viewModel.addCommentScreenState.first()
        // ASSERT
        Assertions.assertEquals(expectedState, collectedState)
    }

}