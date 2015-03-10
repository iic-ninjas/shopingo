package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.iic.shopingo.R;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.dal.models.ShoppingList;

public class SaveRequestActivity extends ActionBarActivity {

  public static final String EXTRAS_SHOPPER_KEY = "shopper";
  public static final String EXTRAS_SHOPPING_LIST_KEY = "shopping_list";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_save_request);

    Contact shopper = getIntent().getParcelableExtra(EXTRAS_SHOPPER_KEY);
    ShoppingList shoppingList = getIntent().getParcelableExtra(EXTRAS_SHOPPING_LIST_KEY);
    OutgoingRequest request = saveRequest(shopper, shoppingList);
    Intent intent = new Intent(this, RequestStateActivity.class);
    intent.putExtra(RequestStateActivity.EXTRAS_REQUEST_KEY, request);
    startActivity(intent);
    finish();
  }

  // TODO: Move this to somewhere more fitting
  private OutgoingRequest saveRequest(Contact shopper, ShoppingList shoppingList) {
    // TODO: Send `shopper` and `shoppingList` to server and get an `OutgoingRequest` back
    return new OutgoingRequest("123", shopper, shoppingList, BaseRequest.RequestStatus.PENDING);
  }
}