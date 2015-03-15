package com.iic.shopingo.api.user;

import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.models.ApiCurrentState;
import com.iic.shopingo.api.models.ApiIncomingRequest;
import com.iic.shopingo.api.models.converters.IncomingRequestConverter;
import com.iic.shopingo.api.models.converters.OutgoingRequestConverter;
import com.iic.shopingo.api.models.converters.StatusConverter;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.services.CurrentUser;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 11/03/15.
 */
public class CurrentStateApiResult extends ApiResult {
  public CurrentUser.State userState;
  public List<IncomingRequest> activeTripRequests;
  public OutgoingRequest activeOutgoingRequest;

  public CurrentStateApiResult(ApiCurrentState apiCurrentState) {
    this.userState = StatusConverter.UserState.convert(apiCurrentState.state);
    if (apiCurrentState.activeTripRequests != null) {
      this.activeTripRequests = new ArrayList<>();
      for (ApiIncomingRequest apiRequest : apiCurrentState.activeTripRequests) {
        this.activeTripRequests.add(IncomingRequestConverter.convert(apiRequest));
      }
    }
    if (apiCurrentState.activeOutgoingRequest != null) {
      this.activeOutgoingRequest = OutgoingRequestConverter.convert(apiCurrentState.activeOutgoingRequest);
    }
  }

  public CurrentStateApiResult(String errorMessage) {
    super(errorMessage);
  }
}
