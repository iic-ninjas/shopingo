package com.iic.shopingo.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.iic.shopingo.R;
import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.request.GetNearbyShoppersCommand;
import com.iic.shopingo.api.request.ShoppersApiResult;
import com.iic.shopingo.api.trip.StartTripCommand;
import com.iic.shopingo.api.user.CurrentStateApiResult;
import com.iic.shopingo.api.user.GetCurrentStateCommand;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.FacebookConnector;
import com.iic.shopingo.services.location.CurrentLocationProvider;
import com.iic.shopingo.services.location.LocationUpdatesListenerAdapter;
import com.iic.shopingo.ui.home.MapManager;
import com.iic.shopingo.ui.onboarding.activities.OnboardingActivity;
import com.iic.shopingo.ui.request_flow.activities.CreateShoppingListActivity;
import com.iic.shopingo.ui.request_flow.activities.RequestStateActivity;
import com.iic.shopingo.ui.trip_flow.activities.ManageTripActivity;
import java.util.ArrayList;

public class HomeActivity extends ActionBarActivity implements GoogleMap.OnMarkerClickListener {

  private final long REQUEST_INTERVAL = 10 * 1000; // 10 seconds in milliseconds

  private MapManager mapManager;

  @InjectView(R.id.home_content_container)
  View contentContainer;

  @InjectView(R.id.home_spinner_container)
  View spinnerContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init();
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (CurrentUser.getInstance().state == CurrentUser.State.LOGGED_OUT) {
      navigateToOnboarding();
    } else {
      showSpinner();

      new GetCurrentStateCommand(CurrentUser.getToken()).executeAsync().continueWith(new Continuation<CurrentStateApiResult, Void>() {
        @Override
        public Void then(Task<CurrentStateApiResult> task) throws Exception {
          if (!task.isFaulted() && !task.isCancelled()) {
            switch (task.getResult().userState) {
              case LOGGED_OUT: {
                Log.e("HomeActivity", "Server should not report user as logged out");
                finish();
              } break;
              case IDLE: {
                // TODO: a user might still have a declined request that he never saw. redirect to the declined request here.
                showContent();
              } break;
              case TRIPPING: {
                CurrentUser.getInstance().state = CurrentUser.State.TRIPPING;
                Intent intent = new Intent(HomeActivity.this, ManageTripActivity.class);
                ArrayList<IncomingRequest> list = new ArrayList<>(task.getResult().activeTripRequests.size());
                list.addAll(task.getResult().activeTripRequests);
                intent.putParcelableArrayListExtra(ManageTripActivity.EXTRA_REQUESTS, list);
                startActivity(intent);
              } break;
              case REQUESTING: {
                CurrentUser.getInstance().state = CurrentUser.State.REQUESTING;
                Intent intent = new Intent(HomeActivity.this, RequestStateActivity.class);
                intent.putExtra(RequestStateActivity.EXTRAS_REQUEST_KEY, task.getResult().activeOutgoingRequest);
                startActivity(intent);
              } break;
            }
          } else {
            Log.e("HomeActivity", "Can't contact server", task.getError());
            finish();
          }
          return null;
        }
      }, Task.UI_THREAD_EXECUTOR);
    }
  }

  private void init() {
    setContentView(R.layout.activity_home);
    ButterKnife.inject(this);

    GoogleMap map =  ((MapFragment) getFragmentManager().findFragmentById(R.id.select_shopper_map)).getMap();

    map.setOnMarkerClickListener(this);

    new CurrentLocationProvider(this, REQUEST_INTERVAL, new LocationUpdatesListenerAdapter() {
      @Override
      public void onLocationUpdated(Location location) {
        mapManager.setUserLocation(location);
      }
    });

    mapManager = new MapManager(this, map);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case R.id.action_logout:
        FacebookConnector.logout(this);
        CurrentUser.getInstance().logout();
        navigateToOnboarding();
        break;
      case R.id.action_update_contact:
        Intent intent = new Intent(this, ContactDetailsActivity.class);
        startActivity(intent);
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.select_shopper_go_yourself_button)
  public void onGoYourself(View view) {
    ApiTask<ApiResult> task = new ApiTask<>(getSupportFragmentManager(), "Starting trip...", new StartTripCommand(CurrentUser.getToken()));

    task.execute().continueWith(new Continuation<ApiResult, Void>() {
      @Override
      public Void then(Task<ApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          CurrentUser.getInstance().state = CurrentUser.State.TRIPPING;
          Intent intent = new Intent(HomeActivity.this, ManageTripActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(intent);
        } else {
          Toast.makeText(HomeActivity.this, "Could not start trip: " + task.getError().getMessage(),
              Toast.LENGTH_LONG).show();
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  private void updateShoppers() {
    GetNearbyShoppersCommand req = new GetNearbyShoppersCommand(CurrentUser.getToken());
    req.executeAsync().continueWith(new Continuation<ShoppersApiResult, Object>() {
      @Override
      public Object then(Task<ShoppersApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          mapManager.setShoppers(task.getResult().shoppers);
        } else {
          Toast.makeText(HomeActivity.this, "Failed to fetch shoppers", Toast.LENGTH_SHORT).show();
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  private void navigateToOnboarding() {
    Intent intent = new Intent(this, OnboardingActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
  }

  private void showSpinner() {
    contentContainer.setVisibility(View.GONE);
    spinnerContainer.setVisibility(View.VISIBLE);
  }

  private void showContent() {
    spinnerContainer.setVisibility(View.GONE);
    contentContainer.setVisibility(View.VISIBLE);
    updateShoppers();
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    Intent intent = new Intent(this, CreateShoppingListActivity.class);
    intent.putExtra(CreateShoppingListActivity.EXTRAS_SHOPPER_KEY, mapManager.contactFromMarker(marker));
    startActivity(intent);
    return true;
  }
}
