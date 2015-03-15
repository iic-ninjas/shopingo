package com.iic.shopingo.api.trip;

import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.BaseApiCommand;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiSimpleResponse;

/**
 * Created by asafg on 11/03/15.
 */
public class EndTripCommand extends BaseApiCommand<ApiResult> {
  public EndTripCommand(String authToken) {
    super(authToken);
  }

  @Override
  public ApiResult executeSync() {
    try {
      ApiSimpleResponse response = Server.post(authToken, Constants.Routes.TRIPS_END_PATH, ApiSimpleResponse.class);
      return new ApiResult(response);
    } catch (Exception e) {
      return new ApiResult(e.getMessage());
    }
  }
}
