package com.openclassrooms.hexagonal.games.data.service

import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User

/**
 * This class implements the PostApi interface and provides a fake in-memory data source for Posts.
 * It's intended for testing purposes and simulates a real API by holding sample Post and User data
 * within ArrayLists.
 */
class PostFakeApi : PostApi {
  private val users = mutableListOf(
    User("1", "Gerry", "Ariella"),
    User("2", "Brenton", "Capri"),
    User("3", "Wally", "Claud")
  )
  
  private val posts = mutableListOf(
    Post(
      "1",
      "Laughing History",
      "He learned the important lesson that a picnic at the beach on a windy day is a bad idea.",
      "",
      1361696994,
      users[0]
    ),
    
    Post(
      "2",
      "The Invisible Window",
      null,
      "https://picsum.photos/id/40/1080/",
      1210645031,
      users[1]
    ),
    
    Post(
      "3",
      "Woman of Years",
      "After fighting off the alligator, Brian still had to face the anaconda.",
      null,
      1346601558,
      users[0]
    ),
    
    Post(
      "4",
      "The Door's Game",
      null,
      "https://picsum.photos/id/85/1080/",
      1451638679,
      users[2]
    ),
    
    Post(
      "5",
      "The Secret of the Flowers",
      "Improve your goldfish's physical fitness by getting him a bicycle.",
      null,
      1629858873,
      users[0]
    )
  )
  
  override fun getPostsOrderByCreationDateDesc(): List<Post> =
    posts.sortedBy { it.timestamp }
  
  override fun addPost(post: Post) {
    posts.add(post)
  }
  
}
