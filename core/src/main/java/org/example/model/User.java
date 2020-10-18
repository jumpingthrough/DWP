package org.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class User {

  private final long id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final String ipAddress;
  private final BigDecimal latitude;
  private final BigDecimal longitude;

  @JsonCreator
  public User(
      @JsonProperty("id") long id,
      @JsonProperty("first_name") String firstName,
      @JsonProperty("last_name") String lastName,
      @JsonProperty("email") String email,
      @JsonProperty("ip_address") String ipAddress,
      @JsonProperty("latitude") BigDecimal latitude,
      @JsonProperty("longitude") BigDecimal longitude) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.ipAddress = ipAddress;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

}
