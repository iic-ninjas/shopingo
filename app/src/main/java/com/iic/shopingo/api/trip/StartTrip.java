package com.iic.shopingo.api.trip;

import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.BaseApiRequest;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiSimpleResponse;
import com.iic.shopingo.services.CurrentUser;
import java.io.IOException;

/**
 * Created by asafg on 11/03/15.
 */
public class StartTrip extends BaseApiRequest<ApiResult> {
  public StartTrip(String authToken) {
    super(authToken);
  }

  @Override
  public ApiResult executeSync() {
    try {
      ApiSimpleResponse response = server.post(Constants.Routes.TRIPS_START_PATH, ApiSimpleResponse.class);
      return new ApiResult(response);
    } catch (Exception e) {
      return new ApiResult(e.getMessage());
    }
  }
}
