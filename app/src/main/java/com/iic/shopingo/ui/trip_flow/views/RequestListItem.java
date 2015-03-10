package com.iic.shopingo.ui.trip_flow.views;

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
import com.iic.shopingo.ui.utils.AvatarUriGenerator;
import com.squareup.picasso.Picasso;
import com.iic.shopingo.dal.models.BaseRequest;

/**
 * Created by asafg on 03/03/15.
 */
public class RequestListItem extends FrameLayout {

  @InjectView(R.id.request_list_item_requester_pic) ImageView thumbnail;
  @InjectView(R.id.request_list_item_requester_name) TextView name;
  @InjectView(R.id.request_list_item_requested_items_count) TextView numItems;
  @InjectView(R.id.request_list_item_price_offer) TextView offer;

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

  public void setRequest(Bitmap thumbnail, String name, int numItems, int offerInCents, BaseRequest.RequestStatus status) {
    if (thumbnail != null) {
      this.thumbnail.setImageBitmap(thumbnail);
    } else {
      // TODO: remove this code
      Picasso.with(getContext()).load(AvatarUriGenerator.generateAvatarUri(name)).into(this.thumbnail);
    }

    this.name.setText(name);
    this.numItems.setText(String.format("%d items", numItems));
    this.offer.setText(PriceHelper.getUSDPriceString(offerInCents));
    switch (status) {
      case ACCEPTED:
        this.setBackgroundResource(android.R.color.holo_green_light);
        break;
      case DECLINED:
        this.setBackgroundResource(android.R.color.holo_red_light);
        break;
      default:
        this.setBackgroundResource(0);
        break;
    }
  }

  private void init() {
    LayoutInflater inflater = LayoutInflater.from(getContext());

    inflater.inflate(R.layout.request_list_item, this, true);
    ButterKnife.inject(this);
  }
}
