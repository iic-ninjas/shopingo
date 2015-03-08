package com.iic.shopingo.ui.request_flow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import com.iic.shopingo.R;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.ui.HomeActivity;

public class RequestStateActivity extends ActionBarActivity {
  public static final String EXTRAS_REQUEST_KEY = "request";

  private static final int[] LAYOUTS = new int[] {
      R.layout.activity_request_state_pending, R.layout.activity_request_state_approved,
      R.layout.activity_request_state_declined
  };

  private OutgoingRequest request;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    request = getIntent().getParcelableExtra(EXTRAS_REQUEST_KEY);
    setContentView(LAYOUTS[request.getStatus().ordinal()]);
    ButterKnife.inject(this);
  }

  @Optional
  @OnClick(R.id.request_state_cancel_button)
  public void onCancelRequest(View view) {
    request.setStatus(BaseRequest.RequestStatus.CANCELED);
    // TODO: Cancel request in server
    goToActivity(SelectShopperActivity.class);
  }

  @Optional
  @OnClick(R.id.request_state_settle_button)
  public void onSettleRequest(View view) {
    request.setStatus(BaseRequest.RequestStatus.SETTLED);
    // TODO: Settle request in server
    goToActivity(HomeActivity.class);
  }

  @Optional
  @OnClick(R.id.request_state_try_again_button)
  public void onTryAgain(View view) {
    goToActivity(SelectShopperActivity.class);
  }

  @Optional
  @OnClick(R.id.request_state_go_yourself_button)
  public void onGoYourself(View view) {
    // TODO: Create trip and go to trip activity
  }

  private void goToActivity(Class<?> cls) {
    Intent intent = new Intent(this, cls);
    startActivity(intent);
    finish();
  }
}
