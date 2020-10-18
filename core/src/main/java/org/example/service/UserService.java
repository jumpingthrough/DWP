package org.example.service;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import org.example.model.User;
import org.example.ports.clients.UserClient;

public class UserService {

  private final UserClient client;

  public UserService(UserClient client) {
    this.client = client;
  }

  /**
   * Retrieve a List of Users that are either living in London, or within 50 miles of London.
   *
   * @return the List of Users.
   */
  public List<User> getLondonUsers() {
    // Get Users from /users and filter those within 50 miles of London

    // Get Users from /city/{city}/users

    // Join Lists and return

    return Collections.emptyList();
  }

  Predicate<User> filter50MilesFromLondon() {
    return (user) -> {
      return true;
    };
  }

}
