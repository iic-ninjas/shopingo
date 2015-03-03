package com.iic.shopingo.services;

import android.content.SharedPreferences;
import com.iic.shopingo.dal.models.User;

/**
 * Created by ifeins on 3/3/15.
 */
public class UserConnector {

  private static final String PREF_USER_ID_KEY = "pref_user_id";

  private User currentUser;

  private SharedPreferences sharedPreferences;

  public UserConnector(SharedPreferences sharedPreferences) {
    this.sharedPreferences = sharedPreferences;
  }

  public boolean isUserSignedIn() {
    return getCurrentUser() != null;
  }

  public User getCurrentUser() {
    if (currentUser == null) {
      int userId = sharedPreferences.getInt(PREF_USER_ID_KEY, -1);
      if (userId != -1) {
        currentUser = new User(userId, null);
      }
    }

    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(PREF_USER_ID_KEY, currentUser.getId());
    editor.apply();
  }
}
