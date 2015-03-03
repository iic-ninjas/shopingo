package com.iic.shopingo.ui.request_flow;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import com.iic.shopingo.R;

public class RequestStateActivity extends ActionBarActivity {

  public static final String REQUEST_STATE_EXTRA_KEY = "request_state";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String state = getIntent().getStringExtra(REQUEST_STATE_EXTRA_KEY);
    int layout;
    switch (state) {
      case "approved":
        layout = R.layout.activity_request_state_approved;
        break;
      case "declined":
        layout = R.layout.activity_request_state_declined;
        break;
      default:
        layout = R.layout.activity_request_state_pending;
    }
    setContentView(layout);
  }

  public void onCancelRequest(View view) {
    // TODO: Cancel request in server and go to trippers list
  }

  public void onSettleRequest(View view) {
    // TODO: Settle request in server and go home
  }

  public void onTryAgain(View view) {
    // TODO: Go to trippers list
  }

  public void onGoYourself(View view) {
    // TODO: Create trip and go to trip activity
  }
}
