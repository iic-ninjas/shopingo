package com.iic.shopingo.ui.request_flow.Views;

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
import com.iic.shopingo.ui.request_flow.SelectShopperAdapter;
import com.squareup.picasso.Picasso;

/**
 * TODO: document your custom view class.
 */
public class SelectShopperListItemView extends LinearLayout {
  @InjectView(R.id.select_shopper_list_item_photo)
  ImageView mPhotoView;

  @InjectView(R.id.select_shopper_list_item_name)
  TextView mNameView;

  @InjectView(R.id.select_shopper_list_item_distance)
  TextView mDistanceView;

  public SelectShopperListItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater.from(context).inflate(R.layout.select_shopper_list_item_children, this, true);
    ButterKnife.inject(this);
  }

  public static SelectShopperListItemView inflate(ViewGroup parent) {
    SelectShopperListItemView itemView = (SelectShopperListItemView) LayoutInflater.from(parent.getContext())
        .inflate(R.layout.select_shopper_list_item, parent, false);
    return itemView;
  }

  public void setUser(SelectShopperAdapter.User user) {
    Picasso.with(getContext()).load(user.photo).into(mPhotoView);
    mNameView.setText(user.name);
    int distanceInMeters = CurrentLocationService.distanceToPoint(user.latitude, user.longitude);
    mDistanceView.setText(String.format(getContext().getString(R.string.select_shopper_distance_format), distanceInMeters / 1000.0f));
  }

  // TODO: Move to actual service
  public static class CurrentLocationService {
    public static int distanceToPoint(Long latitude, Long longitude) {
      return 1200; // This was calculated in advance.
    }
  }
}
