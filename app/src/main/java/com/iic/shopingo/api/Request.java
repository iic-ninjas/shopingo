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

/**
 * Created by asafg on 09/03/15.
 */
public class Request {
  public static OutgoingRequestAPIResult makeRequest(List<String> shoppingItems, int offer, String shopperId) {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      HashMap<String, Object> params = new HashMap<>();
      params.put(Constants.Parameters.REQUESTS_SHOPPER_ID, shopperId);
      params.put(Constants.Parameters.REQUESTS_OFFER, offer);
      params.put(Constants.Parameters.REQUESTS_ITEMS, shoppingItems);
      ApiOutgoingRequest response = server.post(Constants.Routes.REQUESTS_CREATE_PATH, ApiOutgoingRequest.class, params);
      return new OutgoingRequestAPIResult(response);
    } catch (IOException e) {
      return new OutgoingRequestAPIResult(e.getMessage());
    }
  }

  public static APIResult cancelRequest() {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      ApiSimpleResponse response = server.post(Constants.Routes.REQUESTS_CANCEL_PATH, ApiSimpleResponse.class);
      return new APIResult(response);
    } catch (IOException e) {
      return new APIResult(e.getMessage());
    }
  }

  public static APIResult settleRequest() {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      ApiSimpleResponse response = server.post(Constants.Routes.REQUESTS_SETTLE_PATH, ApiSimpleResponse.class);
      return new APIResult(response);
    } catch (IOException e) {
      return new APIResult(e.getMessage());
    }
  }

  public static ShoppersAPIResult getNearbyShoppers() {
    try {
      Server server = new Server(CurrentUser.getInstance().userInfo.getUid());
      ApiContact[] response = server.get(Constants.Routes.TRIPS_INDEX_PATH, ApiContact[].class);
      return new ShoppersAPIResult(Arrays.asList(response));
    } catch (IOException e) {
      return new ShoppersAPIResult(e.getMessage());
    }
  }

  public static class OutgoingRequestAPIResult extends APIResult {

    public OutgoingRequest request;

    public OutgoingRequestAPIResult(ApiOutgoingRequest request) {
      super();
      this.request = OutgoingRequestConverter.convert(request);
    }

    public OutgoingRequestAPIResult(String errorMessage) {
      super(errorMessage);
    }
  }

  private static class ShoppersAPIResult extends APIResult {
    public List<Contact> shoppers;

    public ShoppersAPIResult(List<ApiContact> shoppers) {
      super();
      List<Contact> tempShoppers = new ArrayList<>();
      for (ApiContact apiShopper : shoppers) {
        tempShoppers.add(ContactConverter.convert(apiShopper));
      }
      this.shoppers = tempShoppers;
    }

    public ShoppersAPIResult(String errorMessage) {
      super(errorMessage);
    }
  }
}
