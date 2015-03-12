package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;
import com.iic.shopingo.R;
import com.iic.shopingo.api.request.MakeRequest;
import com.iic.shopingo.api.request.OutgoingRequestApiResult;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.dal.models.ShoppingList;
import com.iic.shopingo.services.CurrentUser;

public class SaveRequestActivity extends ActionBarActivity {

  public static final String EXTRAS_SHOPPER_KEY = "shopper";
  public static final String EXTRAS_SHOPPING_LIST_KEY = "shopping_list";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_save_request);

    Contact shopper = getIntent().getParcelableExtra(EXTRAS_SHOPPER_KEY);
    ShoppingList shoppingList = getIntent().getParcelableExtra(EXTRAS_SHOPPING_LIST_KEY);

    MakeRequest req = new MakeRequest(CurrentUser.getToken(), shoppingList.getItems(), shoppingList.getOffer(), shopper.getId());
    req.executeAsync().continueWith(new Continuation<OutgoingRequestApiResult, Object>() {
      @Override
      public Object then(Task<OutgoingRequestApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          CurrentUser.getInstance().state = CurrentUser.State.REQUESTING;
          CurrentUser.getInstance().save();
          Intent intent = new Intent(SaveRequestActivity.this, RequestStateActivity.class);
          intent.putExtra(RequestStateActivity.EXTRAS_REQUEST_KEY, task.getResult().request);
          startActivity(intent);
        } else {
          Toast.makeText(SaveRequestActivity.this, "Could not make request: " + task.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
        finish();
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }
}