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
import java.util.concurrent.Callable;

/**
 * Created by ifeins on 3/14/15.
 */
public class NotificationsHelper {

  private static final String PREF_GCM_REG_ID = "GCM_REG_ID";

  private static final String PREF_APP_VERSION = "APP_VERSION";

  private static final String LOG_TAG = NotificationsHelper.class.getSimpleName();

  public static boolean isPlayServicesSupported(Context context) {
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
    return resultCode == ConnectionResult.SUCCESS;
  }

  public static String getGcmRegistrationId(Context context) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences(NotificationsHelper.class.getSimpleName(), Context.MODE_PRIVATE);
    String gcmRegId = sharedPreferences.getString(PREF_GCM_REG_ID, null);
    if (gcmRegId == null) {
      return null;
    }

    // If app was updated then we need a new registration id
    int cachedVersion = sharedPreferences.getInt(PREF_APP_VERSION, -1);
    int currentVersion = getAppVersion(context);
    if (currentVersion != cachedVersion) {
      Log.d(LOG_TAG, "App was updated");
      return null;
    }

    return gcmRegId;
  }

  public static Task<String> registerForNotificationsAsync(final Context context) {
    return Task.callInBackground(new Callable<String>() {
      @Override
      public String call() throws Exception {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String senderId = context.getString(R.string.google_project_number);
        String gcmRegId = gcm.register(senderId);

        sendToServer(gcmRegId);
        return gcmRegId;
      }
    });
  }

  private static void sendToServer(String gcmRegId) {
    // TODO: implement method
  }

  private static int getAppVersion(Context context) {
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
