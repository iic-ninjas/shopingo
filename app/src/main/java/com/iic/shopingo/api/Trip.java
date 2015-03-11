package com.iic.shopingo.api;

import com.iic.shopingo.api.models.ApiIncomingRequest;
import com.iic.shopingo.api.models.ApiSimpleResponse;
import com.iic.shopingo.api.models.converters.IncomingRequestConverter;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.services.CurrentUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by asafg on 09/03/15.
 */
public class Trip {
  public static ApiResult startTrip() {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      ApiSimpleResponse response = server.post(Constants.Routes.TRIPS_START_PATH, ApiSimpleResponse.class);
      return new ApiResult(response);
    } catch (IOException e) {
      return new ApiResult(e.getMessage());
    }
  }

  public static PendingRequestsApiResult getPendingRequests() {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      ApiIncomingRequest[] response = server.get(Constants.Routes.REQUESTS_INDEX_PATH, ApiIncomingRequest[].class);
      return new PendingRequestsApiResult(Arrays.asList(response));
    } catch (IOException e) {
      return new PendingRequestsApiResult(e.getMessage());
    }
  }

  public static ApiResult acceptRequest(int requestId) {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      HashMap<String, Object> params = new HashMap<>();
      String path = String.format(Constants.Routes.REQUESTS_ACCEPT_PATH_TEMPLATE, requestId);
      ApiSimpleResponse response = server.post(path, ApiSimpleResponse.class, params);
      return new ApiResult(response);
    } catch (IOException e) {
      return new ApiResult(e.getMessage());
    }
  }

  public static ApiResult declineRequest(int requestId) {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      HashMap<String, Object> params = new HashMap<>();
      String path = String.format(Constants.Routes.REQUESTS_DECLINE_PATH_TEMPLATE, requestId);
      ApiSimpleResponse response = server.post(path, ApiSimpleResponse.class, params);
      return new ApiResult(response);
    } catch (IOException e) {
      return new ApiResult(e.getMessage());
    }
  }

  public static ApiResult endTrip() {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      ApiSimpleResponse response = server.post(Constants.Routes.TRIPS_END_PATH, ApiSimpleResponse.class);
      return new ApiResult(response);
    } catch (IOException e) {
      return new ApiResult(e.getMessage());
    }
  }

  public static class PendingRequestsApiResult extends ApiResult {
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
}
