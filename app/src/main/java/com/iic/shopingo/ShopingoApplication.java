package com.iic.shopingo;

import android.app.Application;
import android.preference.PreferenceManager;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.UserStorage;

/**
 * Created by ifeins on 3/3/15.
 */
public class ShopingoApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    CurrentUser.getInstance().setStorage(new UserStorage(PreferenceManager.getDefaultSharedPreferences(this)));
  }
}
