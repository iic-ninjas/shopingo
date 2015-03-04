package com.iic.shopingo.ui.request_flow.views;

import android.content.Context;
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
    Picasso.with(getContext()).load(shopper.photo).into(photoView);
    nameView.setText(shopper.name);
    int distanceInMeters = CurrentLocationService.distanceToPoint(shopper.latitude, shopper.longitude);
    distanceView.setText(
        String.format(getContext().getString(R.string.select_shopper_distance_format), distanceInMeters / 1000.0f));
  }

  // TODO: Move to actual service
  public static class CurrentLocationService {
    public static int distanceToPoint(Long latitude, Long longitude) {
      return 1200; // This was calculated in advance.
    }
  }
}
