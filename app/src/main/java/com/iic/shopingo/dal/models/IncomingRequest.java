package com.iic.shopingo.dal.models;

import android.os.Parcel;

/**
 * Created by assafgelber on 3/8/15.
 */
public class IncomingRequest extends BaseRequest implements android.os.Parcelable {
  public static final Creator<IncomingRequest> CREATOR = new Creator<IncomingRequest>() {
    public IncomingRequest createFromParcel(Parcel source) {
      return new IncomingRequest(source);
    }

    public IncomingRequest[] newArray(int size) {
      return new IncomingRequest[size];
    }
  };

  private Contact requester;

  public IncomingRequest(Contact requester, ShoppingList shoppingList, RequestStatus status) {
    super(shoppingList, status);
    this.requester = requester;
  }

  private IncomingRequest(Parcel in) {
    this.requester = in.readParcelable(Contact.class.getClassLoader());
    this.shoppingList = in.readParcelable(ShoppingList.class.getClassLoader());
    int tmpStatus = in.readInt();
    this.status = tmpStatus == -1 ? null : RequestStatus.values()[tmpStatus];
  }

  public Contact getRequester() {
    return requester;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.requester, 0);
    dest.writeParcelable(this.shoppingList, 0);
    dest.writeInt(this.status == null ? -1 : this.status.ordinal());
  }
}
