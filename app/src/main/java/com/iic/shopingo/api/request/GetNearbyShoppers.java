package com.iic.shopingo.api.request;

import com.iic.shopingo.api.BaseApiRequest;
import com.iic.shopingo.api.Constants;
import com.iic.shopingo.api.Server;
import com.iic.shopingo.api.models.ApiContact;
import com.iic.shopingo.services.CurrentUser;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by asafg on 11/03/15.
 */
public class GetNearbyShoppers extends BaseApiRequest<ShoppersApiResult> {
  public GetNearbyShoppers(String authToken) {
    super(authToken);
  }

  @Override
  public ShoppersApiResult executeSync() {
    try {
      ApiContact[] response = server.get(Constants.Routes.TRIPS_INDEX_PATH, ApiContact[].class);
      return new ShoppersApiResult(Arrays.asList(response));
    } catch (Exception e) {
      return new ShoppersApiResult(e.getMessage());
    }
  }
}
