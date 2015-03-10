package com.iic.shopingo.api.models.converters;

import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.services.CurrentUser;
import java.util.HashMap;

/**
 * Created by assafgelber on 3/10/15.
 */
public class StatusConverter {
  public static class RequestStatus {
    private static final HashMap<String, Integer> STATUS_MAP = new HashMap<String, Integer>() {{
      put("pending", BaseRequest.RequestStatus.PENDING.ordinal());
      put("accepted", BaseRequest.RequestStatus.ACCEPTED.ordinal());
      put("declined", BaseRequest.RequestStatus.DECLINED.ordinal());
      put("settled", BaseRequest.RequestStatus.SETTLED.ordinal());
      put("canceled", BaseRequest.RequestStatus.CANCELED.ordinal());
    }};

    public static BaseRequest.RequestStatus convert(String status) {
      return BaseRequest.RequestStatus.values()[STATUS_MAP.get(status)];
    }
  }

  public static class UserState {
    private static final HashMap<String, Integer> STATUS_MAP = new HashMap<String, Integer>() {{
      put("idle", CurrentUser.State.IDLE.ordinal());
      put("tripping", CurrentUser.State.TRIPPING.ordinal());
      put("requesting", CurrentUser.State.REQUESTING.ordinal());
    }};

    public static CurrentUser.State convert(String status) {
      return CurrentUser.State.values()[STATUS_MAP.get(status)];
    }
  }
}
