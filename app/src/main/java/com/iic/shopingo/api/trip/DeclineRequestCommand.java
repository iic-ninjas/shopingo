package com.iic.shopingo.api.trip;

import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.BaseApiCommand;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiSimpleResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asafg on 11/03/15.
 */
public class DeclineRequestCommand extends BaseApiCommand<ApiResult> {

  private final String requestId;

  public DeclineRequestCommand(String authToken, String requestId) {
    super(authToken);
    this.requestId = requestId;
  }

  @Override
  public ApiResult executeSync() {
    try {
      Map<String, Object> params = new HashMap<>();
      params.put(Constants.Parameters.REQUESTS_REQUEST_ID, requestId);
      ApiSimpleResponse response = Server.post(authToken, Constants.Routes.REQUESTS_DECLINE_PATH_TEMPLATE, ApiSimpleResponse.class, params);
      return new ApiResult(response);
    } catch (Exception e) {
      return new ApiResult(e.getMessage());
    }
  }
}
