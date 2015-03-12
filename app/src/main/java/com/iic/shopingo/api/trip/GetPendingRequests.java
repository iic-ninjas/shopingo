package com.iic.shopingo.api.trip;

import com.iic.shopingo.api.BaseApiRequest;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiIncomingRequest;
import com.iic.shopingo.api.trip.PendingRequestsApiResult;
import com.iic.shopingo.services.CurrentUser;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by asafg on 11/03/15.
 */
public class GetPendingRequests extends BaseApiRequest<PendingRequestsApiResult> {
  public GetPendingRequests(String authToken) {
    super(authToken);
  }

  @Override
  public PendingRequestsApiResult executeSync() {
    try {
      ApiIncomingRequest[] response = server.get(Constants.Routes.REQUESTS_INDEX_PATH, ApiIncomingRequest[].class);
      return new PendingRequestsApiResult(Arrays.asList(response));
    } catch (Exception e) {
      return new PendingRequestsApiResult(e.getMessage());
    }
  }
}
