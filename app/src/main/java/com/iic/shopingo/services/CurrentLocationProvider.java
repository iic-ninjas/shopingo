package com.iic.shopingo.services;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by assafgelber on 3/5/15.
 */
public class CurrentLocationProvider {
  private LocationManager locationManager;
  private LocationListener internalListener;
  private LocationUpdatesListener updatesListener;
  private long requestInterval;

  public CurrentLocationProvider(Context context, long requestInterval, LocationUpdatesListener listener) {
    updatesListener = listener;
    this.requestInterval = requestInterval;
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    internalListener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        updatesListener.onLocationUpdated(location);
      }

      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {}

      @Override
      public void onProviderEnabled(String provider) {}

      @Override
      public void onProviderDisabled(String provider) {}
    };

    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, requestInterval, 0, internalListener);
  }

  public void resume() {
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, requestInterval, 0, internalListener);
  }

  public void stop() {
    locationManager.removeUpdates(internalListener);
  }

  public interface LocationUpdatesListener {
    public void onLocationUpdated(Location location);
  }
}
