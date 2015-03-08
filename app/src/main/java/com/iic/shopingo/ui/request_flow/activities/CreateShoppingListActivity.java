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
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.dal.models.ShoppingList;
import com.iic.shopingo.ui.request_flow.views.CreateRequestItemListView;
import java.util.Currency;
import java.util.Locale;

public class CreateShoppingListActivity extends ActionBarActivity {

  public static final String EXTRAS_REQUEST_KEY = "request";

  @InjectView(R.id.create_request_items_list)
  CreateRequestItemListView itemListView;

  @InjectView(R.id.create_request_currency_symbol)
  TextView currencyView;

  @InjectView(R.id.create_request_price_input)
  EditText priceView;

  private OutgoingRequest request;

  private ShoppingList shoppingList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    request = getIntent().getParcelableExtra(EXTRAS_REQUEST_KEY);
    shoppingList = request.getShoppingList();

    setContentView(R.layout.activity_create_request);
    ButterKnife.inject(this);

    itemListView.addAllItems(request.getShoppingList().getItems());
    itemListView.addItem("");

    if (shoppingList.getOffer() != 0) {
      priceView.setText(Integer.toString(shoppingList.getOffer()));
    }

    currencyView.setText(Currency.getInstance(Locale.getDefault()).getSymbol());
  }

  @OnClick(R.id.create_request_create_button)
  public void onCreateRequest(View view) {
    shoppingList.setItems(itemListView.getAllItems());
    shoppingList.setOffer(Integer.parseInt(priceView.getText().toString()));
    request.setShoppingList(shoppingList);
    request.setStatus(BaseRequest.RequestStatus.PENDING);
    // TODO: Create request on server and move to state activity
    Intent intent = new Intent(this, RequestStateActivity.class);
    intent.putExtra(RequestStateActivity.EXTRAS_REQUEST_KEY, request);
    startActivity(intent);
    finish();
  }
}
