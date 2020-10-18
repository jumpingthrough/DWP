package org.example.model;

import java.math.BigDecimal;

public class User {

  private final long id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final String ipAddress;
  private final BigDecimal latitude;
  private final BigDecimal longitude;

  public User(
      long id,
      String firstName,
      String lastName,
      String email,
      String ipAddress,
      BigDecimal latitude,
      BigDecimal longitude) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.ipAddress = ipAddress;
    this.latitude = latitude;
    this.longitude = longitude;
  }

}
