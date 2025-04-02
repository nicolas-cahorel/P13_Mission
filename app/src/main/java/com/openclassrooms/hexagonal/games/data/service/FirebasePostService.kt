package com.openclassrooms.hexagonal.games.data.service

import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.FirebaseStorage
import com.openclassrooms.hexagonal.games.data.repository.PostResult
import com.openclassrooms.hexagonal.games.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

/**
 * FirebasePostService is an implementation of [PostApi] that provides methods
 * for interacting with Firebase Firestore and Firebase Storage to manage posts.
 */
class FirebasePostService : PostApi {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    /**
     * Retrieves all posts ordered by their creation date in descending order.
     *
     * @return A Flow emitting [PostResult] containing the list of posts or an error.
     */
    override fun getPostsOrderByCreationDate(): Flow<PostResult> = flow {
        try {
            firestore
                .collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .snapshots()
                .collect { querySnapshot ->
                    val posts = querySnapshot.toObjects(Post::class.java)

                    if (posts.isEmpty()) {
                        Log.d("Nicolas", "FirebasePostService - getPostsOrderByCreationDate() : No posts found in Firestore DB.")
                        emit(PostResult.GetPostsEmpty)
                    } else {
                        Log.d("Nicolas", "FirebasePostService - getPostsOrderByCreationDate() : Successfully retrieved ${posts.size} post(s) from Firestore DB.")
                        emit(PostResult.GetPostsSuccess(posts))
                    }
                }
        } catch (exception: Exception) {
            Log.e("Nicolas", "FirebasePostService - getPostsOrderByCreationDate() : Error occurred while fetching posts from Firestore DB.", exception)
            emit(PostResult.GetPostsError(exception))
        }
    }

    /**
     * Uploads a photo associated with a post to Firebase Storage.
     *
     * @param post The post containing the photo URL.
     * @return A Flow emitting [PostResult] indicating success or failure.
     */
    override fun addPhoto(post: Post): Flow<PostResult> = flow {
        try {
            val imageRef = storage.reference.child("post_images/${post.id}")
            imageRef.putFile(post.photoUrl.toUri()).await()

            val downloadUri = imageRef.downloadUrl.await().toString()

            Log.d("Nicolas", "FirebasePostService - addPhoto() : Post photo successfully uploaded to Firebase Storage.")
            emit(PostResult.AddPhotoSuccess(downloadUri))
        } catch (exception: Exception) {
            Log.e("Nicolas", "FirebasePostService - addPhoto() : Error occurred while uploading photo in Firebase Storage.", exception)
            emit(PostResult.AddPhotoError(exception))
        }
    }

    /**
     * Adds a new post to Firebase Firestore.
     *
     * This function uses Firestore's `add` method to store the post data. The post is first converted
     * into a HashMap using `toHashmap()`, then added to the "posts" collection. The result is emitted
     * as a `Flow` of `PostResult`, indicating either success or failure.
     *
     * The function operates asynchronously and returns a `callbackFlow` to handle Firestore's
     * asynchronous listeners.
     *
     * @param post The post to be added, containing details such as title, description, and author.
     * @return A [Flow] emitting [PostResult.AddPostSuccess] if the post is successfully added,
     *         or [PostResult.AddPostError] in case of failure.
     */
    override fun addPost(post: Post): Flow<PostResult> = flow {
        try {
            firestore
                .collection("posts")
                .document(post.id)
                .set(post.toHashmap())

            Log.d("Nicolas", "FirebasePostService - addPost() : Post successfully uploaded to Firestore DB.")
            emit(PostResult.AddPostSuccess)
        } catch (exception: Exception) {
            Log.e("Nicolas", "FirebasePostService - addPost() : Error occurred while uploading post in Firestore DB.", exception)
            emit(PostResult.AddPostError(exception))
        }
    }

    /**
     * Retrieves a specific post.
     *
     * @return A Flow emitting [PostResult] with the requested post.
     */
    override fun getPost(postId: String): Flow<PostResult> = flow {
        try {
            val snapshot = firestore
                .collection("posts")
                .document(postId)
                .get()
                .await()
            val post = snapshot.toObject(Post::class.java)

            if (post == null) {
                Log.d("Nicolas", "FirebasePostService - getPost() : No post found in Firestore DB.")
                emit(PostResult.GetPostNotFound)
            } else {
                Log.d("Nicolas", "FirebasePostService - getPost() : Successfully retrieved post from Firestore DB.")
                emit(PostResult.GetPostSuccess(post))
            }
        } catch (exception: Exception) {
            Log.e("Nicolas", "FirebasePostService - getPost() : Error occurred while fetching post from Firestore DB.", exception)
            emit(PostResult.GetPostError(exception))
        }
    }

    /**
     * Converts a [Post] object into a [HashMap] representation for Firestore storage.
     *
     * This function transforms the [Post] instance into a key-value structure that can be
     * directly stored in Firestore. The `author` field is also converted into a nested HashMap
     * to maintain the structured relationship between the post and its author.
     *
     * @receiver The [Post] instance to be converted.
     * @return A [HashMap] containing all the post's fields, including a nested author object.
     */
    private fun Post.toHashmap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "title" to title,
            "description" to description,
            "photoUrl" to photoUrl,
            "timestamp" to timestamp,
            "author" to hashMapOf(
                "id" to author!!.id,
                "email" to author.email,
                "firstname" to author.firstname,
                "lastname" to author.lastname
            )
        )
    }

}