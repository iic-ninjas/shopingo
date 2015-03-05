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
import java.util.Currency;
import java.util.Locale;

public class CreateRequestActivity extends ActionBarActivity {

  public static final String EXTRAS_REQUEST_KEY = "request";

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
    request = getIntent().getParcelableExtra(EXTRAS_REQUEST_KEY);

    setContentView(R.layout.activity_create_request);
    ButterKnife.inject(this);

    itemListView.addAllItems(request.items);
    itemListView.addItem("");

    if (request.price != 0) {
      priceView.setText(Integer.toString(request.price));
    }

    currencyView.setText(Currency.getInstance(Locale.getDefault()).getSymbol());
  }

  @OnClick(R.id.create_request_create_button)
  public void onCreateRequest(View view) {
    request.items = itemListView.getAllItems();
    request.price = Integer.parseInt(priceView.getText().toString());
    // TODO: Create request on server and move to state activity
    Intent intent = new Intent(this, RequestStateActivity.class);
    intent.putExtra(RequestStateActivity.EXTRAS_REQUEST_KEY, request);
    startActivity(intent);
  }
}
