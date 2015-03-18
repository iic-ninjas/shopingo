package com.iic.shopingo.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.GsonBuilder;
import com.iic.shopingo.R;
import com.iic.shopingo.api.models.converters.ContactConverter;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.dal.models.ShoppingList;
import com.iic.shopingo.events.AppEventBus;
import com.iic.shopingo.services.notifications.IncomingRequestNotification;
import com.iic.shopingo.services.notifications.OutgoingRequestNotification;
import com.iic.shopingo.services.notifications.ShopingoNotification;
import com.iic.shopingo.services.notifications.TripNotification;
import com.iic.shopingo.ui.HomeActivity;
import com.iic.shopingo.ui.request_flow.activities.SelectShopperActivity;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ifeins on 3/14/15.
 */
public class GcmIntentService extends IntentService {

  public static final String EXTRA_MESSAGE = "message";

  public static final String EXTRA_PAYLOAD = "payload";

  public static final String EXTRA_NOTIFICATION_TYPE = "notification_type";

  private static final String SERVICE_NAME = GcmIntentService.class.getSimpleName();

  private static final int NOTIFICATION_ID = 1;

  private static final String LOG_TAG = GcmIntentService.class.getSimpleName();

  private static Map<String, Class<? extends ShopingoNotification>> notificationTypeToClassMapping;

  static {
    notificationTypeToClassMapping = new HashMap<>();
    notificationTypeToClassMapping.put("incoming_request_notification", IncomingRequestNotification.class);
    notificationTypeToClassMapping.put("outgoing_request_notification", OutgoingRequestNotification.class);
    notificationTypeToClassMapping.put("trip_notification", TripNotification.class);
  }

  public GcmIntentService() {
    super(SERVICE_NAME);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.d(LOG_TAG, "Handling notification");
    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
    String messageType = gcm.getMessageType(intent);

    if (messageType.equals(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE)) {
      String payload = intent.getStringExtra(EXTRA_PAYLOAD);
      String notificationType = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE);
      ShopingoNotification notification =
          new GsonBuilder().create().fromJson(payload, notificationTypeToClassMapping.get(notificationType));
      handleNotification(intent.getStringExtra(EXTRA_MESSAGE), notification);
    }
  }

  private void handleNotification(String msg, ShopingoNotification shopingoNotification) {
    if (ActivitiesLifecycleManager.getInstance().isInForeground()) {
      Log.d(LOG_TAG, "Received GCM while app is in foreground");
      AppEventBus.getInstance().post(shopingoNotification);
    } else {
      Log.d(LOG_TAG, "Received GCM while app is in background");
      displayBackgroundNotification(shopingoNotification, msg);
    }
  }

  private void displayBackgroundNotification(ShopingoNotification shopingoNotification, String msg) {
    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    Notification notification = new NotificationCompat.Builder(this).
        setSmallIcon(R.drawable.ic_action_accept)
        .setContentTitle(getString(R.string.app_name))
        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
        .setContentText(msg)
        .setContentIntent(createPendingIntent(shopingoNotification))
        .setAutoCancel(true)
        .build();

    notificationManager.notify(NOTIFICATION_ID, notification);
  }

  private PendingIntent createPendingIntent(ShopingoNotification shopingoNotification) {
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

    PendingIntent pendingIntent = null;
    if (shopingoNotification instanceof TripNotification) {
      TripNotification tripNotification = (TripNotification) shopingoNotification;
      if (tripNotification.getStatus().equals("active")) {
        Intent resultIntent = new Intent(this, SelectShopperActivity.class);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
      }
    } else if (shopingoNotification instanceof IncomingRequestNotification) {
        // Home activity will "redirect" the user to correct screen
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, HomeActivity.class), 0);
    } else if (shopingoNotification instanceof OutgoingRequestNotification) {
      OutgoingRequestNotification requestNotification = (OutgoingRequestNotification) shopingoNotification;
      BaseRequest.RequestStatus status = BaseRequest.RequestStatus.valueOf(requestNotification.getStatus().toUpperCase());
      if (status == BaseRequest.RequestStatus.DECLINED) {
        // since the user state is idle we can't rely on the home activity to navigate the user to the declined state,
        // so we need to store an OutgoingRequest in the storage.
        Contact shopper = ContactConverter.convert(requestNotification.getShopper());
        ShoppingList shoppingList = new ShoppingList(requestNotification.getItems(), requestNotification.getOffer());
        OutgoingRequest outgoingRequest = new OutgoingRequest(requestNotification.getId(), shopper, shoppingList);
        new OutgoingRequestStorage(PreferenceManager.getDefaultSharedPreferences(this)).store(outgoingRequest);
      }
    }

    return pendingIntent;
  }
}
