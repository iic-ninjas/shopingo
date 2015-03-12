package com.iic.shopingo.ui.trip_flow.activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.PriceHelper;
import com.iic.shopingo.R;
import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.trip.AcceptRequest;
import com.iic.shopingo.api.trip.DeclineRequest;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.ui.ApiTask;

/**
 * Created by asafg on 03/03/15.
 */
public class RequestDetails extends FragmentActivity {
  public static final String EXTRA_REQUEST = "request";

  public static final int RESULT_NONE = 0;
  public static final int RESULT_ACCEPT = 1;
  public static final int RESULT_DECLINE = 2;

  private IncomingRequest request;

  @InjectView(R.id.request_details_requester_name) TextView name;
  @InjectView(R.id.request_details_num_items) TextView numItems;
  @InjectView(R.id.request_details_items_list) ListView itemsList;
  @InjectView(R.id.request_details_offer) TextView offer;
  @InjectView(R.id.request_details_address) TextView address;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (!intent.hasExtra(EXTRA_REQUEST)) {
      throw new IllegalArgumentException("Must pass EXTRA_REQUEST to RequestDetails");
    } else {
      request = intent.getParcelableExtra(EXTRA_REQUEST);

      setContentView(R.layout.request_details);
      ButterKnife.inject(this);

      name.setText(request.getRequester().getFirstName());
      numItems.setText(request.getShoppingList().getItems().size() + " Items");
      offer.setText(PriceHelper.getUSDPriceString(request.getShoppingList().getOffer()));
      address.setText(request.getRequester().getStreetAddress());

      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
      adapter.addAll(request.getShoppingList().getItems());
      itemsList.setAdapter(adapter);
    }
  }

  @OnClick(R.id.request_details_call_button)
  public void onCall(View view) {
    Intent intent = new Intent(Intent.ACTION_CALL);
    intent.setData(Uri.parse("tel:" + request.getRequester().getPhoneNumber()));
    startActivity(intent);
  }

  @OnClick(R.id.request_details_accept_button)
  public void onAccept(View view) {
    ApiTask<ApiResult> task = new ApiTask<>(getSupportFragmentManager(), "Accepting request...", new AcceptRequest(
        CurrentUser.getToken(), request.getId()));

    task.execute().continueWith(new Continuation<ApiResult, Object>() {
      @Override
      public Object then(Task<ApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          setResult(RESULT_ACCEPT);
          finish();
        } else {
          Toast.makeText(RequestDetails.this, "Could not accept: " + task.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);

  }

  @OnClick(R.id.request_details_decline_button)
  public void onReject(View view) {
    ApiTask<ApiResult> task = new ApiTask<>(getSupportFragmentManager(), "Declining request...", new DeclineRequest(
        CurrentUser.getToken(), request.getId()));

    task.execute().continueWith(new Continuation<ApiResult, Object>() {
      @Override
      public Object then(Task<ApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          setResult(RESULT_DECLINE);
          finish();
        } else {
          Toast.makeText(RequestDetails.this, "Could not decline: " + task.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }
}
