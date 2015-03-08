package com.iic.shopingo.ui.request_flow.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.iic.shopingo.R;
import com.iic.shopingo.services.location.CurrentLocationProvider;
import com.iic.shopingo.services.location.LocationUpdatesListenerAdapter;
import com.iic.shopingo.ui.request_flow.views.SelectShopperListItemView;
import java.util.ArrayList;
import java.util.List;

public class SelectShopperActivity extends ActionBarActivity {
  private final long REQUEST_INTERVAL = 10 * 1000; // 10 seconds in millisecnods

  @InjectView(R.id.select_shopper_list)
  ListView shopperList;

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

    List<SelectShopperAdapter.Shopper> shoppers = new ArrayList<>();
    adapter = new SelectShopperAdapter(shoppers);
    shopperList.setAdapter(adapter);
  }

  @OnItemClick(R.id.select_shopper_list)
  public void onListItemClick(int position) {
    // TODO: Pass the selected shopper to the request creation activity
  }

  public static class SelectShopperAdapter extends BaseAdapter {
    private List<Shopper> shoppers;

    private Location userLocation;

    public SelectShopperAdapter(List<Shopper> shoppers) {
      this.shoppers = shoppers;
    }

    @Override
    public int getCount() {
      return shoppers.size();
    }

    @Override
    public Shopper getItem(int position) {
      return shoppers.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      SelectShopperListItemView itemView = (SelectShopperListItemView)convertView;
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

    // TODO: Move actual model
    public static class Shopper {
      public String photo;
      public String name;
      public Double latitude;
      public Double longitude;

      public Shopper(String photo, String name, Double latitude, Double longitude) {
        this.photo = photo;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
      }
    }
  }
}