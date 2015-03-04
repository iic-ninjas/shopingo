package com.iic.shopingo.ui.trip_flow.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 03/03/15.
 */
public class Request implements Parcelable {
  public String photoUrl;
  public String name;
  public int offerInCents;
  public Location location;
  public List<String> items;


  public Request() {
    location = new Location();
    items = new ArrayList<>();
  }

  public Request(Parcel source) {
    photoUrl = source.readString();
    name = source.readString();
    offerInCents = source.readInt();
    location = source.readParcelable(Location.class.getClassLoader());
    items = new ArrayList<>();
    source.readStringList(items);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(photoUrl);
    dest.writeString(name);
    dest.writeInt(offerInCents);
    dest.writeParcelable(location, 0);
    dest.writeStringList(items);
  }

  public static final Creator<Request> CREATOR = new Creator<Request>() {
    @Override
    public Request createFromParcel(Parcel source) {
      return new Request(source);
    }

    @Override
    public Request[] newArray(int size) {
      return new Request[size];
    }
  };
}
