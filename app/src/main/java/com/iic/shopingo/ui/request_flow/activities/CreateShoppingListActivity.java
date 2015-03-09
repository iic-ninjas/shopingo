package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.iic.shopingo.R;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.ShoppingList;
import com.iic.shopingo.ui.request_flow.views.CreateRequestItemListView;
import java.util.Currency;
import java.util.Locale;

public class CreateShoppingListActivity extends ActionBarActivity implements
    CreateRequestItemListView.OnRequestItemListChanged {

  public static final String EXTRAS_SHOPPER_KEY = "shopper";

  @InjectView(R.id.create_request_items_list)
  CreateRequestItemListView itemListView;

  @InjectView(R.id.create_request_currency_symbol)
  TextView currencyView;

  @InjectView(R.id.create_request_offer_input)
  EditText offerView;

  @InjectView(R.id.create_request_create_button)
  Button createRequestButton;

  private Contact shopper;

  private ShoppingList shoppingList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_request);
    ButterKnife.inject(this);

    shopper = getIntent().getParcelableExtra(EXTRAS_SHOPPER_KEY);
    shoppingList = new ShoppingList(); // TODO: Load persisted shopping list from somewhere

    itemListView.addAllItems(shoppingList.getItems());
    itemListView.addItem("");
    itemListView.setListener(this);

    if (shoppingList.getOffer() != 0) {
      offerView.setText(Integer.toString(shoppingList.getOffer()));
    }

    currencyView.setText(Currency.getInstance(Locale.getDefault()).getSymbol());
  }

  @OnTextChanged(R.id.create_request_offer_input)
  public void onOfferChanged(CharSequence text) {
    setCreateButtonEnabled();
  }

  @Override
  public void onItemListChanged(View view) {
    setCreateButtonEnabled();
  }

  @OnClick(R.id.create_request_create_button)
  public void onCreateRequest(View view) {
    shoppingList.setItems(itemListView.getAllItems());
    shoppingList.setOffer(Integer.parseInt(offerView.getText().toString()));
    Intent intent = new Intent(this, SaveRequestActivity.class);
    intent.putExtra(SaveRequestActivity.EXTRAS_SHOPPER_KEY, shopper);
    intent.putExtra(SaveRequestActivity.EXTRAS_SHOPPING_LIST_KEY, shoppingList);
    startActivity(intent);
    finish();
  }

  private void setCreateButtonEnabled() {
    boolean valid = itemListView.getAllItems().size() > 0 && offerView.getText().toString().length() > 0;
    createRequestButton.setEnabled(valid);
  }
}
