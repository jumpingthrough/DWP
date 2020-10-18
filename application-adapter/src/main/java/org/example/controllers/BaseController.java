package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.util.List;
import org.example.model.User;
import org.example.ports.clients.UserClient;
import org.example.service.UserService;
import org.example.user.client.ExternalApiUserClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

  private static final String EXTERNAL_API_BASE_URL = "https://bpdts-test-app.herokuapp.com";

  private final HttpClient httpClient = HttpClient.newHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final UserClient userClient = new ExternalApiUserClient(
      EXTERNAL_API_BASE_URL, httpClient, objectMapper);

  private final UserService userService = new UserService(userClient);

  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<User>> getLondonUsers() {
    return ResponseEntity.ok(userService.getLondonUsers());
  }

}
