package com.iic.shopingo.dal.models;

import android.os.Parcel;
import android.os.Parcelable;
import butterknife.internal.ListenerClass;

/**
 * Created by ifeins on 3/3/15.
 */
public class UserInfo implements Parcelable {
  private static final String AVATAR_URL_FORMAT = "https://graph.facebook.com/%s/picture?width=300";

  private String uid;
  private String firstName;
  private String lastName;
  private String street;
  private String city;
  private String phoneNumber;

  public UserInfo(String uid, String firstName, String lastName, String street, String city, String phoneNumber) {
    this.uid = uid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.street = street;
    this.city = city;
    this.phoneNumber = phoneNumber;
  }

  public String getUid() {
    return uid;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAvatarUrl() {
    return String.format(AVATAR_URL_FORMAT, getUid());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(uid);
    dest.writeString(firstName);
    dest.writeString(lastName);
    dest.writeString(street);
    dest.writeString(city);
    dest.writeString(phoneNumber);
  }

  public UserInfo(Parcel in) {
    uid = in.readString();
    firstName = in.readString();
    lastName = in.readString();
    street = in.readString();
    city = in.readString();
    phoneNumber = in.readString();
  }

  public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
    @Override
    public UserInfo createFromParcel(Parcel source) {
      return new UserInfo(source);
    }

    @Override
    public UserInfo[] newArray(int size) {
      return new UserInfo[size];
    }
  };
}
