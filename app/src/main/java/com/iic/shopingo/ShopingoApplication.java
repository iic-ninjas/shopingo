package com.iic.shopingo;

import android.app.Application;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import com.iic.shopingo.services.ActivitiesLifecycleManager;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.NotificationsHelper;
import com.iic.shopingo.services.UserStorage;

public class ShopingoApplication extends Application {

  public static final String MOCK_LOCATION_PROVIDER = "MOCK_LOCATION_PROVIDER";

  @Override
  public void onCreate() {
    super.onCreate();

    CurrentUser.getInstance().setStorage(new UserStorage(PreferenceManager.getDefaultSharedPreferences(this)));
    if (CurrentUser.getToken() != null) {
      NotificationsHelper.registerForNotificationsAsync(this);
    }

    setInitialLocation();
    registerActivityLifecycleCallbacks(ActivitiesLifecycleManager.getInstance());
  }

  private void setInitialLocation() {
    String latitude = getString(R.string.initial_latitude);
    String longitude = getString(R.string.initial_longitude);
    if (latitude != null && longitude != null) {
      LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      if (locationManager.getProvider(MOCK_LOCATION_PROVIDER) != null) {
        locationManager.removeTestProvider(MOCK_LOCATION_PROVIDER);
      }

      locationManager.addTestProvider(MOCK_LOCATION_PROVIDER, false, false, false, false, false, false, false,
          Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
      Location initialLocation = new Location(MOCK_LOCATION_PROVIDER);
      initialLocation.setLatitude(Double.parseDouble(latitude));
      initialLocation.setLongitude(Double.parseDouble(longitude));
      initialLocation.setAccuracy(5);
      initialLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
      initialLocation.setTime(System.currentTimeMillis());

      locationManager.setTestProviderEnabled(MOCK_LOCATION_PROVIDER, true);
      locationManager.setTestProviderStatus(MOCK_LOCATION_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
      locationManager.setTestProviderLocation(MOCK_LOCATION_PROVIDER, initialLocation);
    }
  }
}
