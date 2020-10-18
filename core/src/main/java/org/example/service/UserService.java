package org.example.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    CompletableFuture<List<User>> allUsersFuture =
        client
            .getAllUsers()
            .thenApply(users ->
              users.stream()
                  .filter(filter50MilesFromLondon())
                  .collect(Collectors.toList())
            );

    // Join on the Futures, flatten the Lists and return
    return Stream.of(allUsersFuture, client.getUserByCity("London"))
        .map(CompletableFuture::join)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  // TODO
  Predicate<User> filter50MilesFromLondon() {
    return (user) -> {
      return true;
    };
  }

}
