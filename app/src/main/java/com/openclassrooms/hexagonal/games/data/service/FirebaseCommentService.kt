package com.openclassrooms.hexagonal.games.data.service

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.openclassrooms.hexagonal.games.data.repository.CommentResult
import com.openclassrooms.hexagonal.games.domain.model.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * FirebaseCommentService is an implementation of [CommentApi] that provides methods
 * for interacting with Firebase Firestore to manage comments.
 * It allows for retrieving and adding comments related to a specific post.
 */
class FirebaseCommentService : CommentApi {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Retrieves comments for a specific post, ordered by their creation date in ascending order.
     *
     * This function fetches all the comments linked to a given post ID from Firestore and orders them
     * by the timestamp field in ascending order. If no comments are found, it emits a `GetCommentsEmpty` result.
     * Otherwise, it emits a `GetCommentsSuccess` result containing the list of comments.
     *
     * @param postId The ID of the post for which comments are to be retrieved.
     * @return A [Flow] emitting a [CommentResult] that contains either the list of comments or an error.
     */
    override fun getCommentsOrderByCreationDate(postId: String): Flow<CommentResult> = flow {
        Log.d("Nicolas", "postId dans FirebaseCommentService : $postId")
        try {
            firestore
                .collection("posts")
                .document(postId)
                .collection("comments")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .snapshots()
                .collect { querySnapshot ->
                    val comments = querySnapshot.toObjects(Comment::class.java)

                    if (comments.isEmpty()) {
                        Log.d("Nicolas", "FirebaseCommentService - getCommentsOrderByCreationDate() : No comments found in Firestore DB.")
                        emit(CommentResult.GetCommentsEmpty)
                    } else {
                        Log.d("Nicolas", "FirebaseCommentService - getCommentsOrderByCreationDate() : Successfully retrieved ${comments.size} comment(s) from Firestore DB.")
                        emit(CommentResult.GetCommentsSuccess(comments))
                    }
                }
        } catch (exception: Exception) {
            Log.d("Nicolas", "FirebaseCommentService - getCommentsOrderByCreationDate() : Error occurred while fetching comments from Firestore DB.", exception)
            emit(CommentResult.GetCommentsError(exception))
        }
    }

    /**
     * Adds a new comment to the database in Firebase Firestore.
     *
     * This function adds the given comment to Firestore's "comments" collection. The comment is converted
     * into a [HashMap] before being added to Firestore. If the addition is successful, it emits a
     * `AddCommentSuccess` result. If an error occurs, it emits a `AddCommentError` result with the exception.
     *
     * @param comment The comment to be added, containing details such as content, timestamp, and author.
     * @return A [Flow] emitting a [CommentResult] indicating the success or failure of the add operation.
     */
    override fun addComment(comment: Comment): Flow<CommentResult> = flow {
        try {
            firestore
                .collection("posts")
                .document(comment.postId)
                .collection("comments")
                .document(comment.id)
                .set(comment.toHashmap())

            Log.d("Nicolas", "FirebaseCommentService - addComment() : Comment successfully uploaded to Firestore DB.")
            emit(CommentResult.AddCommentSuccess)
        } catch (exception: Exception) {
            Log.e("Nicolas", "\"FirebaseCommentService - addComment() : Error occurred while uploading comment in Firestore DB.", exception)
            emit(CommentResult.AddCommentError(exception))
        }
    }

//    override fun addComment(comment: Comment): Flow<CommentResult> {
//        return callbackFlow {
//            firestore
//                .collection("posts")
//                .document(comment.postId)
//                .collection("comments")
//                .document(comment.id)
//                .add(comment.toHashmap())
//                .addOnSuccessListener {
//                    trySend(CommentResult.AddCommentSuccess)
//                }
//                .addOnFailureListener { exception ->
//                    exception.printStackTrace()
//                    trySend(CommentResult.AddCommentError(exception))
//                }
//        }
//    }

    /**
     * Converts a [Comment] object into a [HashMap] representation for Firestore storage.
     *
     * This function transforms the [Comment] instance into a key-value structure that can be directly stored
     * in Firestore. The `author` field is also converted into a nested [HashMap] to maintain the structured
     * relationship between the comment and its author.
     *
     * @receiver The [Comment] instance to be converted.
     * @return A [HashMap] containing all the comment's fields, including a nested author object.
     */
    private fun Comment.toHashmap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "postId" to postId,
            "content" to content,
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