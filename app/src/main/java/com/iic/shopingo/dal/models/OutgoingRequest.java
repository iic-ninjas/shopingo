package com.iic.shopingo.dal.models;

/**
 * Created by assafgelber on 3/8/15.
 */
public class OutgoingRequest extends BaseRequest {
  private Contact shopper;

  public OutgoingRequest(Contact shopper, ShoppingList shoppingList, RequestStatus status) {
    super(shoppingList, status);
    this.shopper = shopper;
  }

  public Contact getShopper() {
    return shopper;
  }
}
