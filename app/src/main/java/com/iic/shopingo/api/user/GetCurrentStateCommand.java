package com.iic.shopingo.api.user;

import com.iic.shopingo.api.BaseApiCommand;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiCurrentState;

/**
 * Created by asafg on 11/03/15.
 */
public class GetCurrentStateCommand extends BaseApiCommand<CurrentStateApiResult> {

  public GetCurrentStateCommand(String authToken) {
    super(authToken);
  }

  @Override
  public CurrentStateApiResult executeSync() {
    try {
      ApiCurrentState response = Server.get(authToken, Constants.Routes.USERS_STATE_PATH, ApiCurrentState.class);
      return new CurrentStateApiResult(response);
    } catch (Exception e) {
      return new CurrentStateApiResult(e.getMessage());
    }
  }
}
