package com.iic.shopingo.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by assafgelber on 3/10/15.
 */
public class ApiCurrentState {
  public String state;

  @SerializedName("active_trip")
  public List<ApiIncomingRequest> activeTripRequests;

  @SerializedName("active_request")
  public ApiOutgoingRequest activeOutgoingRequest;

  public ApiCurrentState() {
  }
}
