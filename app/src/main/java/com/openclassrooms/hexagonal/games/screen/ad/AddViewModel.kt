package com.openclassrooms.hexagonal.games.screen.ad

import androidx.lifecycle.ViewModel
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * This ViewModel manages data and interactions related to adding new posts in the AddScreen.
 * It utilizes dependency injection to retrieve a PostRepository instance for interacting with post data.
 */
@HiltViewModel
class AddViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {
  /**
   * Initiates the process of adding a new Post. This method currently has a TODO marker,
   * indicating that the specific implementation for adding a post needs to be completed.
   * The implementation likely involves collecting user input, creating a Post object, and
   * using the PostRepository to persist the new Post.
   */
  fun addPost( /*...*/) {
    //TODO
    //postRepository.addPost();
  }
}
