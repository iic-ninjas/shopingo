package com.iic.shopingo.api.models;

import java.util.List;

/**
 * Created by assafgelber on 3/10/15.
 */
public class ApiOutgoingRequest {
  public String id;

  public ApiContact shopper;

  public int status;

  public List<String> items;

  public int offer;

  public ApiOutgoingRequest() {
  }

  public ApiOutgoingRequest(String id, ApiContact shopper, int status, List<String> items, int offer) {
    this.id = id;
    this.shopper = shopper;
    this.status = status;
    this.items = items;
    this.offer = offer;
  }
}
