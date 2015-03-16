package com.iic.shopingo.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.iic.shopingo.R;
import com.iic.shopingo.ui.HomeActivity;

/**
 * Created by ifeins on 3/14/15.
 */
public class GcmIntentService extends IntentService {

  private static final String SERVICE_NAME = GcmIntentService.class.getSimpleName();

  private static final int NOTIFICATION_ID = 1;

  private static final String LOG_TAG = GcmIntentService.class.getSimpleName();

  public GcmIntentService() {
    super(SERVICE_NAME);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.d(LOG_TAG, "Handling notification");
    Bundle extras = intent.getExtras();
    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
    String messageType = gcm.getMessageType(intent);

    if (messageType.equals(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE)) {
      sendNotification("Received: " + extras.toString());
    }
  }

  private void sendNotification(String msg) {
    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, HomeActivity.class), 0);

    Notification notification = new NotificationCompat.Builder(this).
        setSmallIcon(R.drawable.ic_action_accept)
        .setContentTitle("GCM notification")
        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
        .setContentText(msg)
        .setContentIntent(pendingIntent)
        .build();

    notificationManager.notify(NOTIFICATION_ID, notification);
  }
}
