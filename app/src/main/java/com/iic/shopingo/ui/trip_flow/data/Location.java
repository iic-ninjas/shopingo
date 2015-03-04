package com.iic.shopingo.ui.trip_flow.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asafg on 03/03/15.
 */
public class Location implements Parcelable {
  public android.location.Location coords;
  public String country;
  public String city;
  public String address;
  public String zipcode;
  public String phone;

  public Location() {
    coords = new android.location.Location("location");
  }

  public String toAddressString() {
    return address + ", " + city;
  }

  public Location(Parcel source) {
    coords = source.readParcelable(android.location.Location.class.getClassLoader());
    country = source.readString();
    city = source.readString();
    address = source.readString();
    zipcode = source.readString();
    phone = source.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(coords, 0);
    dest.writeString(country);
    dest.writeString(city);
    dest.writeString(address);
    dest.writeString(zipcode);
    dest.writeString(phone);
  }


  public final static Creator<Location> CREATOR = new Creator<Location>() {
    @Override
    public Location createFromParcel(Parcel source) {
      return new Location(source);
    }

    @Override
    public Location[] newArray(int size) {
      return new Location[size];
    }
  };
}
