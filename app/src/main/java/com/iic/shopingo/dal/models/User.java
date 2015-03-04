package com.iic.shopingo.dal.models;

/**
 * Created by ifeins on 3/3/15.
 */
public class User {

  //private static final String AVATAR_URL_FORMAT = "https://graph.facebook.com/%s/picture?width=100&height=100";
  private static final String AVATAR_URL_FORMAT = "https://graph.facebook.com/%s/picture?type=large";

  private String uid;

  private String firstName;

  private String lastName;

  private String street;

  private String city;

  private String phoneNumber;
  
  public User(String uid, String firstName, String lastName, String street, String city, String phoneNumber) {
    this.uid = uid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.street = street;
    this.city = city;
    this.phoneNumber = phoneNumber;
  }

  public String getUid() {
    return uid;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getAvatarUrl() {
    return String.format(AVATAR_URL_FORMAT, getUid());
  }
}
