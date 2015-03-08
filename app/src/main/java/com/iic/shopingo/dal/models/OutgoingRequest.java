package com.iic.shopingo.dal.models;

/**
 * Created by assafgelber on 3/8/15.
 */
public class OutgoingRequest extends BaseRequest {

  public OutgoingRequest(Contact contact, ShoppingList shoppingList, RequestStatus status) {
    super(contact, shoppingList, status);
  }
}
