package com.iic.shopingo.api;

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

  public APIResult(String errorMessage) {
    success = false;
    this.errorMessage = errorMessage;
  }

  public APIResult(boolean success, String errorMessage) {
    this.success = success;
    this.errorMessage = errorMessage;
  }
}
