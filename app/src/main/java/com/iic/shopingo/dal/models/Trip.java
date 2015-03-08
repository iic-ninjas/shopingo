package com.iic.shopingo.dal.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by assafgelber on 3/8/15.
 */
public class Trip implements Parcelable {
  public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
    public Trip createFromParcel(Parcel source) {
      return new Trip(source);
    }

    public Trip[] newArray(int size) {
      return new Trip[size];
    }
  };

  private List<IncomingRequest> requests;

  public Trip(List<IncomingRequest> requests) {
    this.requests = requests;
  }

  private Trip(Parcel in) {
    in.readTypedList(requests, IncomingRequest.CREATOR);
  }

  public List<IncomingRequest> getRequests() {
    return requests;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(requests);
  }
}
