package com.iic.shopingo.ui.trip_flow.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.PriceHelper;
import com.iic.shopingo.R;
import com.iic.shopingo.dal.models.BaseRequest;
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

  @InjectView(R.id.request_list_item_buttons_container)
  LinearLayout buttonsContainer;

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
    itemsList.setVisibility(VISIBLE);
    buttonsContainer.setVisibility(VISIBLE);
    offer.setVisibility(VISIBLE);
    address.setVisibility(VISIBLE);
    isExpanded = true;
  }

  private void collapse() {
    itemsList.setVisibility(GONE);
    buttonsContainer.setVisibility(GONE);
    offer.setVisibility(GONE);
    address.setVisibility(GONE);
    isExpanded = false;
  }

  public void setRequest(IncomingRequest request) {
    this.request = request;

    Picasso.with(getContext()).load(request.getRequester().getAvatarUrl()).into(this.thumbnail);

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
      TextView view = new TextView(getContext());
      view.setText(item);
      view.setTextAppearance(getContext(), R.style.BodyText);
      itemsList.addView(view);
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
    request.setStatus(BaseRequest.RequestStatus.ACCEPTED);
    if (listener != null) {
      listener.onRequestAccepted(request);
    }
  }

  @OnClick(R.id.request_list_item_decline_button)
  public void onDecline(View view) {
    request.setStatus(BaseRequest.RequestStatus.DECLINED);
    if (listener != null) {
      listener.onRequestDeclined(request);
    }
  }
}
