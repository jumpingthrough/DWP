package org.example.user.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.example.model.User;
import org.example.ports.clients.UserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalApiUserClient implements UserClient {

  private static final Logger logger = LoggerFactory.getLogger(ExternalApiUserClient.class);

  private final String baseUrl;
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  public ExternalApiUserClient(
      String baseUrl,
      HttpClient httpClient,
      ObjectMapper objectMapper) {
    this.baseUrl = baseUrl;
    this.httpClient = httpClient;
    this.objectMapper = objectMapper;
  }

  @Override
  public CompletableFuture<List<User>> getAllUsers() {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/users"))
        .build();

    return sendUserListRequestAndParse(request);
  }

  @Override
  public CompletableFuture<List<User>> getUserByCity(String city) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/city/" + city + "/users"))
        .build();

    return sendUserListRequestAndParse(request);
  }

  private CompletableFuture<List<User>> sendUserListRequestAndParse(HttpRequest request) {
    return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenApply(response -> {
          List<User> usersList;

          try {
            usersList = objectMapper.readValue(response, new TypeReference<>() {});
            logger.debug("Parsed user list {}", usersList);
          } catch (Exception e) {
            String errorMessage = "Exception thrown when parsing JSON String " + response;
            logger.error(errorMessage, e);
            // Could throw an Application specific exception here instead
            throw new RuntimeException(errorMessage);
          }

          return usersList;
        });
  }
}
