package org.example.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.example.model.User;
import org.example.ports.clients.UserClient;

public class UserService {

  // Used a lat/long that references Westminster as the centre of London
  private static final BigDecimal LONDON_LAT = new BigDecimal("51.494720");
  private static final BigDecimal LONDON_LONG = new BigDecimal("-0.135278");

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
                  .filter(filter50MilesFrom(LONDON_LAT, LONDON_LONG))
                  .collect(Collectors.toList())
            );

    // Join on the Futures, flatten the Lists and return
    return Stream.of(allUsersFuture, client.getUserByCity("London"))
        .map(CompletableFuture::join)
        .flatMap(List::stream)
        .distinct()
        .collect(Collectors.toList());
  }

  /*
   * Returns true if a User's lat/long are within 1 degree (roughly 60 miles) of a given lat/long.
   * From the centre of London, that works out to around 40-50 miles from Central/Greater London
   * as a whole.
   */
  Predicate<User> filter50MilesFrom(BigDecimal latitude, BigDecimal longitude) {
    return (user) ->
        (user.getLatitude().compareTo(latitude.subtract(new BigDecimal("1"))) >= 0
            && user.getLatitude().compareTo(latitude.add(new BigDecimal("1"))) <= 0)
            && (user.getLongitude().compareTo(longitude.subtract(new BigDecimal("1"))) >= 0
            && user.getLongitude().compareTo(longitude.add(new BigDecimal("1"))) <= 0);
  }

}
