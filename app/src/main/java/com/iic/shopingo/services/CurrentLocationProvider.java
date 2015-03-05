package com.iic.shopingo.services;

import android.location.Location;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by assafgelber on 3/5/15.
 */
public class CurrentLocationProvider {
  private GoogleApiClient googleApiClient;
  private LocationListener internalListener;
  private LocationUpdatesListener updatesListener;

  public CurrentLocationProvider(GoogleApiClient client, long requestInterval, LocationUpdatesListener listener) {
    this.googleApiClient = client;
    this.updatesListener = listener;

    LocationRequest locationRequest = new LocationRequest();
    locationRequest.setInterval(requestInterval);
    locationRequest.setFastestInterval(requestInterval);
    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    internalListener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        updatesListener.onLocationUpdated(location);
      }
    };

    LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, internalListener);
  }

  public void stop() {
    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, internalListener);
  }

  public interface LocationUpdatesListener {
    public void onLocationUpdated(Location location);
  }
}
