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
import com.iic.shopingo.services.SharedUserConnector;
import com.iic.shopingo.ui.request_flow.activities.SelectShopperActivity;

public class HomeActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.inject(this);

    if (!SharedUserConnector.getInstance().isUserSignedIn()) {
      navigateToOnboarding();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    } else if (id == R.id.action_logout) {
      SharedUserConnector.getInstance().logout(this);
      navigateToOnboarding();
    } else if (id == R.id.action_update_contact) {
      Intent intent = new Intent(this, ContactDetailsActivity.class);
      startActivity(intent);
    }

    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.home_create_request_btn)
  public void onCreateRequest(View view) {
    Intent intent = new Intent(this, SelectShopperActivity.class);
    startActivity(intent);
  }

  private void navigateToOnboarding() {
    Intent intent = new Intent(this, OnboardingActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
  }
}
