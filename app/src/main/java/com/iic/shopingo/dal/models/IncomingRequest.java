package com.iic.shopingo.dal.models;

/**
 * Created by assafgelber on 3/8/15.
 */
public class IncomingRequest extends BaseRequest {
  private Contact requester;

  public IncomingRequest(Contact requester, ShoppingList shoppingList, RequestStatus status) {
    super(shoppingList, status);
    this.requester = requester;
  }

  public Contact getRequester() {
    return requester;
  }
}
