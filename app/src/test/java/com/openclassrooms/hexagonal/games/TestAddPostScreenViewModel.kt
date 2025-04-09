package com.openclassrooms.hexagonal.games

import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.data.repository.PostResult
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.repository.UserResult
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User
import com.openclassrooms.hexagonal.games.screen.addPostScreen.AddPostScreenState
import com.openclassrooms.hexagonal.games.screen.addPostScreen.AddPostScreenViewModel
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
 * Unit tests for [AddPostScreenViewModel].
 * These tests verify the behavior of the viewmodel when interacting with the [PostRepository].
 */
@ExperimentalCoroutinesApi
class TestAddPostScreenViewModel {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockInternetUtils: InternetUtils

    @Mock
    private lateinit var mockUserRepository: UserRepository

    @Mock
    private lateinit var mockPostRepository: PostRepository
    private lateinit var viewModel: AddPostScreenViewModel

    /**
     * Sets up the test environment before each test.
     * Initializes the mock objects and sets the main coroutine dispatcher.
     */
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = AddPostScreenViewModel(
            internetUtils = mockInternetUtils,
            userRepository = mockUserRepository,
            postRepository = mockPostRepository
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
     * Tests [AddPostScreenViewModel.addPost] when the repository returns a successful result.
     */
    @Test
    fun addPost_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakeUser = User(
            id = "1",
            firstname = "Jim",
            lastname = "Nastyk",
            email = "jim.nastyk@test.com",
            password = ""
        )
        val fakePost = Post(
            id = "1",
            title = "post 1",
            description = "description of mocked post 1",
            photoUrl = "www.google.fr",
            timestamp = 123456789,
            author = fakeUser
        )
        val expectedState = AddPostScreenState.AddPostSuccess
        // Mocking dependencies
        `when`(mockInternetUtils.isInternetAvailable()).thenReturn(true)
        `when`(mockPostRepository.addPhoto(any())).thenReturn(flow { emit(PostResult.AddPhotoSuccess(fakePost.photoUrl)) })
        `when`(mockUserRepository.readUser()).thenReturn(flow { emit(UserResult.ReadUserSuccess(fakeUser)) })
        `when`(mockPostRepository.addPost(any())).thenReturn(flow { emit(PostResult.AddPostSuccess) })
        // ACT
        viewModel.addPost()
        advanceUntilIdle()
        val collectedState = viewModel.addPostScreenState.first()
        // ASSERT
        Assertions.assertEquals(expectedState, collectedState)
    }

    /**
     * Tests [AddPostScreenViewModel.addPost] when the repository returns an error result.
     */
    @Test
    fun addPost_ReturnsError() = runTest {
        // ARRANGE
        val fakeUser = User(
            id = "1",
            firstname = "Jim",
            lastname = "Nastyk",
            email = "jim.nastyk@test.com",
            password = ""
        )
        val fakePost = Post(
            id = "1",
            title = "post 1",
            description = "description of mocked post 1",
            photoUrl = "www.google.fr",
            timestamp = 123456789,
            author = fakeUser
        )
        val fakeError = Exception("error while adding post")
        val expectedState = AddPostScreenState.AddPostError
        // Mocking dependencies
        `when`(mockInternetUtils.isInternetAvailable()).thenReturn(true)
        `when`(mockPostRepository.addPhoto(any())).thenReturn(flow { emit(PostResult.AddPhotoSuccess(fakePost.photoUrl)) })
        `when`(mockUserRepository.readUser()).thenReturn(flow { emit(UserResult.ReadUserSuccess(fakeUser)) })
        `when`(mockPostRepository.addPost(any())).thenReturn(flow { emit(PostResult.AddPostError(fakeError)) })
        // ACT
        viewModel.addPost()
        advanceUntilIdle()
        val collectedState = viewModel.addPostScreenState.first()
        // ASSERT
        Assertions.assertEquals(expectedState, collectedState)
    }

}