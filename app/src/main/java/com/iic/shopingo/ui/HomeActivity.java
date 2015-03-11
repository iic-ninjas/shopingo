package com.iic.shopingo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.iic.shopingo.R;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.SharedUserConnector;
import com.iic.shopingo.ui.request_flow.activities.SelectShopperActivity;
import com.iic.shopingo.ui.trip_flow.activities.ManageTripActivity;

public class HomeActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    switch (CurrentUser.getInstance().state) {
      case LOGGED_OUT:
        navigateToOnboarding();
        break;
      case IDLE:
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        break;
      case TRIPPING:
        // TODO: go to shopping experience.
        break;
      case REQUESTING:
        // TODO: go to request screen.
        break;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case R.id.action_logout:
        SharedUserConnector.getInstance().logout(this);
        navigateToOnboarding();
        break;
      case R.id.action_update_contact:
        Intent intent = new Intent(this, ContactDetailsActivity.class);
        startActivity(intent);
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.home_create_request_btn)
  public void onCreateRequest(View view) {
    Intent intent = new Intent(this, SelectShopperActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.home_go_shopping_btn)
  public void onGoShopping(View view) {
    Intent intent = new Intent(this, ManageTripActivity.class);
    startActivity(intent);
    // TODO: transition to `shopping` state
    finish();
  }

  private void navigateToOnboarding() {
    Intent intent = new Intent(this, OnboardingActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
  }
}