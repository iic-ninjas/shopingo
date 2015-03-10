package com.iic.shopingo.api;

import com.iic.shopingo.dal.models.UserInfo;

/**
 * Created by asafg on 09/03/15.
 */
public class User {

  public static UserAPIResult login(String facebookId, String firstName, String lastName, String streetAddress, String city, String phoneNumber) {
    ServerSim.simulateRequest();
    return new UserAPIResult(new UserInfo(
        facebookId,
        firstName,
        lastName,
        streetAddress,
        city,
        phoneNumber
    ));
  }

  public static UserAPIResult updateDetails(String facebookId, String firstName, String lastName, String streetAddress, String city, String phoneNumber) {
    ServerSim.simulateRequest();
    return new UserAPIResult(new UserInfo(
        facebookId,
        firstName,
        lastName,
        streetAddress,
        city,
        phoneNumber
    ));
  }

  public static class UserAPIResult extends APIResult {
    public UserInfo userContactInfo;

    public UserAPIResult(UserInfo userContactInfo) {
      super();
      this.userContactInfo = userContactInfo;
    }

    public UserAPIResult(String errorMessage) {
      super(errorMessage);
    }
  }
}
