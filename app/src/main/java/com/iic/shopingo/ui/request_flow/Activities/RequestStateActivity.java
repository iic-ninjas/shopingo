package com.iic.shopingo.ui.request_flow.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import com.iic.shopingo.R;

public class RequestStateActivity extends ActionBarActivity {
  private CreateRequestActivity.Request request;

  public static final String REQUEST_EXTRA_KEY = "request";
  private static final int[] LAYOUTS = new int[] {
    R.layout.activity_request_state_pending,
    R.layout.activity_request_state_approved,
    R.layout.activity_request_state_declined
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    request = getIntent().getParcelableExtra(REQUEST_EXTRA_KEY);
    setContentView(LAYOUTS[request.status.ordinal()]);
    ButterKnife.inject(this);
  }

  @Optional
  @OnClick(R.id.request_state_cancel_button)
  public void onCancelRequest(View view) {
    // TODO: Cancel request in server and go to trippers list
  }

  @Optional
  @OnClick(R.id.request_state_settle_button)
  public void onSettleRequest(View view) {
    // TODO: Settle request in server and go home
  }

  @Optional
  @OnClick(R.id.request_state_try_again_button)
  public void onTryAgain(View view) {
    // TODO: Go to trippers list
  }

  @Optional
  @OnClick(R.id.request_state_go_yourself_button)
  public void onGoYourself(View view) {
    // TODO: Create trip and go to trip activity
  }
}
