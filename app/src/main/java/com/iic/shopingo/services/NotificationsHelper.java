package com.iic.shopingo.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import bolts.Task;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.iic.shopingo.R;
import com.iic.shopingo.api.device.RegisterDeviceCommand;
import java.util.concurrent.Callable;

/**
 * Created by ifeins on 3/14/15.
 */
public class NotificationsHelper {

  private static final String LOG_TAG = NotificationsHelper.class.getSimpleName();

  public static Task<String> registerForNotificationsAsync(final Context context) {
    return Task.callInBackground(new Callable<String>() {
      @Override
      public String call() throws Exception {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String senderId = context.getString(R.string.google_project_number);
        String gcmRegId = gcm.register(senderId);

        new RegisterDeviceCommand(CurrentUser.getToken(), gcmRegId).executeSync();
        return gcmRegId;
      }
    });
  }

}
