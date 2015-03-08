package com.iic.shopingo.ui.trip_flow.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 04/03/15.
 */
public class ShoppingList {
  public String requesterName;
  public String phoneNumber;

  public List<Item> items;

  public ShoppingList() {
    items = new ArrayList<>();
  }

  public static class Item {
    public boolean checked;
    public String title;

    public Item(String title) {
      this.title = title;
      this.checked = false;
    }

    public Item(String title, boolean checked) {
      this.title = title;
      this.checked = checked;
    }
  }
}