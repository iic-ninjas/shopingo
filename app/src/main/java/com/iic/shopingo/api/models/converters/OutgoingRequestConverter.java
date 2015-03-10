package com.iic.shopingo.api.models.converters;

import com.iic.shopingo.api.models.ApiOutgoingRequest;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.dal.models.ShoppingList;

/**
 * Created by assafgelber on 3/10/15.
 */
public class OutgoingRequestConverter {
  public static OutgoingRequest convert(ApiOutgoingRequest apiRequest) {
    return new OutgoingRequest(
        apiRequest.id,
        ContactConverter.convert(apiRequest.shopper),
        new ShoppingList(
            apiRequest.items,
            apiRequest.offer
        ), BaseRequest.RequestStatus.values()[apiRequest.status]
    );
  }
}
