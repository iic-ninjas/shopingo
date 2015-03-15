package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.R;
import com.iic.shopingo.api.request.MakeRequestCommand;
import com.iic.shopingo.api.request.OutgoingRequestApiResult;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.ShoppingList;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.ui.ApiTask;
import com.iic.shopingo.ui.request_flow.views.CreateRequestItemListView;

public class CreateShoppingListActivity extends ActionBarActivity implements TextWatcher {

  public static final String EXTRAS_SHOPPER_KEY = "shopper";

  @InjectView(R.id.create_request_items_list)
  CreateRequestItemListView itemListView;

  @InjectView(R.id.create_request_price_input)
  EditText priceView;

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

    if (shoppingList.getOffer() != 0) {
      priceView.setText(Integer.toString(shoppingList.getOffer()));
    }

    priceView.addTextChangedListener(this);
  }

  @OnClick(R.id.create_request_create_button)
  public void onCreateRequest(View view) {
    shoppingList.setItems(itemListView.getAllItems());
    shoppingList.setOffer(Integer.parseInt(priceView.getText().toString().substring(1)));

    ApiTask<OutgoingRequestApiResult> task = new ApiTask<>(getSupportFragmentManager(), "Making request...", new MakeRequestCommand(
        CurrentUser.getToken(), shoppingList.getItems(), shoppingList.getOffer(), shopper.getId()));
    task.execute().continueWith(new Continuation<OutgoingRequestApiResult, Object>() {
      @Override
      public Object then(Task<OutgoingRequestApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          CurrentUser.getInstance().state = CurrentUser.State.REQUESTING;
          CurrentUser.getInstance().save();
          Intent intent = new Intent(CreateShoppingListActivity.this, RequestStateActivity.class);
          intent.putExtra(RequestStateActivity.EXTRAS_REQUEST_KEY, task.getResult().request);
          startActivity(intent);
        } else {
          Toast.makeText(CreateShoppingListActivity.this, "Could not make request: " + task.getError().getMessage(),
              Toast.LENGTH_LONG).show();
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
  }

  @Override
  public void afterTextChanged(Editable s) {
    String text = s.toString();
    if (!text.contains("$")) {
      priceView.removeTextChangedListener(this);
      priceView.setTextKeepState("$" + text);
      Selection.setSelection(priceView.getText(), text.length() + 1);
      priceView.addTextChangedListener(this);
    }
  }
}
