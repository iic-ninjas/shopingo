package com.iic.shopingo.api.models.converters;

import com.iic.shopingo.api.models.ApiIncomingRequest;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.dal.models.ShoppingList;

/**
 * Created by assafgelber on 3/10/15.
 */
public class IncomingRequestConverter {
  public static IncomingRequest convert(ApiIncomingRequest apiRequest) {
    return new IncomingRequest(
        apiRequest.id,
        ContactConverter.convert(apiRequest.requester),
        new ShoppingList(
            apiRequest.items,
            apiRequest.offer
        ),
        BaseRequest.RequestStatus.values()[apiRequest.status]
    );
  }
}
