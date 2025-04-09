package com.openclassrooms.hexagonal.games

import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.data.repository.PostResult
import com.openclassrooms.hexagonal.games.data.service.CommentApi
import com.openclassrooms.hexagonal.games.data.service.PostApi
import com.openclassrooms.hexagonal.games.domain.model.Post
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
 * Unit tests for [PostRepository].
 * These tests verify the behavior of the repository when interacting with the [CommentApi].
 */
@ExperimentalCoroutinesApi
class TestPostRepository {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockPostApi: PostApi
    private lateinit var postRepository: PostRepository

    /**
     * Sets up the test environment before each test.
     * Initializes the mock objects and sets the main coroutine dispatcher.
     */
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        postRepository = PostRepository(mockPostApi)
    }

    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Tests [PostRepository.getPosts] when the API returns a successful result.
     */
    @Test
    fun getPosts_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakeResponse = listOf(
            Post(
                id = "1",
                title = "post 1",
                description = "description of post 1",
                photoUrl = "",
                timestamp = 123456789,
                author = User(
                    id = "1",
                    firstname = "Jim",
                    lastname = "Nastyk",
                    email = "jim.nastyk@test.com",
                    password = ""
                )
            ),
            Post(
                id = "2",
                title = "post 2",
                description = "description of post 2",
                photoUrl = "",
                timestamp = 123456789,
                author = User(
                    id = "1",
                    firstname = "Jim",
                    lastname = "Nastyk",
                    email = "jim.nastyk@test.com",
                    password = ""
                )
            )
        )
        val expectedResult = PostResult.GetPostsSuccess(fakeResponse)
        // Mocking dependencies
        `when`(mockPostApi.getPostsOrderByCreationDate()).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = postRepository.getPosts().first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [PostRepository.getPosts] when the API returns an error result.
     */
    @Test
    fun getPosts_ReturnsError() = runTest {
        // ARRANGE
        val expectedResult = PostResult.GetPostsError(
            Exception("error while fetching posts")
        )
        // Mocking dependencies
        `when`(mockPostApi.getPostsOrderByCreationDate()).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = postRepository.getPosts().first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [PostRepository.addPost] when the API returns a successful result.
     */
    @Test
    fun addPost_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakePost = Post(
            id = "1",
            title = "post 1",
            description = "description of post 1",
            photoUrl = "",
            timestamp = 123456789,
            author = User(
                id = "1",
                firstname = "Jim",
                lastname = "Nastyk",
                email = "jim.nastyk@test.com",
                password = ""
            )
        )
        val expectedResult = PostResult.AddPostSuccess
        // Mocking dependencies
        `when`(mockPostApi.addPost(fakePost)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = postRepository.addPost(fakePost).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [PostRepository.addPost] when the API returns an error result.
     */
    @Test
    fun addPost_ReturnsError() = runTest {
        // ARRANGE
        val fakePost = Post(
            id = "1",
            title = "post 1",
            description = "description of post 1",
            photoUrl = "",
            timestamp = 123456789,
            author = User(
                id = "1",
                firstname = "Jim",
                lastname = "Nastyk",
                email = "jim.nastyk@test.com",
                password = ""
            )
        )
        val expectedResult = PostResult.AddPostError(
            Exception("error while adding post")
        )
        // Mocking dependencies
        `when`(mockPostApi.addPost(fakePost)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = postRepository.addPost(fakePost).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

}