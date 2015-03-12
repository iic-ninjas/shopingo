package com.iic.shopingo.api.request;

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
public class CancelRequest extends BaseApiRequest<ApiResult> {

  public CancelRequest(String authToken) {
    super(authToken);
  }

  @Override
  public ApiResult executeSync() {
    try {
      ApiSimpleResponse response = server.post(Constants.Routes.REQUESTS_CANCEL_PATH, ApiSimpleResponse.class);
      return new ApiResult(response);
    } catch (Exception e) {
      return new ApiResult(e.getMessage());
    }
  }
}
