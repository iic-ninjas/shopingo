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

  private ShoppingList shoppingList;
  private RequestStatus status;

  public BaseRequest(ShoppingList shoppingList, RequestStatus status) {
    this.shoppingList = shoppingList;
    this.status = status;
  }

  public ShoppingList getShoppingList() {
    return shoppingList;
  }

  public RequestStatus getStatus() {
    return status;
  }
}
