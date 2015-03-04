package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.R;
import com.iic.shopingo.ui.request_flow.views.CreateRequestItemListView;
import com.iic.shopingo.ui.request_flow.views.CreateRequestListItemView;
import java.util.Currency;
import java.util.Locale;

public class CreateRequestActivity extends ActionBarActivity implements
    CreateRequestListItemView.OnListViewChanged {
  public static final String REQUEST_EXTRA_KEY = "request";

  @InjectView(R.id.create_request_items_list)
  CreateRequestItemListView itemListView;

  @InjectView(R.id.create_request_currency_symbol)
  TextView currencyView;

  @InjectView(R.id.create_request_price_input)
  EditText priceView;

  private SelectShopperActivity.SelectShopperAdapter.ShopRequest request;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    request = getIntent().getParcelableExtra(REQUEST_EXTRA_KEY);

    setContentView(R.layout.activity_create_request);
    ButterKnife.inject(this);

    itemListView.addAllItems(request.items);
    itemListView.addItem("");

    if (request.price != 0) {
      priceView.setText(request.price);
    }

    currencyView.setText(Currency.getInstance(Locale.getDefault()).getSymbol());
  }

  @Override
  public void onRemoveButtonClicked(View view) {
    itemListView.removeItem(itemListView.getPositionForView(view));
  }

  @Override
  public void onItemEdited(View view, String value) {
    int position = itemListView.getPositionForView(view);
    if (value.equals("")) {
      if (position != itemListView.size() - 1) {
        itemListView.removeItem(position);
      }
    } else {
      itemListView.setItem(position, value);
      if (position == itemListView.size() - 1) {
        itemListView.addItem("");
        ((CreateRequestListItemView)itemListView.getChildAt(position + 1)).focus();
      }
    }
  }

  @OnClick(R.id.create_request_create_button)
  public void onCreateRequest(View view) {
    request.items = itemListView.getAllItems();
    request.price = Integer.parseInt(priceView.getText().toString());
    // TODO: Create request on server and move to state activity
    Intent intent = new Intent(this, RequestStateActivity.class);
    intent.putExtra(RequestStateActivity.REQUEST_EXTRA_KEY, request);
    startActivity(intent);
  }
}
