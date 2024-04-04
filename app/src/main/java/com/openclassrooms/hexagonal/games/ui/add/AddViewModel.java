package com.openclassrooms.hexagonal.games.ui.add;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

import com.openclassrooms.hexagonal.games.data.repository.PostRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * This ViewModel manages data and interactions related to adding new posts in the AddFragment.
 * It utilizes dependency injection to retrieve a PostRepository instance for interacting with post data.
 *
 * @see AddFragment
 * @see PostRepository
 */
@HiltViewModel
public final class AddViewModel
    extends ViewModel
{

  /**
   * Repository instance used for accessing and manipulating Post data.
   */
  private final PostRepository postRepository;

  /**
   * Constructor for AddViewModel. Injected with a PostRepository instance.
   *
   * @param postRepository Repository for interacting with Post data.
   */
  @Inject
  public AddViewModel(PostRepository postRepository)
  {
    this.postRepository = postRepository;
  }

  /**
   * Initiates the process of adding a new Post. This method currently has a TODO marker,
   * indicating that the specific implementation for adding a post needs to be completed.
   * The implementation likely involves collecting user input, creating a Post object, and
   * using the PostRepository to persist the new Post.
   */
  public void addPost(/*...*/)
  {
    //TODO
    //postRepository.addPost();
  }

}
