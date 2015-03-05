package com.iic.shopingo.services;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by assafgelber on 3/5/15.
 */
public class CurrentLocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
  private GoogleApiClient googleApiClient;
  private LocationListener internalListener;
  private LocationUpdatesListener updatesListener;
  private long requestInterval;

  public CurrentLocationProvider(Context context, long requestInterval, LocationUpdatesListener listener) {
    this.requestInterval = requestInterval;
    this.updatesListener = listener;

    googleApiClient = new GoogleApiClient.Builder(context)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .build();
    googleApiClient.connect();
  }

  @Override
  public void onConnected(Bundle bundle) {
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

    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, internalListener);
  }

  @Override
  public void onConnectionSuspended(int i) {
    stop();
    updatesListener.onConnectionStop();
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    updatesListener.onConnectionFail();
  }

  public void stop() {
    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, internalListener);
  }

  public interface LocationUpdatesListener {
    public void onLocationUpdated(Location location);
    public void onConnectionStop();
    public void onConnectionFail();
  }
}
