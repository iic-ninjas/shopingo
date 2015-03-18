package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import com.iic.shopingo.R;
import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.request.CancelRequestCommand;
import com.iic.shopingo.api.request.SettleRequestCommand;
import com.iic.shopingo.api.trip.StartTripCommand;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.events.AppEventBus;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.notifications.ShopRequestNotification;
import com.iic.shopingo.ui.ApiTask;
import com.iic.shopingo.ui.HomeActivity;
import com.iic.shopingo.ui.trip_flow.activities.ManageTripActivity;
import com.squareup.otto.Subscribe;

public class RequestStateActivity extends ActionBarActivity {
  public static final String EXTRAS_REQUEST_KEY = "request";

  private static final int[] LAYOUTS = new int[] {
      R.layout.activity_request_state_pending,
      R.layout.activity_request_state_accepted,
      R.layout.activity_request_state_declined
  };

  private static final int[] EXPLANATION_STRINGS = new int[] {
    R.string.request_state_pending_explanation,
    R.string.request_state_accepted_explanation,
    R.string.request_state_declined_explanation
  };

  private OutgoingRequest request;

  @Optional
  @InjectView(R.id.request_state_status_explanation)
  TextView statusExplanation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    request = getIntent().getParcelableExtra(EXTRAS_REQUEST_KEY);
    setContentView(LAYOUTS[request.getStatus().ordinal()]);
    ButterKnife.inject(this);
    String explanationString = getString(EXPLANATION_STRINGS[request.getStatus().ordinal()]);
    statusExplanation.setText(String.format(explanationString, request.getShopper().getFirstName()));
  }

  @Override
  protected void onResume() {
    super.onResume();
    AppEventBus.getInstance().register(this);
  }

  @Override
  protected void onPause() {
    AppEventBus.getInstance().unregister(this);
    super.onPause();
  }

  @Subscribe
  public void onRequestStateChanged(ShopRequestNotification notification) {
    BaseRequest.RequestStatus newStatus = BaseRequest.RequestStatus.valueOf(notification.getStatus().toUpperCase());
    if (!newStatus.equals(request.getStatus())) {
      reload(newStatus);
    }
  }

  private void reload(BaseRequest.RequestStatus newStatus) {
    request.setStatus(newStatus);
    getIntent().putExtra(EXTRAS_REQUEST_KEY, request);

    finish();
    startActivity(getIntent());
  }

  @Optional
  @OnClick(R.id.request_state_cancel_button)
  public void onCancelRequest(View view) {
    ApiTask<ApiResult> task = new ApiTask<>(getSupportFragmentManager(), "Cancelling request...", new CancelRequestCommand(CurrentUser.getToken()));

    task.execute().continueWith(new Continuation<ApiResult, Object>() {
      @Override
      public Object then(Task<ApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          request.setStatus(BaseRequest.RequestStatus.CANCELED);
          CurrentUser.getInstance().state = CurrentUser.State.IDLE;
          CurrentUser.getInstance().save();
          finishAndStartActivity(SelectShopperActivity.class);
        } else {
          Toast.makeText(RequestStateActivity.this, "Could not cancel request: " + task.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  @Optional
  @OnClick(R.id.request_state_settle_button)
  public void onSettleRequest(View view) {
    ApiTask<ApiResult> task = new ApiTask<>(getSupportFragmentManager(), "Settling request...", new SettleRequestCommand(CurrentUser.getToken()));

    task.execute().continueWith(new Continuation<ApiResult, Object>() {
      @Override
      public Object then(Task<ApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          request.setStatus(BaseRequest.RequestStatus.SETTLED);
          CurrentUser.getInstance().state = CurrentUser.State.IDLE;
          CurrentUser.getInstance().save();
          finishAndStartActivity(HomeActivity.class);
        } else {
          Toast.makeText(RequestStateActivity.this, "Could not settle request: " + task.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  @Optional
  @OnClick(R.id.request_state_try_again_button)
  public void onTryAgain(View view) {
    finishAndStartActivity(SelectShopperActivity.class);
  }

  @Optional
  @OnClick(R.id.request_state_go_yourself_button)
  public void onGoYourself(View view) {
    ApiTask<ApiResult> task = new ApiTask<>(getSupportFragmentManager(), "Starting trip...", new StartTripCommand(CurrentUser.getToken()));

    task.execute().continueWith(new Continuation<ApiResult, Void>() {
      @Override
      public Void then(Task<ApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          CurrentUser.getInstance().state = CurrentUser.State.TRIPPING;
          Intent intent = new Intent(RequestStateActivity.this, ManageTripActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(intent);
        } else {
          Toast.makeText(RequestStateActivity.this, "Could not start trip: " + task.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  private void finishAndStartActivity(Class<?> cls) {
    Intent intent = new Intent(this, cls);
    startActivity(intent);
    finish();
  }
}
