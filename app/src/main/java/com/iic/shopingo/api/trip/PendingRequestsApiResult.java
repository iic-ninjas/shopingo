package com.iic.shopingo.api.trip;

import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.models.ApiIncomingRequest;
import com.iic.shopingo.api.models.converters.IncomingRequestConverter;
import com.iic.shopingo.dal.models.IncomingRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 11/03/15.
 */
public class PendingRequestsApiResult extends ApiResult {
  public List<IncomingRequest> requests;

  public PendingRequestsApiResult(List<ApiIncomingRequest> requests) {
    super();
    List<IncomingRequest> tempRequests = new ArrayList<>();
    for (ApiIncomingRequest apiRequest : requests) {
      tempRequests.add(IncomingRequestConverter.convert(apiRequest));
    }
    this.requests = tempRequests;
  }

  public PendingRequestsApiResult(String errorMessage) {
    super(errorMessage);
  }
}
