package com.iic.shopingo.api.trip;

import com.iic.shopingo.api.BaseApiCommand;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiIncomingRequest;
import java.util.Arrays;

/**
 * Created by asafg on 11/03/15.
 */
public class GetPendingRequestsCommand extends BaseApiCommand<PendingRequestsApiResult> {
  public GetPendingRequestsCommand(String authToken) {
    super(authToken);
  }

  @Override
  public PendingRequestsApiResult executeSync() {
    try {
      ApiIncomingRequest[] response = Server.get(authToken, Constants.Routes.REQUESTS_INDEX_PATH, ApiIncomingRequest[].class);
      return new PendingRequestsApiResult(Arrays.asList(response));
    } catch (Exception e) {
      return new PendingRequestsApiResult(e.getMessage());
    }
  }
}
