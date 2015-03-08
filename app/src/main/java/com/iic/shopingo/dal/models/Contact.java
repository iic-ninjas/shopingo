package com.iic.shopingo.dal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by assafgelber on 3/8/15.
 */
public class Contact implements Parcelable {
  public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
    public Contact createFromParcel(Parcel source) {
      return new Contact(source);
    }

    public Contact[] newArray(int size) {
      return new Contact[size];
    }
  };

  private String firstName;

  private String lastName;

  private String avatar;

  private String phoneNumber;

  private String streetAddress;

  private String city;

  private double latitiude;

  private double longitude;

  public Contact(String firstName, String lastName, String avatar, String phoneNumber, String streetAddress,
      String city, double latitiude, double longitude) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatar = avatar;
    this.phoneNumber = phoneNumber;
    this.streetAddress = streetAddress;
    this.city = city;
    this.latitiude = latitiude;
    this.longitude = longitude;
  }

  private Contact(Parcel in) {
    this.firstName = in.readString();
    this.lastName = in.readString();
    this.avatar = in.readString();
    this.phoneNumber = in.readString();
    this.streetAddress = in.readString();
    this.city = in.readString();
    this.latitiude = in.readDouble();
    this.longitude = in.readDouble();
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getName() {
    return this.firstName + " " + this.lastName;
  }

  public String getAvatar() {
    return avatar;
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

  public double getLatitiude() {
    return latitiude;
  }

  public double getLongitude() {
    return longitude;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.firstName);
    dest.writeString(this.lastName);
    dest.writeString(this.avatar);
    dest.writeString(this.phoneNumber);
    dest.writeString(this.streetAddress);
    dest.writeString(this.city);
    dest.writeDouble(this.latitiude);
    dest.writeDouble(this.longitude);
  }
}
