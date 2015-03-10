package com.iic.shopingo.api;

import com.iic.shopingo.api.models.ApiUserInfo;
import com.iic.shopingo.api.models.converters.UserInfoConverter;
import com.iic.shopingo.dal.models.UserInfo;
import com.iic.shopingo.services.CurrentUser;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by asafg on 09/03/15.
 */
public class User {

  public static UserAPIResult login(String facebookId, String firstName, String lastName, String streetAddress, String city, String phoneNumber) {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      HashMap<String, Object> params = new HashMap<>();
      params.put(Constants.Parameters.USERS_FACEBOOK_ID, facebookId);
      params.put(Constants.Parameters.USERS_FIRST_NAME, firstName);
      params.put(Constants.Parameters.USERS_LAST_NAME, lastName);
      params.put(Constants.Parameters.USERS_STREET_ADDRESS, streetAddress);
      params.put(Constants.Parameters.USERS_CITY, city);
      params.put(Constants.Parameters.USERS_PHONE_NUMBER, phoneNumber);
      ApiUserInfo response = server.post(Constants.Routes.USERS_LOGIN_PATH, ApiUserInfo.class, params);
      return new UserAPIResult(response);
    } catch (IOException e) {
      return new UserAPIResult(e.getMessage());
    }
  }

  public static UserAPIResult updateDetails(String facebookId, String firstName, String lastName, String streetAddress, String city, String phoneNumber) {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      HashMap<String, Object> params = new HashMap<>();
      params.put(Constants.Parameters.USERS_FACEBOOK_ID, facebookId);
      params.put(Constants.Parameters.USERS_FACEBOOK_ID, facebookId);
      params.put(Constants.Parameters.USERS_FIRST_NAME, firstName);
      params.put(Constants.Parameters.USERS_LAST_NAME, lastName);
      params.put(Constants.Parameters.USERS_STREET_ADDRESS, streetAddress);
      params.put(Constants.Parameters.USERS_CITY, city);
      params.put(Constants.Parameters.USERS_PHONE_NUMBER, phoneNumber);
      ApiUserInfo response = server.post(Constants.Routes.USERS_UPDATE_PATH, ApiUserInfo.class, params);
      return new UserAPIResult(response);
    } catch (IOException e) {
      return new UserAPIResult(e.getMessage());
    }
  }

  public static class UserAPIResult extends APIResult {
    public UserInfo userContactInfo;
    public int userState;

    public UserAPIResult(ApiUserInfo apiUserInfo) {
      super();
      this.userContactInfo = UserInfoConverter.convert(apiUserInfo);
      this.userState = apiUserInfo.state;
    }

    public UserAPIResult(String errorMessage) {
      super(errorMessage);
    }
  }
}
