package com.iic.shopingo.ui.trip_flow.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.PriceHelper;
import com.iic.shopingo.R;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by asafg on 03/03/15.
 */
public class RequestListItem extends FrameLayout {

  public interface RequestListener {
    public void onRequestAccepted(IncomingRequest request);
    public void onRequestDeclined(IncomingRequest request);
  }

  @InjectView(R.id.request_list_item_avatar)
  ImageView thumbnail;

  @InjectView(R.id.request_list_item_name)
  TextView name;

  @InjectView(R.id.request_list_item_items_count)
  TextView numItems;

  @InjectView(R.id.request_list_item_items_list)
  LinearLayout itemsList;

  @InjectView(R.id.request_list_item_offer)
  TextView offer;

  @InjectView(R.id.request_list_item_address)
  TextView address;

  @InjectView(R.id.request_list_item_expandable_content)
  LinearLayout expandableContent;

  private boolean isExpanded;

  private IncomingRequest request;

  private RequestListener listener;

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

  public void toggleExpanded() {
    if (isExpanded) {
      collapse();
    } else {
      expand();
    }
  }

  private void expand() {
    expandableContent.setVisibility(VISIBLE);
    isExpanded = true;
  }

  private void collapse() {
    expandableContent.setVisibility(GONE);
    isExpanded = false;
  }

  public void setRequest(IncomingRequest request) {
    this.request = request;

    Picasso.with(getContext()).load(request.getRequester().getAvatarUrl()).resize(300, 300).centerCrop().into(this.thumbnail);

    name.setText(request.getRequester().getName());
    numItems.setText(getResources().getQuantityString(R.plurals.items_count, request.getShoppingList().getItems().size(), request.getShoppingList().getItems().size(), PriceHelper.getUSDPriceString(request.getShoppingList().getOffer())));
    offer.setText(getContext().getString(R.string.format_offered_price, PriceHelper.getUSDPriceString(request.getShoppingList().getOffer())));
    address.setText(getContext().getString(R.string.format_delivery_address, request.getRequester().getStreetAddress(), request.getRequester().getCity()));
    setupItemList(request.getShoppingList().getItems());
    collapse();
  }

  private void setupItemList(List<String> items) {
    itemsList.removeAllViews();
    for (String item : items) {
      TextView textView = new TextView(getContext());
      textView.setText(item);
      textView.setTextSize(20);
      int spacing = (int) getResources().getDimension(R.dimen.small_spacing);
      textView.setPadding(spacing, spacing, spacing, spacing);
      itemsList.addView(textView);

      View dividerView = new View(getContext());
      dividerView.setBackgroundColor(getResources().getColor(R.color.baseTextColor));
      dividerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
      itemsList.addView(dividerView);
    }
  }

  public void setListener(RequestListener listener) {
    this.listener = listener;
  }

  private void init() {
    LayoutInflater inflater = LayoutInflater.from(getContext());

    inflater.inflate(R.layout.request_list_item, this, true);
    ButterKnife.inject(this);
  }

  @OnClick(R.id.request_list_item_accept_button)
  public void onAccept(View view) {
    if (listener != null) {
      listener.onRequestAccepted(request);
    }
  }

  @OnClick(R.id.request_list_item_decline_button)
  public void onDecline(View view) {
    if (listener != null) {
      listener.onRequestDeclined(request);
    }
  }
}
