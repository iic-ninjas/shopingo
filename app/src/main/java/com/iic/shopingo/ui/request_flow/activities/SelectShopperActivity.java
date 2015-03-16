package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Optional;
import com.iic.shopingo.R;
import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.request.GetNearbyShoppersCommand;
import com.iic.shopingo.api.request.ShoppersApiResult;
import com.iic.shopingo.api.trip.StartTripCommand;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.location.CurrentLocationProvider;
import com.iic.shopingo.services.location.LocationUpdatesListenerAdapter;
import com.iic.shopingo.ui.async.ApiTask;
import com.iic.shopingo.ui.request_flow.views.SelectShopperListItemView;
import com.iic.shopingo.ui.trip_flow.activities.ManageTripActivity;
import java.util.ArrayList;
import java.util.List;

public class SelectShopperActivity extends ActionBarActivity {
  private final long REQUEST_INTERVAL = 10 * 1000; // 10 seconds in milliseconds

  @InjectView(R.id.select_shopper_list)
  ListView shopperList;

  @InjectView(R.id.select_shopper_list_empty_state)
  LinearLayout emptyState;

  private SelectShopperAdapter adapter;

  private CurrentLocationProvider locationProvider;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_select_shopper);
    ButterKnife.inject(this);

    locationProvider = new CurrentLocationProvider(this, REQUEST_INTERVAL, new LocationUpdatesListenerAdapter() {
      @Override
      public void onLocationUpdated(Location location) {
        adapter.setUserLocation(location);
      }
    });

    shopperList.setEmptyView(emptyState);

    adapter = new SelectShopperAdapter();
    shopperList.setAdapter(adapter);
  }

  @OnItemClick(R.id.select_shopper_list)
  public void onListItemClick(int position) {
    Intent intent = new Intent(this, CreateShoppingListActivity.class);
    intent.putExtra(CreateShoppingListActivity.EXTRAS_SHOPPER_KEY, adapter.getItem(position));
    startActivity(intent);
  }

  @Optional
  @OnClick(R.id.select_shopper_list_go_yourself_button)
  public void onGoYourself(View view) {
    ApiTask<ApiResult> task = new ApiTask<>(getSupportFragmentManager(), "Starting trip...", new StartTripCommand(CurrentUser.getToken()));

    task.execute().continueWith(new Continuation<ApiResult, Void>() {
      @Override
      public Void then(Task<ApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          CurrentUser.getInstance().state = CurrentUser.State.TRIPPING;
          Intent intent = new Intent(SelectShopperActivity.this, ManageTripActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(intent);
          finish();
        } else {
          Toast.makeText(SelectShopperActivity.this, "Could not start trip: " + task.getError().getMessage(),
              Toast.LENGTH_LONG).show();
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

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
          adapter.setShoppers(task.getResult().shoppers);
        } else {
          // TODO: handle failure
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  public static class SelectShopperAdapter extends BaseAdapter {
    private List<Contact> shoppers;

    private Location userLocation;

    public SelectShopperAdapter() {
      this.shoppers = new ArrayList<>();
    }

    @Override
    public int getCount() {
      return shoppers.size();
    }

    @Override
    public Contact getItem(int position) {
      return shoppers.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      SelectShopperListItemView itemView = (SelectShopperListItemView) convertView;
      if (itemView == null) {
        itemView = SelectShopperListItemView.inflate(parent);
      }
      itemView.setShopper(getItem(position));
      if (userLocation != null) {
        itemView.setUserLocation(userLocation);
      }
      return itemView;
    }

    private void setUserLocation(Location userLocation) {
      this.userLocation = userLocation;
      notifyDataSetChanged();
    }

    public void setShoppers(List<Contact> shoppers) {
      this.shoppers = shoppers;
      notifyDataSetChanged();
    }
  }
}