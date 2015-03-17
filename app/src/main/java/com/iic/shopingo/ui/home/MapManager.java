package com.iic.shopingo.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iic.shopingo.R;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.utils.BitmapCircler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by assafgelber on 3/17/15.
 */
public class MapManager {
  private List<Contact> shoppers;

  private Location userLocation;

  private GoogleMap map;

  private Context context;

  private List<Target> picassoTargets = new ArrayList<>();

  private HashMap<Marker, Contact> markerToShopper = new HashMap<>();

  public MapManager(Context context, GoogleMap map) {
    this.context = context;
    this.map = map;
    this.shoppers = new ArrayList<>();
  }

  public void setUserLocation(Location userLocation) {
    this.userLocation = userLocation;
    updateMapCenter();
  }

  public void setShoppers(List<Contact> shoppers) {
    this.shoppers = shoppers;
    resetMarkers();
  }

  public Contact contactFromMarker(Marker marker) {
    return markerToShopper.get(marker);
  }

  private void updateMapCenter() {
    LatLng userLatLng = new LatLng(this.userLocation.getLatitude(), this.userLocation.getLongitude());
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15.0f));
  }

  private void resetMarkers() {
    map.clear();
    for (Contact shopper : this.shoppers) {
      addMarker(shopper);
    }
  }

  private void addMarker(final Contact shopper) {
    Target target = new Target() {
      private Marker placeholderMarker;

      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Bitmap circledBitmap = BitmapCircler.circle(bitmap);
        LatLng shopperLatLng = new LatLng(shopper.getLatitude(), shopper.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(shopperLatLng)
            .title(shopper.getName())
            .icon(BitmapDescriptorFactory.fromBitmap(circledBitmap));
        Marker marker = map.addMarker(markerOptions);
        markerToShopper.put(marker, shopper);
        picassoTargets.remove(this);
        if (placeholderMarker != null) {
          placeholderMarker.remove();
          markerToShopper.remove(placeholderMarker);
        }
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {
        picassoTargets.remove(this);
      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {
        LatLng shopperLatLng = new LatLng(shopper.getLatitude(), shopper.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(shopperLatLng)
            .title(shopper.getName())
            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        placeholderMarker = map.addMarker(markerOptions);
        markerToShopper.put(placeholderMarker, shopper);
      }
    };

    picassoTargets.add(target);
    Picasso.with(context)
        .load(shopper.getAvatarUrl())
        .resize(100, 100)
        .centerCrop()
        .into(target);
  }
}