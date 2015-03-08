package com.iic.shopingo.ui.request_flow.activities;

import android.location.Location;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
  private final long REQUEST_INTERVAL = 10 * 1000; // 10 seconds in milliseconds
  public static final String EXTRAS_REQUEST_KEY = "request";

  @InjectView(R.id.select_shopper_list)
  ListView shopperList;

  private SelectShopperAdapter.ShopRequest request;

  private SelectShopperAdapter adapter;

  private CurrentLocationProvider locationProvider;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    request = getIntent().getParcelableExtra(EXTRAS_REQUEST_KEY);
    if (request == null) {
      request = new SelectShopperAdapter.ShopRequest();
    }

    setContentView(R.layout.activity_select_shopper);
    ButterKnife.inject(this);

    locationProvider = new CurrentLocationProvider(this, REQUEST_INTERVAL, new LocationUpdatesListenerAdapter() {
      @Override
      public void onLocationUpdated(Location location) {
        adapter.setUserLocation(location);
      }
    });

    List<SelectShopperAdapter.Shopper> shoppers = new ArrayList<>();
    shoppers.add(new SelectShopperAdapter.Shopper("http://agelber.com/images/avatar-large.png", "Assaf Gelber", 32.0613776, 34.7692314));
    // TODO: Get actual shoppers
    adapter = new SelectShopperAdapter(shoppers);
    shopperList.setAdapter(adapter);
  }

  @OnItemClick(R.id.select_shopper_list)
  public void onListItemClick(int position) {
    Intent intent = new Intent(this, CreateRequestActivity.class);
    request.shopper = adapter.getItem(position);
    intent.putExtra(CreateRequestActivity.EXTRAS_REQUEST_KEY, request);
    startActivity(intent);
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

    // TODO: Move to actual model
    public static class ShopRequest implements Parcelable {
      public final static Creator<ShopRequest> CREATOR = new Creator<ShopRequest>() {
        @Override
        public ShopRequest createFromParcel(Parcel source) {
          return new ShopRequest(source);
        }

        @Override
        public ShopRequest[] newArray(int size) {
          return new ShopRequest[size];
        }
      };

      public SelectShopperActivity.SelectShopperAdapter.Shopper shopper;
      public List<String> items = new ArrayList<>();
      public int price;
      public RequestStatus status = RequestStatus.PENDING;

      public ShopRequest() {
      }

      public ShopRequest(Parcel source) {
        shopper = source.readParcelable(SelectShopperActivity.SelectShopperAdapter.Shopper.class.getClassLoader());
        items = source.createStringArrayList();
        price = source.readInt();
        status = RequestStatus.values()[source.readInt()];
      }

      @Override
      public int describeContents() {
        return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(shopper, 0);
        dest.writeStringList(items);
        dest.writeInt(price);
        dest.writeInt(status.ordinal());
      }

      public static enum RequestStatus {PENDING, APPROVED, DECLINED}
    }

    // TODO: Move actual model
    public static class Shopper implements Parcelable {
      public final static Creator<Shopper> CREATOR = new Creator<Shopper>() {
        @Override
        public Shopper createFromParcel(Parcel source) {
          return new Shopper(source);
        }

        @Override
        public Shopper[] newArray(int size) {
          return new Shopper[size];
        }
      };

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

      public Shopper(Parcel source) {
        photo = source.readString();
        name = source.readString();
        latitude = source.readDouble();
        longitude = source.readDouble();
      }

      @Override
      public int describeContents() {
        return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photo);
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
      }
    }
  }
}