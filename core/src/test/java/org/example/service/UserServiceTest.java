package org.example.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.example.model.User;
import org.example.ports.clients.UserClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class UserServiceTest {

  private final UserClient userClient = mock(UserClient.class);

  private final UserService userService = new UserService(userClient);

  @Test
  void testPredicateOnEmptyList() {
    List<User> userList = Collections.emptyList();
    List<User> filteredList = userList.stream()
        .filter(userService.filter50MilesFromLondon())
        .collect(Collectors.toList());

    assertTrue(filteredList.isEmpty());
  }

}
