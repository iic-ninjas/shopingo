package com.iic.shopingo.api.models;

import java.util.List;

/**
 * Created by assafgelber on 3/10/15.
 */
public class ApiIncomingRequest extends ApiSimpleResponse {
  public String id;

  public ApiContact requester;

  public int status;

  public List<String> items;

  public int offer;

  public ApiIncomingRequest() {
    super();
  }
}
