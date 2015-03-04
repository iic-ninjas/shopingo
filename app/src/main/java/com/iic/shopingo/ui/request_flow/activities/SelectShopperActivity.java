package com.iic.shopingo.ui.request_flow.activities;

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
import com.iic.shopingo.ui.request_flow.views.SelectShopperListItemView;
import java.util.ArrayList;
import java.util.List;

public class SelectShopperActivity extends ActionBarActivity {

  public static final String REQUEST_EXTRA_KEY = "request";

  @InjectView(R.id.select_shopper_list)
  ListView shopperList;

  private SelectShopperAdapter.ShopRequest request;
  private SelectShopperAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    request = getIntent().getParcelableExtra(REQUEST_EXTRA_KEY);
    if (request == null) {
      request = new SelectShopperAdapter.ShopRequest();
    }

    setContentView(R.layout.activity_select_shopper);
    ButterKnife.inject(this);

    List<SelectShopperAdapter.Shopper> shoppers = new ArrayList<>();
    // TODO: Get actual shoppers
    adapter = new SelectShopperAdapter(shoppers);
    shopperList.setAdapter(adapter);
  }

  @OnItemClick(R.id.select_shopper_list)
  public void onListItemClick(int position) {
    Intent intent = new Intent(this, CreateRequestActivity.class);
    request.shopper = adapter.getItem(position);
    intent.putExtra(CreateRequestActivity.REQUEST_EXTRA_KEY, request);
    startActivity(intent);
  }

  public static class SelectShopperAdapter extends BaseAdapter {
    private List<Shopper> shoppers;

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
      return itemView;
    }

    // TODO: Move to actual model
    public static class ShopRequest implements Parcelable {
      public static enum RequestStatus { PENDING, APPROVED, DECLINED }

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
    }

    // TODO: Move actual model
    public static class Shopper implements Parcelable {
      public String photo;
      public String name;
      public Long latitude;
      public Long longitude;

      public Shopper(String photo, String name, Long latitude, Long longitude) {
        this.photo = photo;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
      }

      public Shopper(Parcel source) {
        photo = source.readString();
        name = source.readString();
        latitude = source.readLong();
        longitude = source.readLong();
      }

      @Override
      public int describeContents() {
        return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photo);
        dest.writeString(name);
        dest.writeLong(latitude);
        dest.writeLong(longitude);
      }

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
    }
  }
}