package com.iic.shopingo.dal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by assafgelber on 3/8/15.
 */
public class Contact implements Parcelable {
  private static final String AVATAR_URL_FORMAT = "https://graph.facebook.com/%s/picture?width=300";

  public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
    public Contact createFromParcel(Parcel source) {
      return new Contact(source);
    }

    public Contact[] newArray(int size) {
      return new Contact[size];
    }
  };

  private String id;

  private String firstName;

  private String lastName;

  private String phoneNumber;

  private String streetAddress;

  private String city;

  private double latitude;

  private double longitude;

  public Contact(String id, String firstName, String lastName, String phoneNumber, String streetAddress,
      String city, double latitude, double longitude) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.streetAddress = streetAddress;
    this.city = city;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  private Contact(Parcel in) {
    this.id = in.readString();
    this.firstName = in.readString();
    this.lastName = in.readString();
    this.phoneNumber = in.readString();
    this.streetAddress = in.readString();
    this.city = in.readString();
    this.latitude = in.readDouble();
    this.longitude = in.readDouble();
  }

  public String getId() { return id; }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getName() {
    return this.firstName + " " + this.lastName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public String getCity() {
    return city;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public String getAvatarUrl() {
    return String.format(AVATAR_URL_FORMAT, getId());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.firstName);
    dest.writeString(this.lastName);
    dest.writeString(this.phoneNumber);
    dest.writeString(this.streetAddress);
    dest.writeString(this.city);
    dest.writeDouble(this.latitude);
    dest.writeDouble(this.longitude);
  }
}
