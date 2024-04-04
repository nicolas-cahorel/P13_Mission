package com.openclassrooms.hexagonal.games.data.repository;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.hexagonal.games.data.service.PostApi;
import com.openclassrooms.hexagonal.games.domain.model.Post;

/**
 * This class provides a repository for accessing and managing Post data.
 * It utilizes dependency injection to retrieve a PostApi instance for interacting
 * with the data source. The class is marked as a Singleton using @Singleton annotation,
 * ensuring there's only one instance throughout the application.
 */
@Singleton
public final class PostRepository
{

  private final PostApi postApi;

  /**
   * Constructor for PostRepository. Injects the PostApi dependency.
   *
   * @param postApi An instance of the PostApi to be used.
   */
  @Inject
  public PostRepository(PostApi postApi)
  {
    this.postApi = postApi;
  }

  /**
   * Retrieves a LiveData object containing a list of Posts ordered by creation date
   * in descending order.
   *
   * @return LiveData containing a list of Posts.
   */
  public LiveData<List<Post>> getPosts()
  {
    return new MutableLiveData<>(postApi.getPostsOrderByCreationDateDesc());
  }

  /**
   * Adds a new Post to the data source using the injected PostApi.
   *
   * @param post The Post object to be added.
   */
  public void addPost(Post post)
  {
    postApi.addPost(post);
  }

}
