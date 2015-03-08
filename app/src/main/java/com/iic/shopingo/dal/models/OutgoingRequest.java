package com.iic.shopingo.dal.models;

import android.os.Parcel;

/**
 * Created by assafgelber on 3/8/15.
 */
public class OutgoingRequest extends BaseRequest implements android.os.Parcelable {
  public static final Creator<OutgoingRequest> CREATOR = new Creator<OutgoingRequest>() {
    public OutgoingRequest createFromParcel(Parcel source) {
      return new OutgoingRequest(source);
    }

    public OutgoingRequest[] newArray(int size) {
      return new OutgoingRequest[size];
    }
  };

  private Contact shopper;

  public OutgoingRequest(Contact shopper, ShoppingList shoppingList, RequestStatus status) {
    super(shoppingList, status);
    this.shopper = shopper;
  }

  private OutgoingRequest(Parcel in) {
    this.shopper = in.readParcelable(Contact.class.getClassLoader());
    this.shoppingList = in.readParcelable(ShoppingList.class.getClassLoader());
    int tmpStatus = in.readInt();
    this.status = tmpStatus == -1 ? null : RequestStatus.values()[tmpStatus];
  }

  public Contact getShopper() {
    return shopper;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.shopper, 0);
    dest.writeParcelable(this.shoppingList, 0);
    dest.writeInt(this.status == null ? -1 : this.status.ordinal());
  }
}
