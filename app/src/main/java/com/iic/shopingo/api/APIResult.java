package com.iic.shopingo.api;

import com.iic.shopingo.api.models.ApiSimpleResponse;

/**
 * Created by asafg on 09/03/15.
 */
public class APIResult {
  public boolean success;
  public String errorMessage;

  public APIResult() {
    success = true;
    errorMessage = null;
  }

  public APIResult(String error) {
    errorMessage = error;
  }

  public APIResult(ApiSimpleResponse response) {
    if (response.success && response.errorMessage != null) {
      throw new IllegalArgumentException("Response can not be marked as successful and contain an error message");
    }
    success = response.success;
    errorMessage = response.errorMessage;
  }
}
