package com.iic.shopingo.services;

import android.content.SharedPreferences;
import com.iic.shopingo.dal.models.UserInfo;

/**
 * Created by asafg on 09/03/15.
 */
public class UserStorage {

  private static final String USER_INFO_UID_KEY = "user_info_uid";
  private static final String USER_INFO_FIRST_NAME_KEY = "user_info_first_name";
  private static final String USER_INFO_LAST_NAME_KEY = "user_info_last_name";
  private static final String USER_INFO_STREET_KEY = "user_info_street";
  private static final String USER_INFO_CITY_KEY = "user_info_city";
  private static final String USER_INFO_PHONE_KEY = "user_info_phone";
  private static final String USER_STATE_KEY = "user_state";

  private SharedPreferences sharedPreferences;

  public UserStorage(SharedPreferences prefs) {
    sharedPreferences = prefs;
  }

  public void storeUserInfo(UserInfo info) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    if (info != null) {
      editor.putString(USER_INFO_UID_KEY, info.getUid());
      editor.putString(USER_INFO_FIRST_NAME_KEY, info.getFirstName());
      editor.putString(USER_INFO_LAST_NAME_KEY, info.getLastName());
      editor.putString(USER_INFO_STREET_KEY, info.getStreet());
      editor.putString(USER_INFO_CITY_KEY, info.getCity());
      editor.putString(USER_INFO_PHONE_KEY, info.getPhoneNumber());
    } else {
      editor.remove(USER_INFO_UID_KEY);
      editor.remove(USER_INFO_FIRST_NAME_KEY);
      editor.remove(USER_INFO_LAST_NAME_KEY);
      editor.remove(USER_INFO_STREET_KEY);
      editor.remove(USER_INFO_CITY_KEY);
      editor.remove(USER_INFO_PHONE_KEY);
    }
    editor.apply();
  }

  public void storeUserState(CurrentUser.State state) {
    sharedPreferences.edit().putInt(USER_STATE_KEY, state.ordinal()).apply();
  }

  public UserInfo getUserInfo() {
    String uid = sharedPreferences.getString(USER_INFO_UID_KEY, null);
    if (uid != null) {
      String firstName = sharedPreferences.getString(USER_INFO_FIRST_NAME_KEY, null);
      String lastName = sharedPreferences.getString(USER_INFO_LAST_NAME_KEY, null);
      String street = sharedPreferences.getString(USER_INFO_STREET_KEY, null);
      String city = sharedPreferences.getString(USER_INFO_CITY_KEY, null);
      String phone = sharedPreferences.getString(USER_INFO_PHONE_KEY, null);
      return new UserInfo(uid, firstName, lastName, street, city, phone);
    } else {
      return null;
    }
  }

  public CurrentUser.State getUserState() {
    return CurrentUser.State.values()[sharedPreferences.getInt(USER_STATE_KEY, 0)];
  }
}
