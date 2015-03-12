package com.iic.shopingo.api.user;

import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.BaseApiRequest;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiSimpleResponse;
import com.iic.shopingo.api.models.ApiUserInfo;
import com.iic.shopingo.services.CurrentUser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asafg on 11/03/15.
 */
public class UpdateDetails extends BaseApiRequest<ApiResult> {
  private String facebookId;
  private String firstName;
  private String lastName;
  private String streetAddress;
  private String city;
  private String phoneNumber;

  public UpdateDetails(String authToken, String facebookId, String firstName, String lastName, String streetAddress, String city, String phoneNumber) {
    super(authToken);
    this.facebookId = facebookId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.streetAddress = streetAddress;
    this.city = city;
    this.phoneNumber = phoneNumber;
  }

  @Override
  public ApiResult executeSync() {
    try {
      Map<String, Object> params = new HashMap<>();
      params.put(Constants.Parameters.USERS_FACEBOOK_ID, facebookId);
      params.put(Constants.Parameters.USERS_FIRST_NAME, firstName);
      params.put(Constants.Parameters.USERS_LAST_NAME, lastName);
      params.put(Constants.Parameters.USERS_STREET_ADDRESS, streetAddress);
      params.put(Constants.Parameters.USERS_CITY, city);
      params.put(Constants.Parameters.USERS_PHONE_NUMBER, phoneNumber);
      ApiSimpleResponse response = server.post(Constants.Routes.USERS_UPDATE_PATH, ApiSimpleResponse.class, params);
      return new ApiResult(response);
    } catch (Exception e) {
      return new ApiResult(e.getMessage());
    }
  }
}
