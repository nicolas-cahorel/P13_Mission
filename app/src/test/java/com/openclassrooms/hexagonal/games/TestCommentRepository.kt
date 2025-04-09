package com.openclassrooms.hexagonal.games

import com.openclassrooms.hexagonal.games.data.repository.CommentRepository
import com.openclassrooms.hexagonal.games.data.repository.CommentResult
import com.openclassrooms.hexagonal.games.data.service.CommentApi
import com.openclassrooms.hexagonal.games.domain.model.Comment
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
class TestCommentRepository {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockCommentApi: CommentApi
    private lateinit var commentRepository: CommentRepository

    /**
     * Sets up the test environment before each test.
     * Initializes the mock objects and sets the main coroutine dispatcher.
     */
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        commentRepository = CommentRepository(mockCommentApi)
    }

    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Tests [CommentRepository.getComments] when the API returns a successful result.
     */
    @Test
    fun getComments_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakePostId = "post123"
        val fakeResponse = listOf(
            Comment(
                id = "1",
                postId = fakePostId,
                content = "mocked comment 1 on post 123",
                timestamp = 123456789,
                author = User(
                    id = "1",
                    firstname = "Jim",
                    lastname = "Nastyk",
                    email = "jim.nastyk@test.com",
                    password = ""
                )
            ),
            Comment(
                id = "2",
                postId = fakePostId,
                content = "mocked comment 2 on post 123",
                timestamp = 123456790,
                author = User(
                    id = "1",
                    firstname = "Jim",
                    lastname = "Nastyk",
                    email = "jim.nastyk@test.com",
                    password = ""
                )
            )
        )
        val expectedResult = CommentResult.GetCommentsSuccess(fakeResponse)
        // Mocking dependencies
        `when`(mockCommentApi.getCommentsOrderByCreationDate(fakePostId)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = commentRepository.getComments(fakePostId).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [CommentRepository.getComments] when the API returns an error result.
     */
    @Test
    fun getComments_ReturnsError() = runTest {
        // ARRANGE
        val fakePostId = "123"
        val expectedResult = CommentResult.GetCommentsError(
            Exception("error while fetching comments on post 123")
        )
        // Mocking dependencies
        `when`(mockCommentApi.getCommentsOrderByCreationDate(fakePostId)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = commentRepository.getComments(fakePostId).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [CommentRepository.addComment] when the API returns a successful result.
     */
    @Test
    fun addComment_ReturnsSuccess() = runTest {
        // ARRANGE
        val fakeComment = Comment(
            id = "1",
            postId = "123",
            content = "mocked comment 1 on post 123",
            timestamp = 123456789,
            author = User(
                id = "1",
                firstname = "Jim",
                lastname = "Nastyk",
                email = "jim.nastyk@test.com",
                password = ""
            )
        )
        val expectedResult = CommentResult.AddCommentSuccess
        // Mocking dependencies
        `when`(mockCommentApi.addComment(fakeComment)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = commentRepository.addComment(fakeComment).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

    /**
     * Tests [CommentRepository.addComment] when the API returns an error result.
     */
    @Test
    fun addComment_ReturnsError() = runTest {
        // ARRANGE
        val fakeComment = Comment(
            id = "1",
            postId = "123",
            content = "mocked comment 1 on post 123",
            timestamp = 123456789,
            author = User(
                id = "1",
                firstname = "Jim",
                lastname = "Nastyk",
                email = "jim.nastyk@test.com",
                password = ""
            )
        )
        val expectedResult = CommentResult.AddCommentError(
            Exception("error while adding comment")
        )
        /// Mocking dependencies
        `when`(mockCommentApi.addComment(fakeComment)).thenReturn(flowOf(expectedResult))
        // ACT
        val collectedResult = commentRepository.addComment(fakeComment).first()
        // ASSERT
        Assertions.assertEquals(expectedResult, collectedResult)
    }

}
