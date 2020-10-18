package org.example.user.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.example.model.User;
import org.example.ports.clients.UserClient;

public class ExternalApiUserClient implements UserClient {
  @Override
  public CompletableFuture<List<User>> getAllUsers() {
    return null;
  }

  @Override
  public CompletableFuture<List<User>> getUserByCity(String city) {
    return null;
  }
}
