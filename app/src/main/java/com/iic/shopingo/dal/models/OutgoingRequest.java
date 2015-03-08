package com.iic.shopingo.dal.models;

import android.os.Parcel;

/**
 * Created by assafgelber on 3/8/15.
 */
public class OutgoingRequest extends BaseRequest {
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
    super(in);
    this.shopper = in.readParcelable(Contact.class.getClassLoader());
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
    super.writeToParcel(dest, flags);
    dest.writeParcelable(this.shopper, 0);
  }
}
