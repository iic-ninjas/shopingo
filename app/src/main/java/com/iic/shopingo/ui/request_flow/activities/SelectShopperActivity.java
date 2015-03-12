package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.iic.shopingo.R;
import com.iic.shopingo.api.request.GetNearbyShoppers;
import com.iic.shopingo.api.request.ShoppersApiResult;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.location.CurrentLocationProvider;
import com.iic.shopingo.services.location.LocationUpdatesListenerAdapter;
import com.iic.shopingo.ui.ApiTask;
import com.iic.shopingo.ui.request_flow.views.SelectShopperListItemView;
import java.util.ArrayList;
import java.util.List;

public class SelectShopperActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {
  private final long REQUEST_INTERVAL = 10 * 1000; // 10 seconds in milliseconds
  public static final String EXTRAS_REQUEST_KEY = "request";

  @InjectView(R.id.select_shopper_list)
  ListView shopperList;

  @InjectView(R.id.select_shopper_swipe_container)
  SwipeRefreshLayout swipeLayout;

  private SelectShopperAdapter adapter;

  private CurrentLocationProvider locationProvider;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // TODO: add empty state and loading state to the listview
    setContentView(R.layout.activity_select_shopper);
    ButterKnife.inject(this);

    locationProvider = new CurrentLocationProvider(this, REQUEST_INTERVAL, new LocationUpdatesListenerAdapter() {
      @Override
      public void onLocationUpdated(Location location) {
        adapter.setUserLocation(location);
      }
    });

    adapter = new SelectShopperAdapter();
    shopperList.setAdapter(adapter);

    swipeLayout.setOnRefreshListener(this);
  }

  @OnItemClick(R.id.select_shopper_list)
  public void onListItemClick(int position) {
    Intent intent = new Intent(this, CreateShoppingListActivity.class);
    intent.putExtra(CreateShoppingListActivity.EXTRAS_SHOPPER_KEY, adapter.getItem(position));
    startActivity(intent);
  }

  @Override
  protected void onResume() {
    super.onResume();
    updateShoppers();
  }

  @Override
  public void onRefresh() {
    updateShoppers();
  }

  private void updateShoppers() {
    swipeLayout.setRefreshing(true);
    GetNearbyShoppers req = new GetNearbyShoppers(CurrentUser.getToken());
    req.executeAsync().continueWith(new Continuation<ShoppersApiResult, Object>() {
      @Override
      public Object then(Task<ShoppersApiResult> task) throws Exception {
        swipeLayout.setRefreshing(false);
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