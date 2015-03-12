package com.iic.shopingo.api.user;

import com.iic.shopingo.api.BaseApiRequest;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiCurrentState;
import com.iic.shopingo.services.CurrentUser;
import java.io.IOException;

/**
 * Created by asafg on 11/03/15.
 */
public class GetCurrentState extends BaseApiRequest<CurrentStateApiResult> {

  public GetCurrentState(String authToken) {
    super(authToken);
  }

  @Override
  public CurrentStateApiResult executeSync() {
    try {
      ApiCurrentState response = server.get(Constants.Routes.USERS_STATE_PATH, ApiCurrentState.class);
      return new CurrentStateApiResult(response);
    } catch (Exception e) {
      return new CurrentStateApiResult(e.getMessage());
    }
  }
}
