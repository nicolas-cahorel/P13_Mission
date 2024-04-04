package com.openclassrooms.hexagonal.games.domain.model;

import java.io.Serializable;

/**
 * This class represents a User data object. It holds basic information about a user, including
 * their ID, first name, and last name. The class implements Serializable to allow for potential
 * serialization needs.
 */
public final class User
    implements Serializable
{

  /**
   * Unique identifier for the User.
   */
  public final String id;

  /**
   * User's first name.
   */
  public final String firstname;

  /**
   * User's last name.
   */
  public final String lastname;

  /**
   * Constructor for the User class. Initializes all fields of the User object.
   *
   * @param id        Unique identifier for the User.
   * @param firstname User's first name.
   * @param lastname  User's last name.
   */
  public User(String id, String firstname, String lastname)
  {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
  }

}
