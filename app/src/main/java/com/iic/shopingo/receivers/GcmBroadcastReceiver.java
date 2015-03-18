package com.iic.shopingo.receivers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import com.iic.shopingo.services.GcmIntentService;

/**
 * Created by ifeins on 3/14/15.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

  private static final String LOG_TAG = GcmBroadcastReceiver.class.getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(LOG_TAG, "Received notification");
    ComponentName componentName = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
    startWakefulService(context, (intent.setComponent(componentName)));
    setResultCode(Activity.RESULT_OK);
  }
}
