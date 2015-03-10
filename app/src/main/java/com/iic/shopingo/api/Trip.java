package com.iic.shopingo.api;

import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.IncomingRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 09/03/15.
 */
public class Trip {
  public static APIResult startTrip() {
    ServerSim.simulateRequest();
    return new APIResult();
  }

  public static PendingRequestsAPIResult getPendingRequests() {
    // TODO: use the user's current position instead of his address?
    ServerSim.simulateRequest();
    List<IncomingRequest> requests = new ArrayList<>(10);

    for (int i = 0; i < 10; ++i) {
      List<String> items = new ArrayList<>(4);
      items.add("1 Milk");
      items.add("1 Bread");
      items.add("1 Cheese");
      items.add("12 Eggs");
      IncomingRequest req = new IncomingRequest(
          "12345",
          new Contact(
              "12345",
              "Moshe",
              Integer.toString(i),
              "AVATAR",
              "12345",
              "13 Rothschild Ave.",
              "Tel Aviv",
              32.063146,
              34.770706
          ),
          new com.iic.shopingo.dal.models.ShoppingList(items, 500),
          BaseRequest.RequestStatus.PENDING
      );
      requests.add(req);
    }
    return new PendingRequestsAPIResult(requests);
  }

  public static APIResult acceptRequest(int requestId) {
    ServerSim.simulateRequest();
    return new APIResult();
  }

  public static APIResult declineRequest(int requestId) {
    ServerSim.simulateRequest();
    return new APIResult();
  }

  public static APIResult endTrip() {
    ServerSim.simulateRequest();
    return new APIResult();
  }

  public static class PendingRequestsAPIResult extends APIResult {
    public List<IncomingRequest> requests;

    public PendingRequestsAPIResult(List<IncomingRequest> requests) {
      super();
      this.requests = requests;
    }

    public PendingRequestsAPIResult(String errorMessage) {
      super(errorMessage);
    }
  }
}
