package com.iic.shopingo.dal.models;

import java.util.List;

/**
 * Created by assafgelber on 3/8/15.
 */
public class ShoppingList {
  private List<String> items;
  private int offer;

  public ShoppingList(List<String> items, int offer) {
    this.items = items;
    this.offer = offer;
  }

  public List<String> getItems() {
    return items;
  }

  public int getOffer() {
    return offer;
  }
}
