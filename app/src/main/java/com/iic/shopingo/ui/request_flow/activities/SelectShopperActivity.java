package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iic.shopingo.R;
import com.iic.shopingo.api.request.GetNearbyShoppersCommand;
import com.iic.shopingo.api.request.ShoppersApiResult;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.location.CurrentLocationProvider;
import com.iic.shopingo.services.location.LocationUpdatesListenerAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectShopperActivity extends ActionBarActivity implements GoogleMap.OnMarkerClickListener {
  private final long REQUEST_INTERVAL = 10 * 1000; // 10 seconds in milliseconds

  private SelectShopperMapManager mapManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_select_shopper);
    ButterKnife.inject(this);

    GoogleMap map =  ((MapFragment) getFragmentManager().findFragmentById(R.id.select_shopper_map)).getMap();

    map.setOnMarkerClickListener(this);

    new CurrentLocationProvider(this, REQUEST_INTERVAL, new LocationUpdatesListenerAdapter() {
      @Override
      public void onLocationUpdated(Location location) {
        mapManager.setUserLocation(location);
      }
    });

    mapManager = new SelectShopperMapManager(map);
  }

  //@Optional
  //@OnClick(R.id.select_shopper_list_go_yourself_button)
  //public void onGoYourself(View view) {
  //  ApiTask<ApiResult> task = new ApiTask<>(getSupportFragmentManager(), "Starting trip...", new StartTripCommand(CurrentUser.getToken()));
  //
  //  task.execute().continueWith(new Continuation<ApiResult, Void>() {
  //    @Override
  //    public Void then(Task<ApiResult> task) throws Exception {
  //      if (!task.isFaulted() && !task.isCancelled()) {
  //        CurrentUser.getInstance().state = CurrentUser.State.TRIPPING;
  //        Intent intent = new Intent(SelectShopperActivity.this, ManageTripActivity.class);
  //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
  //        startActivity(intent);
  //        finish();
  //      } else {
  //        Toast.makeText(SelectShopperActivity.this, "Could not start trip: " + task.getError().getMessage(),
  //            Toast.LENGTH_LONG).show();
  //      }
  //      return null;
  //    }
  //  }, Task.UI_THREAD_EXECUTOR);
  //}

  @Override
  protected void onResume() {
    super.onResume();
    updateShoppers();
  }

  private void updateShoppers() {
    GetNearbyShoppersCommand req = new GetNearbyShoppersCommand(CurrentUser.getToken());
    req.executeAsync().continueWith(new Continuation<ShoppersApiResult, Object>() {
      @Override
      public Object then(Task<ShoppersApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          mapManager.setShoppers(task.getResult().shoppers);
        } else {
          // TODO: handle failure
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    Intent intent = new Intent(this, CreateShoppingListActivity.class);
    intent.putExtra(CreateShoppingListActivity.EXTRAS_SHOPPER_KEY, mapManager.contactFromMarker(marker));
    startActivity(intent);
    return true;
  }

  public static class SelectShopperMapManager {
    private List<Contact> shoppers;

    private Location userLocation;

    private GoogleMap map;

    private HashMap<Marker, Contact> markerToShopper = new HashMap<>();

    public SelectShopperMapManager(GoogleMap map) {
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

    private void addMarker(Contact shopper) {
      LatLng shopperLatLng = new LatLng(shopper.getLatitude(), shopper.getLongitude());
      MarkerOptions markerOptions = new MarkerOptions().position(shopperLatLng)
          .title(shopper.getName())
          //.icon(BitmapDescriptorFactory.fromPath(shopper.getAvatarUrl()));
          .icon(BitmapDescriptorFactory.defaultMarker());
      Marker marker = map.addMarker(markerOptions);
      markerToShopper.put(marker, shopper);
    }
  }
}