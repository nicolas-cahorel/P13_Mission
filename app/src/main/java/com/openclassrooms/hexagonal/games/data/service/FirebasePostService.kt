package com.openclassrooms.hexagonal.games.data.service

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.openclassrooms.hexagonal.games.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

/**
 * Implementation of PostApi using Firebase Firestore and Firebase Storage.
 */
class FirebasePostService : PostApi {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun getPostsOrderByCreationDateDesc(): Flow<List<Post>> = flow {
        try {
            val snapshot = firestore.collection("posts")
                .orderBy("timestamp")
                .get()
                .await()

            val posts = snapshot.documents.mapNotNull { it.toObject(Post::class.java) }
            emit(posts)
        } catch (e: Exception) {
            Log.e("FirebasePostService", "Error fetching posts", e)
            emit(emptyList())
        }
    }

    override fun addPost(post: Post) {
        val postRef = firestore.collection("posts").document()
        val postId = postRef.id

        val updatedPost = post.copy(id = postId)

        postRef.set(updatedPost)
            .addOnSuccessListener {
                Log.d("FirebasePostService", "Post successfully added.")
            }
            .addOnFailureListener { e ->
                Log.e("FirebasePostService", "Failed to add post", e)
            }
    }

    /**
     * Adds a post with an image to Firebase Storage and Firestore.
     */
    fun addPostWithImage(post: Post, imageUri: Uri, callback: (Boolean) -> Unit) {
        val postRef = firestore.collection("posts").document()
        val postId = postRef.id
        val imageRef = storage.reference.child("posts/$postId/image.jpg")

        // Upload de l'image
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val updatedPost = post.copy(id = postId, photoUrl = uri.toString())
                    savePostToFirestore(postRef, updatedPost, callback)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebasePostService", "Failed to upload image", e)
                callback(false)
            }
    }

    private fun savePostToFirestore(postRef: DocumentReference, post: Post, callback: (Boolean) -> Unit) {
        postRef.set(post)
            .addOnSuccessListener {
                Log.d("FirebasePostService", "Post successfully saved.")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirebasePostService", "Failed to save post", e)
                callback(false)
            }
    }
}