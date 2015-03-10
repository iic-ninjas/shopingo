package com.iic.shopingo.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by assafgelber on 3/10/15.
 */
public class ApiContact {
  @SerializedName("facebook_id")
  public String facebookId;

  @SerializedName("first_name")
  public String firstName;

  @SerializedName("last_name")
  public String lastName;

  @SerializedName("street_address")
  public String streetAddress;

  public String city;

  @SerializedName("phone_number")
  public String phoneNumber;

  public double latitude;

  public double longitude;

  public ApiContact() {
  }

  public ApiContact(String facebookId, String firstName, String lastName, String streetAddress, String city, String phoneNumber, double latitude, double longitude) {
    this.facebookId = facebookId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.streetAddress = streetAddress;
    this.city = city;
    this.phoneNumber = phoneNumber;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
