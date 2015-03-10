package com.iic.shopingo.api.models;

/**
 * Created by assafgelber on 3/10/15.
 */
public class ApiUserInfo extends ApiContact {
  public int state;

  public ApiUserInfo() {
    super();
  }

  public ApiUserInfo(String facebookId, String firstName, String lastName, String streetAddress, String city, String phoneNumber, double latitude, double longitude, int state) {
    super(facebookId, firstName, lastName, streetAddress, city, phoneNumber, latitude, longitude);
    this.state = state;
  }
}
