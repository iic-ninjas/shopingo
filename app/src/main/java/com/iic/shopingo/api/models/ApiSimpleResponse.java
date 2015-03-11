package com.iic.shopingo.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by assafgelber on 3/10/15.
 */
public class ApiSimpleResponse {
  public boolean success = true;

  @SerializedName("error")
  public String errorMessage;

  public ApiSimpleResponse() {
  }
}
