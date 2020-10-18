package org.example.user.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.example.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class ExternalApiUserClientTest {

  private WireMockServer wireMockServer;

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final HttpClient httpClient = HttpClient.newHttpClient();

  private final ExternalApiUserClient userClient =
      new ExternalApiUserClient("http://localhost:8080", httpClient, objectMapper);

  @BeforeEach
  void init() {
    wireMockServer = new WireMockServer();
    wireMockServer.start();
  }

  @AfterEach
  void clean() {
    wireMockServer.stop();
  }

  @Test
  void testParseEmptyListGetAllResponse() {
    // given
    wireMockServer.stubFor(get(urlEqualTo("/users"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("[]")));

    //when
    CompletableFuture<List<User>> response = userClient.getAllUsers();

    // then
    assertTrue(response.join().isEmpty());
  }

  @Test
  void testParseSingleUserGetAllResponse() {
    // given
    wireMockServer.stubFor(get(urlEqualTo("/users"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody(
                "["
                + "{"
                + "\"id\": 1,"
                + "\"first_name\": \"Maurise\","
                + "\"last_name\": \"Shieldon\","
                + "\"email\": \"mshieldon0@squidoo.com\","
                + "\"ip_address\": \"192.57.232.111\","
                + "\"latitude\": 34.003135,"
                + "\"longitude\": -117.7228641"
                + "}"
                + "]"
            )));

    //when
    List<User> response = userClient.getAllUsers().join();

    // then
    assertEquals(1, response.size());
    User user = response.get(0);
    assertEquals(1, user.getId());
    assertEquals("Maurise", user.getFirstName());
    assertEquals("Shieldon", user.getLastName());
    assertEquals("mshieldon0@squidoo.com", user.getEmail());
    assertEquals("192.57.232.111", user.getIpAddress());
    assertEquals(new BigDecimal("34.003135"), user.getLatitude());
    assertEquals(new BigDecimal("-117.7228641"), user.getLongitude());
  }

  @Test
  void testParseMultipleUserGetAllResponse() {
    // given
    wireMockServer.stubFor(get(urlEqualTo("/users"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody(
                "["
                    + "{"
                    + "\"id\": 1,"
                    + "\"first_name\": \"Maurise\","
                    + "\"last_name\": \"Shieldon\","
                    + "\"email\": \"mshieldon0@squidoo.com\","
                    + "\"ip_address\": \"192.57.232.111\","
                    + "\"latitude\": 34.003135,"
                    + "\"longitude\": -117.7228641"
                    + "},{"
                    + "\"id\": 2,"
                    + "\"first_name\": \"Bendix\","
                    + "\"last_name\": \"Halgarth\","
                    + "\"email\": \"bhalgarth1@timesonline.co.uk\","
                    + "\"ip_address\": \"4.185.73.82\","
                    + "\"latitude\": -2.9623869,"
                    + "\"longitude\": 104.7399789"
                    + "}"
                    + "]"
            )));

    //when
    List<User> response = userClient.getAllUsers().join();

    // then
    assertEquals(2, response.size());
    assertEquals(1, response.get(0).getId());
    assertEquals(2, response.get(1).getId());
  }

  @Test
  void testParseEmptyListGetLondonResponse() {
    // given
    wireMockServer.stubFor(get(urlEqualTo("/city/london/users"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("[]")));

    //when
    CompletableFuture<List<User>> response = userClient.getUserByCity("london");

    // then
    assertTrue(response.join().isEmpty());
  }

  @Test
  void testThrowsRuntimeExceptionIfFailsToParseJson() {
    // given
    wireMockServer.stubFor(get(urlEqualTo("/city/london/users"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("invalid json")));

    //when
    CompletableFuture<List<User>> response = userClient.getUserByCity("london");

    // then
    assertThrows(RuntimeException.class, response::join);
  }

}
