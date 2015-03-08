package com.iic.shopingo.ui.request_flow.views;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iic.shopingo.R;
import com.iic.shopingo.ui.request_flow.activities.SelectShopperActivity;
import com.squareup.picasso.Picasso;

/**
 * TODO: document your custom view class.
 */
public class SelectShopperListItemView extends LinearLayout {
  @InjectView(R.id.select_shopper_list_item_photo)
  ImageView photoView;

  @InjectView(R.id.select_shopper_list_item_name)
  TextView nameView;

  @InjectView(R.id.select_shopper_list_item_distance)
  TextView distanceView;

  private Location shopperLocation = new Location("reverse");

  public SelectShopperListItemView(Context context) {
    super(context);
    init();
  }

  public SelectShopperListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public SelectShopperListItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public static SelectShopperListItemView inflate(ViewGroup parent) {
    SelectShopperListItemView itemView = (SelectShopperListItemView) LayoutInflater.from(parent.getContext())
        .inflate(R.layout.select_shopper_list_item, parent, false);
    return itemView;
  }

  private void init() {
    LayoutInflater.from(getContext()).inflate(R.layout.select_shopper_list_item_children, this, true);
    ButterKnife.inject(this);
  }

  public void setShopper(SelectShopperActivity.SelectShopperAdapter.Shopper shopper) {
    shopperLocation.setLatitude(shopper.latitude);
    shopperLocation.setLongitude(shopper.longitude);
    Picasso.with(getContext()).load(shopper.photo).into(photoView);
    nameView.setText(shopper.name);
    distanceView.setText(
        String.format(getContext().getString(R.string.select_shopper_distance_format), 0f));
  }

  public void setUserLocation(Location userLocation) {
    if (shopperLocation != null) {
      float distanceInMeters = userLocation.distanceTo(shopperLocation);
      distanceView.setText(String.format(getContext().getString(R.string.select_shopper_distance_format), distanceInMeters / 1000.0f));
    }
  }
}
