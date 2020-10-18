package org.example.ports.clients;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.example.model.User;

public interface UserClient {

  CompletableFuture<List<User>> getAllUsers();

  CompletableFuture<List<User>> getUserByCity(String city);

}
