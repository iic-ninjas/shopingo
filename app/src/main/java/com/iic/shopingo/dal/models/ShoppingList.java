package com.iic.shopingo.dal.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by assafgelber on 3/8/15.
 */
public class ShoppingList implements Parcelable {
  public static final Parcelable.Creator<ShoppingList> CREATOR = new Parcelable.Creator<ShoppingList>() {
    public ShoppingList createFromParcel(Parcel source) {
      return new ShoppingList(source);
    }

    public ShoppingList[] newArray(int size) {
      return new ShoppingList[size];
    }
  };

  private List<String> items;

  private int offer;

  public ShoppingList(List<String> items, int offer) {
    this.items = items;
    this.offer = offer;
  }

  private ShoppingList(Parcel in) {
    this.items = in.createStringArrayList();
    this.offer = in.readInt();
  }

  public List<String> getItems() {
    return items;
  }

  public int getOffer() {
    return offer;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeStringList(this.items);
    dest.writeInt(this.offer);
  }
}
