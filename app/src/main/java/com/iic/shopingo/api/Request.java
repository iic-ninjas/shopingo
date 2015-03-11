package com.iic.shopingo.api;

import com.iic.shopingo.api.models.ApiContact;
import com.iic.shopingo.api.models.ApiOutgoingRequest;
import com.iic.shopingo.api.models.ApiSimpleResponse;
import com.iic.shopingo.api.models.converters.ContactConverter;
import com.iic.shopingo.api.models.converters.OutgoingRequestConverter;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.services.CurrentUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asafg on 09/03/15.
 */
public class Request {
  public static OutgoingRequestApiResult makeRequest(List<String> shoppingItems, int offer, String shopperId) {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      Map<String, Object> params = new HashMap<>();
      params.put(Constants.Parameters.REQUESTS_SHOPPER_ID, shopperId);
      params.put(Constants.Parameters.REQUESTS_OFFER, offer);
      params.put(Constants.Parameters.REQUESTS_ITEMS, shoppingItems);
      ApiOutgoingRequest response = server.post(Constants.Routes.REQUESTS_CREATE_PATH, ApiOutgoingRequest.class, params);
      return new OutgoingRequestApiResult(response);
    } catch (IOException e) {
      return new OutgoingRequestApiResult(e.getMessage());
    }
  }

  public static ApiResult cancelRequest() {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      ApiSimpleResponse response = server.post(Constants.Routes.REQUESTS_CANCEL_PATH, ApiSimpleResponse.class);
      return new ApiResult(response);
    } catch (IOException e) {
      return new ApiResult(e.getMessage());
    }
  }

  public static ApiResult settleRequest() {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      ApiSimpleResponse response = server.post(Constants.Routes.REQUESTS_SETTLE_PATH, ApiSimpleResponse.class);
      return new ApiResult(response);
    } catch (IOException e) {
      return new ApiResult(e.getMessage());
    }
  }

  public static ShoppersApiResult getNearbyShoppers() {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      ApiContact[] response = server.get(Constants.Routes.TRIPS_INDEX_PATH, ApiContact[].class);
      return new ShoppersApiResult(Arrays.asList(response));
    } catch (IOException e) {
      return new ShoppersApiResult(e.getMessage());
    }
  }

  public static class OutgoingRequestApiResult extends ApiResult {

    public OutgoingRequest request;

    public OutgoingRequestApiResult(ApiOutgoingRequest request) {
      super();
      this.request = OutgoingRequestConverter.convert(request);
    }

    public OutgoingRequestApiResult(String errorMessage) {
      super(errorMessage);
    }
  }

  private static class ShoppersApiResult extends ApiResult {
    public List<Contact> shoppers;

    public ShoppersApiResult(List<ApiContact> shoppers) {
      super();
      List<Contact> tempShoppers = new ArrayList<>();
      for (ApiContact apiShopper : shoppers) {
        tempShoppers.add(ContactConverter.convert(apiShopper));
      }
      this.shoppers = tempShoppers;
    }

    public ShoppersApiResult(String errorMessage) {
      super(errorMessage);
    }
  }
}
