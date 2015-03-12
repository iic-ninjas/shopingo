package com.iic.shopingo.services;

import android.content.SharedPreferences;
import com.iic.shopingo.dal.models.UserInfo;

/**
 * Created by asafg on 09/03/15.
 */
public class CurrentUser {

  public enum State {
    LOGGED_OUT,
    IDLE,
    TRIPPING,
    REQUESTING
  }

  public UserInfo userInfo;
  public State state;

  private UserStorage storage;
  private static CurrentUser instance;

  private CurrentUser() {
    state = State.LOGGED_OUT;
  }

  public static CurrentUser getInstance() {
    if (instance == null) {
      instance = new CurrentUser();
    }
    return instance;
  }

  public static String getToken() {
    UserInfo info = getInstance().userInfo;
    if (info != null) {
      return info.getUid();
    } else {
      return null;
    }
  }

  public void setStorage(UserStorage storage) {
    this.storage = storage;
    load();
  }

  public void load() {
    userInfo = storage.getUserInfo();
    state = storage.getUserState();
  }

  public void save() {
    storage.storeUserInfo(userInfo);
    storage.storeUserState(state);
  }

  public void logout() {
    state = CurrentUser.State.LOGGED_OUT;
    userInfo = null;
    save();
  }
}
