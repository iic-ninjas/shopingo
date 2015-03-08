package com.iic.shopingo.dal.models;

/**
 * Created by assafgelber on 3/8/15.
 */
public class BaseRequest {
  public enum RequestStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    CANCELED
  }

  private Contact contact;
  private ShoppingList shoppingList;
  private RequestStatus status;

  public BaseRequest(Contact contact, ShoppingList shoppingList, RequestStatus status) {
    this.contact = contact;
    this.shoppingList = shoppingList;
    this.status = status;
  }

  public Contact getContact() {
    return contact;
  }

  public ShoppingList getShoppingList() {
    return shoppingList;
  }

  public RequestStatus getStatus() {
    return status;
  }
}
