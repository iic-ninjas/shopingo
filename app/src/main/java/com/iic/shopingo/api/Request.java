package com.iic.shopingo.api;

import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.OutgoingRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 09/03/15.
 */
public class Request {
  public static OutgoingRequestAPIResult makeRequest(List<String> shoppingItems, int offer, String shopperId) {
    ServerSim.simulateRequest();
    return new OutgoingRequestAPIResult(new OutgoingRequest("12345"));
  }

  public static APIResult cancelRequest() {
    ServerSim.simulateRequest();
    return new APIResult();
  }

  public static APIResult settleRequest() {
    ServerSim.simulateRequest();
    return new APIResult();
  }

  public static ShoppersAPIResult getNearbyShoppers() {
    // TODO: possibly use the user's current location instead of address?
    ServerSim.simulateRequest();
    List<Contact> shoppers = new ArrayList<>(5);
    shoppers.add(new Contact(
        "12345",
        "Moshe",
        "Moshe",
        "http://dummyimage.com/200x200",
        "12345",
        "13 Rothschild Ave.",
        "Tel Aviv",
        32.063146,
        34.770706
    ));
    return new ShoppersAPIResult(new ArrayList<Contact>());
  }

  public static class OutgoingRequestAPIResult extends APIResult {

    public OutgoingRequest request;

    public OutgoingRequestAPIResult(OutgoingRequest request) {
      super();
      this.request = request;
    }

    public OutgoingRequestAPIResult(String errorMessage) {
      super(errorMessage);
    }
  }

  private static class ShoppersAPIResult extends APIResult {
    public List<Contact> shoppers;

    public ShoppersAPIResult(List<Contact> shoppers) {
      super();
      this.shoppers = shoppers;
    }

    public ShoppersAPIResult(String errorMessage) {
      super(errorMessage);
    }
  }
}
