package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.iic.shopingo.R;
import com.iic.shopingo.api.request.MakeRequestCommand;
import com.iic.shopingo.api.request.OutgoingRequestApiResult;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.ShoppingList;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.ui.ApiTask;
import com.iic.shopingo.ui.request_flow.views.CreateRequestItemListView;
import com.squareup.picasso.Picasso;

public class CreateShoppingListActivity extends ActionBarActivity
    implements TextWatcher, CreateRequestItemListView.OnRequestItemListChanged {
  public static final String EXTRAS_SHOPPER_KEY = "shopper";

  @InjectView(R.id.create_request_items_list)
  CreateRequestItemListView itemListView;

  @InjectView(R.id.create_request_create_button)
  Button createRequestButton;

  @InjectView(R.id.create_request_offer_input)
  EditText offerView;

  @InjectView(R.id.create_request_shopper_avatar)
  ImageView shopperAvatar;

  @InjectView(R.id.create_request_shopper_name)
  TextView shopperName;

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

    Picasso.with(this).load(shopper.getAvatarUrl()).into(shopperAvatar);
    shopperName.setText(shopper.getName());

    offerView.addTextChangedListener(this);
  }

  @OnTextChanged(R.id.create_request_offer_input)
  public void onOfferChanged(CharSequence text) {
    toggleCreateButton();
  }

  @Override
  public void onItemListChanged() {
    toggleCreateButton();
  }

  @OnClick(R.id.create_request_create_button)
  public void onCreateRequest(View view) {
    shoppingList.setItems(itemListView.getAllItems());
    shoppingList.setOffer(Integer.parseInt(offerView.getText().toString().substring(1)));

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

  private void toggleCreateButton() {
    boolean valid = itemListView.getAllItems().size() > 0 && offerView.getText().toString().length() > 1;
    createRequestButton.setEnabled(valid);
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
      offerView.removeTextChangedListener(this);
      offerView.setTextKeepState("$" + text);
      Selection.setSelection(offerView.getText(), text.length() + 1);
      offerView.addTextChangedListener(this);
    }
  }
}
