package org.example.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.example.model.User;
import org.example.ports.clients.UserClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UserServiceTest {

  private static final BigDecimal TEST_LAT = new BigDecimal("48.11");
  private static final BigDecimal TEST_LONG = new BigDecimal("51.89");

  private final UserClient userClient = mock(UserClient.class);

  private final UserService userService = new UserService(userClient);

  @Test
  void testEqualToLatLongEvaluatesToTrue() {
    assertTrue(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT, TEST_LONG)));
  }

  @Test
  void testLatitudeLessThanOneDegreeHigherEvaluatesToTrue() {
    assertTrue(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT.add(new BigDecimal("0.5")), TEST_LONG)));
  }

  @Test
  void testLatitudeOneDegreeHigherEvaluatesToTrue() {
    assertTrue(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT.add(new BigDecimal("1")), TEST_LONG)));
  }

  @Test
  void testLatitudeOverOneDegreeHigherEvaluatesToFalse() {
    assertFalse(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT.add(new BigDecimal("1.00000001")), TEST_LONG)));
  }

  @Test
  void testLatitudeLessThanOneDegreeLowerEvaluatesToTrue() {
    assertTrue(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT.subtract(new BigDecimal("0.5")), TEST_LONG)));
  }

  @Test
  void testLatitudeOneDegreeLowerEvaluatesToTrue() {
    assertTrue(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT.subtract(new BigDecimal("1")), TEST_LONG)));
  }

  @Test
  void testLatitudeOverOneDegreeLowerEvaluatesToFalse() {
    assertFalse(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT.subtract(new BigDecimal("1.00000001")), TEST_LONG)));
  }

  @Test
  void testLongitudeLessThanOneDegreeHigherEvaluatesToTrue() {
    assertTrue(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT, TEST_LONG.add(new BigDecimal("0.5")))));
  }

  @Test
  void testLongitudeOneDegreeHigherEvaluatesToTrue() {
    assertTrue(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT, TEST_LONG.add(new BigDecimal("1")))));
  }

  @Test
  void testLongitudeOverOneDegreeHigherEvaluatesToFalse() {
    assertFalse(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT, TEST_LONG.add(new BigDecimal("1.00000001")))));
  }

  @Test
  void testLongitudeLessThanOneDegreeLowerEvaluatesToTrue() {
    assertTrue(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT, TEST_LONG.subtract(new BigDecimal("0.5")))));
  }

  @Test
  void testLongitudeOneDegreeLowerEvaluatesToTrue() {
    assertTrue(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT, TEST_LONG.subtract(new BigDecimal("1")))));
  }

  @Test
  void testLongitudeOverOneDegreeLowerEvaluatesToFalse() {
    assertFalse(
        userService.filter50MilesFrom(TEST_LAT, TEST_LONG)
            .test(generateUser(TEST_LAT, TEST_LONG.subtract(new BigDecimal("1.00000001")))));
  }

  @Test
  void testPredicateOnEmptyList() {
    List<User> userList = Collections.emptyList();
    List<User> filteredList = userList.stream()
        .filter(userService.filter50MilesFrom(TEST_LAT, TEST_LONG))
        .collect(Collectors.toList());

    assertTrue(filteredList.isEmpty());
  }

  @Test
  void testPredicateOnListWithOnePassing() {
    List<User> userList = List.of(generateUser(TEST_LAT, TEST_LONG));
    List<User> filteredList = userList.stream()
        .filter(userService.filter50MilesFrom(TEST_LAT, TEST_LONG))
        .collect(Collectors.toList());

    assertEquals(1, filteredList.size());
  }

  @Test
  void testPredicateOnListWithOneFailing() {
    List<User> userList = List.of(generateUser(TEST_LAT.subtract(new BigDecimal("5")), TEST_LONG));
    List<User> filteredList = userList.stream()
        .filter(userService.filter50MilesFrom(TEST_LAT, TEST_LONG))
        .collect(Collectors.toList());

    assertTrue(filteredList.isEmpty());
  }

  @Test
  void testPredicateOnListWithOnePassingAndOneFailing() {
    List<User> userList = List.of(
        generateUser(TEST_LAT, TEST_LONG),
        generateUser(TEST_LAT.subtract(new BigDecimal("5")), TEST_LONG));
    List<User> filteredList = userList.stream()
        .filter(userService.filter50MilesFrom(TEST_LAT, TEST_LONG))
        .collect(Collectors.toList());

    assertEquals(1, filteredList.size());
  }

  @Test
  void testPredicateOnListWithTwoPassingAndOneFailing() {
    List<User> userList = List.of(
        generateUser(TEST_LAT, TEST_LONG),
        generateUser(TEST_LAT.subtract(new BigDecimal("5")), TEST_LONG),
        generateUser(TEST_LAT, TEST_LONG));
    List<User> filteredList = userList.stream()
        .filter(userService.filter50MilesFrom(TEST_LAT, TEST_LONG))
        .collect(Collectors.toList());

    assertEquals(2, filteredList.size());
  }

  private User generateUser(BigDecimal latitude, BigDecimal longitude) {
    return new User(
        1L,
        "firstName",
        "lastName",
        "email@example.org",
        "127.0.0.1",
        latitude,
        longitude
    );
  }

}
