package com.iic.shopingo.dal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by assafgelber on 3/8/15.
 */
public abstract class BaseRequest implements Parcelable {

  protected ShoppingList shoppingList;

  protected RequestStatus status;

  public BaseRequest(ShoppingList shoppingList, RequestStatus status) {
    this.shoppingList = shoppingList;
    this.status = status;
  }

  protected BaseRequest(Parcel in) {
    this.shoppingList = in.readParcelable(ShoppingList.class.getClassLoader());
    int tmpStatus = in.readInt();
    this.status = tmpStatus == -1 ? null : RequestStatus.values()[tmpStatus];
  }

  public ShoppingList getShoppingList() {
    return shoppingList;
  }

  public RequestStatus getStatus() {
    return status;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
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
