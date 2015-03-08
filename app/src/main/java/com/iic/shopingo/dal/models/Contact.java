package com.iic.shopingo.dal.models;

/**
 * Created by assafgelber on 3/8/15.
 */
public class Contact {
  private String firstName;
  private String lastName;
  private String avatar;
  private String phoneNumber;
  private String streetAddress;
  private String city;
  private double latitiude;
  private double longitude;

  public Contact(String firstName, String lastName, String avatar, String phoneNumber, String streetAddress,
      String city, double latitiude, double longitude) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatar = avatar;
    this.phoneNumber = phoneNumber;
    this.streetAddress = streetAddress;
    this.city = city;
    this.latitiude = latitiude;
    this.longitude = longitude;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getAvatar() {
    return avatar;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public String getCity() {
    return city;
  }

  public double getLatitiude() {
    return latitiude;
  }

  public double getLongitude() {
    return longitude;
  }
}
