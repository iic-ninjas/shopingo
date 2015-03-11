package com.iic.shopingo.api.models.converters;

import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.services.CurrentUser;

/**
 * Created by assafgelber on 3/10/15.
 */
public class StatusConverter {
  public static class RequestStatus {
    public static BaseRequest.RequestStatus convert(String status) {
      return BaseRequest.RequestStatus.valueOf(status.toUpperCase());
    }
  }

  public static class UserState {
    public static CurrentUser.State convert(String status) {
      return CurrentUser.State.valueOf(status.toUpperCase());
    }
  }
}
