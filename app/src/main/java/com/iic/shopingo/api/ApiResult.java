package com.iic.shopingo.api;

import com.iic.shopingo.api.models.ApiSimpleResponse;

/**
 * Created by asafg on 09/03/15.
 */
public class ApiResult {
  public boolean success;
  public String errorMessage;

  public ApiResult() {
    success = true;
    errorMessage = null;
  }

  public ApiResult(String error) {
    success = false;
    errorMessage = error;
  }

  public ApiResult(ApiSimpleResponse response) {
    if (response.success && response.errorMessage != null) {
      throw new IllegalArgumentException("Response can not be marked as successful and contain an error message");
    }
    success = response.success;
    errorMessage = response.errorMessage;
  }
}
