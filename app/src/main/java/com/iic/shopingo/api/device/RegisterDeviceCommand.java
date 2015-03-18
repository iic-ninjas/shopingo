package com.iic.shopingo.api.device;

import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.BaseApiCommand;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiSimpleResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ifeins on 3/15/15.
 */
public class RegisterDeviceCommand extends BaseApiCommand<ApiResult> {

  private String gcmRegId;

  public RegisterDeviceCommand(String authToken, String gcmRegId) {
    super(authToken);
    this.gcmRegId = gcmRegId;
  }

  @Override
  public ApiResult executeSync() {
    try {
      Map<String, Object> params = new HashMap<>();
      params.put(Constants.Parameters.DEVICES_REG_ID, this.gcmRegId);

      ApiSimpleResponse response = Server.post(authToken, Constants.Routes.REGISTER_DEVICE_PATH, ApiSimpleResponse.class, params);
      return new ApiResult(response);
    } catch (IOException e) {
      return new ApiResult(e.getMessage());
    }
  }

}
