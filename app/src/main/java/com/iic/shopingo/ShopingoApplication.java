package com.iic.shopingo;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.iic.shopingo.services.SharedUserConnector;
import com.iic.shopingo.services.UserConnector;

/**
 * Created by ifeins on 3/3/15.
 */
public class ShopingoApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    SharedUserConnector.setInstance(new UserConnector(sharedPreferences));
  }
}
