package com.iic.shopingo.api.models;

import java.util.List;

/**
 * Created by assafgelber on 3/10/15.
 */
public class ApiIncomingRequest {
  public String id;

  public ApiContact requester;

  public int status;

  public List<String> items;

  public int offer;

  public ApiIncomingRequest() {
  }

  public ApiIncomingRequest(String id, ApiContact requester, int status, List<String> items, int offer) {
    this.id = id;
    this.requester = requester;
    this.status = status;
    this.items = items;
    this.offer = offer;
  }
}
