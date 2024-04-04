package com.openclassrooms.hexagonal.games.data.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.openclassrooms.hexagonal.games.domain.model.Post;
import com.openclassrooms.hexagonal.games.domain.model.User;

/**
 * This class implements the PostApi interface and provides a fake in-memory data source for Posts.
 * It's intended for testing purposes and simulates a real API by holding sample Post and User data
 * within ArrayLists.
 */
public final class PostFakeApi
    implements PostApi
{

  private List<User> users = new ArrayList<User>()
  {
    {
      add(new User("1", "Gerry", "Ariella"));
      add(new User("2", "Brenton", "Capri"));
      add(new User("3", "Wally", "Claud"));
    }
  };

  private List<Post> posts = new ArrayList<Post>()
  {
    {
      add(new Post("1", "Laughing History", "He learned the important lesson that a picnic at the beach on a windy day is a bad idea.", "", 1361696994, users.get(0)));
      add(new Post("2", "The Invisible Window", null, "https://picsum.photos/id/40/1080/", 1210645031, users.get(1)));
      add(new Post("3", "Woman of Years", "After fighting off the alligator, Brian still had to face the anaconda.", null, 1346601558, users.get(0)));
      add(new Post("4", "The Door's Game", null, "https://picsum.photos/id/85/1080/", 1451638679, users.get(2)));
      add(new Post("5", "The Secret of the Flowers", "Improve your goldfish's physical fitness by getting him a bicycle.", null, 1629858873, users.get(0)));
    }
  };

  @Override
  public List<Post> getPostsOrderByCreationDateDesc()
  {
    posts.sort(Comparator.comparing(post -> post.timestamp));
    return posts;
  }

  @Override
  public void addPost(Post post)
  {
    posts.add(post);
  }

}
