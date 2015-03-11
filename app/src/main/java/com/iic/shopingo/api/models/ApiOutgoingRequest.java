package com.iic.shopingo.api.models;

import java.util.List;

/**
 * Created by assafgelber on 3/10/15.
 */
public class ApiOutgoingRequest extends ApiSimpleResponse {
  public String id;

  public ApiContact shopper;

  public String status;

  public List<String> items;

  public int offer;
}
