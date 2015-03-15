package com.iic.shopingo.api.request;

import com.iic.shopingo.api.BaseApiCommand;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiOutgoingRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asafg on 11/03/15.
 */
public class MakeRequestCommand extends BaseApiCommand<OutgoingRequestApiResult> {

  private List<String> shoppingItems;
  private int offer;
  private String shopperId;

  public MakeRequestCommand(String authToken, List<String> shoppingItems, int offer, String shopperId) {
    super(authToken);
    this.shoppingItems = shoppingItems;
    this.offer = offer;
    this.shopperId = shopperId;
  }

  @Override
  public OutgoingRequestApiResult executeSync() {
    try {
      Map<String, Object> params = new HashMap<>();
      params.put(Constants.Parameters.REQUESTS_SHOPPER_ID, shopperId);
      params.put(Constants.Parameters.REQUESTS_OFFER, offer);
      params.put(Constants.Parameters.REQUESTS_ITEMS, shoppingItems);
      ApiOutgoingRequest response = Server.post(authToken, Constants.Routes.REQUESTS_CREATE_PATH, ApiOutgoingRequest.class, params);
      return new OutgoingRequestApiResult(response);
    } catch (Exception e) {
      return new OutgoingRequestApiResult(e.getMessage());
    }
  }
}
