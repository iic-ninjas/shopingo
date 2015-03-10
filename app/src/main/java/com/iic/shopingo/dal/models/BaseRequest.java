package com.iic.shopingo.dal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by assafgelber on 3/8/15.
 */
public abstract class BaseRequest implements Parcelable {

  protected String id;

  protected ShoppingList shoppingList;

  protected RequestStatus status;

  public BaseRequest(String id) {
    this.id = id;
    this.shoppingList = new ShoppingList();
  }

  public BaseRequest(String id, ShoppingList shoppingList) {
    this.id = id;
    this.shoppingList = shoppingList;
  }

  public BaseRequest(String id, ShoppingList shoppingList, RequestStatus status) {
    this.id = id;
    this.shoppingList = shoppingList;
    this.status = status;
  }

  protected BaseRequest(Parcel in) {
    this.id = in.readString();
    this.shoppingList = in.readParcelable(ShoppingList.class.getClassLoader());
    int tmpStatus = in.readInt();
    this.status = tmpStatus == -1 ? null : RequestStatus.values()[tmpStatus];
  }

  public String getId() {
    return id;
  }

  public ShoppingList getShoppingList() {
    return shoppingList;
  }

  public void setShoppingList(ShoppingList shoppingList) {
    this.shoppingList = shoppingList;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeParcelable(this.shoppingList, 0);
    dest.writeInt(this.status == null ? -1 : this.status.ordinal());
  }

  public enum RequestStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    SETTLED,
    CANCELED
  }
}
