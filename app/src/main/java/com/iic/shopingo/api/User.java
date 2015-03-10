package com.iic.shopingo.api;

import com.iic.shopingo.api.models.ApiCurrentState;
import com.iic.shopingo.api.models.ApiIncomingRequest;
import com.iic.shopingo.api.models.ApiUserInfo;
import com.iic.shopingo.api.models.converters.IncomingRequestConverter;
import com.iic.shopingo.api.models.converters.OutgoingRequestConverter;
import com.iic.shopingo.api.models.converters.UserInfoConverter;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.dal.models.UserInfo;
import com.iic.shopingo.services.CurrentUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

  public static CurrentStateAPIResult getCurrentState() {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      ApiCurrentState response = server.get(Constants.Routes.USERS_STATE_PATH, ApiCurrentState.class);
      return new CurrentStateAPIResult(response);
    } catch (IOException e) {
      return new CurrentStateAPIResult(e.getMessage());
    }
  }

  public static class UserAPIResult extends APIResult {
    public UserInfo userContactInfo;
    public CurrentUser.State userState;

    public UserAPIResult(ApiUserInfo apiUserInfo) {
      super();
      this.userContactInfo = UserInfoConverter.convert(apiUserInfo);
      this.userState = CurrentUser.State.values()[apiUserInfo.state];
    }

    public UserAPIResult(String errorMessage) {
      super(errorMessage);
    }
  }

  public static class CurrentStateAPIResult extends APIResult {
    public CurrentUser.State userState;
    public List<IncomingRequest> activeTripRequests;
    public OutgoingRequest activeOutgoingRequest;

    public CurrentStateAPIResult(ApiCurrentState apiCurrentState) {
      super();
      this.userState = CurrentUser.State.values()[apiCurrentState.state];
      List<IncomingRequest> tempRequests = new ArrayList<>();
      for (ApiIncomingRequest apiRequest : apiCurrentState.activeTripRequests) {
        tempRequests.add(IncomingRequestConverter.convert(apiRequest));
      }
      this.activeTripRequests = tempRequests;
      this.activeOutgoingRequest = OutgoingRequestConverter.convert(apiCurrentState.activeOutgoingRequest);
    }

    public CurrentStateAPIResult(String errorMessage) {
      super(errorMessage);
    }
  }
}
