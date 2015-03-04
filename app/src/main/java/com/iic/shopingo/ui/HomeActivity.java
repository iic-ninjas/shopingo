package com.iic.shopingo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.iic.shopingo.R;
import com.iic.shopingo.services.SharedUserConnector;

public class HomeActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

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
    }

    return super.onOptionsItemSelected(item);
  }

  private void navigateToOnboarding() {
    Intent intent = new Intent(this, OnboardingActivity.class);
    startActivity(intent);
  }
}
