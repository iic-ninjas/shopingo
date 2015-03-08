package com.iic.shopingo.services.location;

import android.location.Location;

/**
 * Created by assafgelber on 3/8/15.
 */
public class LocationUpdatesListenerAdapter implements CurrentLocationProvider.LocationUpdatesListener {
  @Override
  public void onLocationUpdated(Location location) {}

  @Override
  public void onConnectionStop() {}

  @Override
  public void onConnectionFail() {}
}
