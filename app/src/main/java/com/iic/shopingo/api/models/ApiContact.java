package com.iic.shopingo.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by assafgelber on 3/10/15.
 */
public class ApiContact extends ApiSimpleResponse {
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
    super();
  }
}
