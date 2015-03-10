package com.iic.shopingo.services;

import android.content.SharedPreferences;
import com.iic.shopingo.dal.models.UserInfo;

/**
 * Created by asafg on 09/03/15.
 */
public class CurrentUser {

  public enum State {
    LOGGED_OUT,
    LOGGED_IN,
    SHOPPING,
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

  public void load(SharedPreferences prefs) {
    UserStorage storage = new UserStorage(prefs);
    userInfo = storage.getUserInfo();
    state = storage.getUserState();
  }

  public void save(SharedPreferences prefs) {
    UserStorage storage = new UserStorage(prefs);
    storage.storeUserInfo(userInfo);
    storage.storeUserState(state);
  }
}
