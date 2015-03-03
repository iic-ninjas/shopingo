package com.iic.shopingo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iic.shopingo.PriceHelper;
import com.iic.shopingo.R;

/**
 * Created by asafg on 03/03/15.
 */
public class RequestListItem extends FrameLayout {

  @InjectView(R.id.requester_pic) ImageView thumbnail;
  @InjectView(R.id.requester_name) TextView name;
  @InjectView(R.id.requested_items_count) TextView numItems;
  @InjectView(R.id.price_offer) TextView offer;

  public RequestListItem(Context context) {
    super(context);
    init();
  }

  public RequestListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public RequestListItem(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void setRequest(Bitmap thumbnail, String name, int numItems, int offerInCents) {
    if (thumbnail != null) {
      this.thumbnail.setImageBitmap(thumbnail);
    }
    this.name.setText(name);
    this.numItems.setText(Integer.toString(numItems));
    this.offer.setText(PriceHelper.getUSDPriceString(offerInCents));
  }

  private void init() {
    LayoutInflater inflater = LayoutInflater.from(getContext());

    inflater.inflate(R.layout.request_list_item, this, true);
    ButterKnife.inject(this);
  }
}
