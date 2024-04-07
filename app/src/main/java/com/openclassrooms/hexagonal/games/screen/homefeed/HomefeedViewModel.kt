package com.openclassrooms.hexagonal.games.screen.homefeed

import androidx.lifecycle.ViewModel
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel responsible for managing data and events related to the Homefeed.
 * This ViewModel retrieves posts from the PostRepository and exposes them as a Flow<List<Post>>,
 * allowing UI components to observe and react to changes in the posts data.
 */
@HiltViewModel
class HomefeedViewModel @Inject constructor(private val postRepository: PostRepository) :
  ViewModel() {
  
  /**
   * Returns a Flow observable containing the list of posts fetched from the repository.
   *
   * @return A Flow<Lis<Post>> object that can be observed for changes in the posts data.
   */
  val posts: Flow<List<Post>> = postRepository.posts
  
}
