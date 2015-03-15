package com.iic.shopingo.api.request;

import com.iic.shopingo.api.BaseApiCommand;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiContact;
import java.util.Arrays;

/**
 * Created by asafg on 11/03/15.
 */
public class GetNearbyShoppersCommand extends BaseApiCommand<ShoppersApiResult> {
  public GetNearbyShoppersCommand(String authToken) {
    super(authToken);
  }

  @Override
  public ShoppersApiResult executeSync() {
    try {
      ApiContact[] response = Server.get(authToken, Constants.Routes.TRIPS_INDEX_PATH, ApiContact[].class);
      return new ShoppersApiResult(Arrays.asList(response));
    } catch (Exception e) {
      return new ShoppersApiResult(e.getMessage());
    }
  }
}
