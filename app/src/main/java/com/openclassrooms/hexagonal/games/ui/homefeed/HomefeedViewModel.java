package com.openclassrooms.hexagonal.games.ui.homefeed;

import java.util.List;
import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.hexagonal.games.data.repository.PostRepository;
import com.openclassrooms.hexagonal.games.domain.model.Post;
import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel responsible for managing data and events related to the Homefeed.
 * <p>
 * This ViewModel retrieves posts from the PostRepository and exposes them as a LiveData<List<Post>>,
 * allowing UI components to observe and react to changes in the posts data.
 */
@HiltViewModel
public final class HomefeedViewModel
    extends ViewModel
{

  /**
   * Repository responsible for fetching and managing post data.
   */
  private final PostRepository postRepository;

  /**
   * Constructor for HomefeedViewModel.
   *
   * @param postRepository The PostRepository instance to use for data access.
   */
  @Inject
  public HomefeedViewModel(PostRepository postRepository)
  {
    this.postRepository = postRepository;
  }

  /**
   * Returns a LiveData observable containing the list of posts fetched from the repository.
   *
   * @return A LiveData<List<Post>> object that can be observed for changes in the posts data.
   */
  public LiveData<List<Post>> getPosts()
  {
    return postRepository.getPosts();
  }

}
