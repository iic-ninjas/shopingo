package com.iic.shopingo.dal.models;

import java.util.List;

/**
 * Created by assafgelber on 3/8/15.
 */
public class Trip {
  private List<IncomingRequest> requests;

  public Trip(List<IncomingRequest> requests) {
    this.requests = requests;
  }

  public List<IncomingRequest> getRequests() {
    return requests;
  }
}
