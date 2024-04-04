package com.openclassrooms.hexagonal.games.domain.model;

import java.io.Serializable;

/**
 * This class represents a Post data object. It holds information about a post, including its
 * ID, title, description, photo URL, creation timestamp, and the author (User object).
 * The class implements Serializable to allow for potential serialization needs.
 */
public final class Post
    implements Serializable
{

  /**
   * Unique identifier for the Post.
   */
  public final String id;

  /**
   * Title of the Post.
   */
  public final String title;

  /**
   * Optional description for the Post.
   */
  public final String description;

  /**
   * URL of an image associated with the Post, if any.
   */
  public final String photoUrl;

  /**
   * Timestamp representing the creation date and time of the Post in milliseconds since epoch.
   */
  public final long timestamp;

  /**
   * User object representing the author of the Post.
   */
  public final User author;

  /**
   * Constructor for the Post class. Initializes all fields of the Post object.
   *
   * @param id          Unique identifier for the Post.
   * @param title       Title of the Post.
   * @param description Optional description for the Post.
   * @param photoUrl    URL of an image associated with the Post, if any.
   * @param timestamp   Timestamp representing the creation date and time of the Post in milliseconds since epoch.
   * @param author      User object representing the author of the Post.
   */
  public Post(String id, String title, String description, String photoUrl, long timestamp,
      User author)
  {
    this.id = id;
    this.title = title;
    this.description = description;
    this.photoUrl = photoUrl;
    this.timestamp = timestamp;
    this.author = author;
  }

}
