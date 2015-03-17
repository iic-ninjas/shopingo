package com.iic.shopingo;

import android.app.Application;
import android.preference.PreferenceManager;
import com.iic.shopingo.services.ActivitiesLifecycleManager;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.NotificationsHelper;
import com.iic.shopingo.services.UserStorage;

public class ShopingoApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    CurrentUser.getInstance().setStorage(new UserStorage(PreferenceManager.getDefaultSharedPreferences(this)));
    if (CurrentUser.getToken() != null) {
      // TODO: needs to do this after login as well!!!!!
      NotificationsHelper.registerForNotificationsAsync(this);
    }

    registerActivityLifecycleCallbacks(ActivitiesLifecycleManager.getInstance());
  }
}
