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

    //        .apply {
//        firestoreSettings = FirebaseFirestoreSettings.Builder()
//            .setPersistenceEnabled(false)  // Disable offline cache
//            .build()
//    }
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    init {
        firestore.clearPersistence() // Ensures no offline caching for fresh data
    }

    /**
     * Retrieves all posts ordered by their creation date in descending order.
     *
     * @return A Flow emitting [PostResult] containing the list of posts or an error.
     */
    override fun getPostsOrderByCreationDate(): Flow<PostResult> = flow {
        try {
            firestore.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .snapshots()
                .collect { querySnapshot ->
                    val posts = querySnapshot.toObjects(Post::class.java)
                    if (posts.isEmpty()) {
                        emit(PostResult.GetPostsEmpty)
                    } else {
                        emit(PostResult.GetPostsSuccess(posts))
                    }
                }
        } catch (exception: Exception) {
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
            emit(PostResult.AddPhotoSuccess(downloadUri))
        } catch (exception: Exception) {
            Log.e("Nicolas", "Error adding photo", exception)
            emit(PostResult.AddPhotoError(exception))
        }
    }

    /**
     * Adds a post to Firebase Firestore.
     *
     * @param post The post to be added.
     * @return A Flow emitting [PostResult] indicating success or failure.
     */
    override fun addPost(post: Post): Flow<PostResult> = flow {
        val postToSave = hashMapOf(
            "id" to post.id,
//            "title" to post.title,
//            "description" to post.description,
//            "photoUrl" to post.photoUrl,
//            "timestamp" to post.timestamp,
//            "authorId" to post.author?.id,
//            "authorEmail" to post.author?.email,
//            "authorFirstname" to post.author?.firstname,
//            "authorLastname" to post.author?.lastname
        )


        try {
            Log.d("Nicolas", "Saving post to Firestore: $post")

            testFirestoreConnection()

            firestore.collection("posts").add(postToSave)
                .addOnSuccessListener {
                    Log.d("Nicolas", "addonsuccesslistener.")
                }
                .addOnFailureListener {
                    Log.d("Nicolas", "addonfailurelistener.")
                }
                .addOnCanceledListener {
                    Log.d("Nicolas", "addoncancellistener.")
                }
                .addOnCompleteListener {
                    Log.d("Nicolas", "addoncompletelistener.")
                }

//            firestore.collection("posts").document(post.id).set(postToSave)
//                .addOnSuccessListener {
//                    Log.d("Nicolas", "addonsuccesslistener.")
//                }
//                .addOnFailureListener {
//                    Log.d("Nicolas", "addonfailurelistener.")
//                }
//                .addOnCanceledListener {
//                    Log.d("Nicolas", "addoncancellistener.")
//                }
//                .addOnCompleteListener {
//                    Log.d("Nicolas", "addoncompletelistener.")
//                }
            Log.d("Nicolas", "Post successfully saved.")
            emit(PostResult.AddPostSuccess)
        } catch (exception: Exception) {
            Log.e("Nicolas", "Error adding post", exception)
            emit(PostResult.AddPostError(exception))
        }
    }

    /**
     * Retrieves a specific post.
     *
     * @return A Flow emitting [PostResult] with the requested post.
     */
    override fun getPost(): Flow<PostResult> {
        TODO("Not yet implemented")
    }

    /**
     * Tests the Firestore connection by writing a test document.
     */
    private fun testFirestoreConnection() {
        firestore.collection("test").document("connection_test").set(hashMapOf("status" to "ok"))
            .addOnSuccessListener {
                Log.d("Nicolas", "Firestore connection test: SUCCESS")
            }
            .addOnFailureListener { exception ->
                Log.e("Nicolas", "Firestore connection test: FAILED", exception)
            }
    }
}