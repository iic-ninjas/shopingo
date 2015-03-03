package com.iic.shopingo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.InjectView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.iic.shopingo.R;

public class OnboardingActivity extends ActionBarActivity {

  private static final String LOG_TAG = OnboardingActivity.class.getSimpleName();

  @InjectView(R.id.onboarding_login_btn)
  LoginButton facebookLoginBtn;

  private UiLifecycleHelper facebookLifecycleHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_onboarding);

    facebookLifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
      @Override
      public void call(Session session, SessionState sessionState, Exception e) {
        onSessionStateChanged(session, sessionState, e);
      }
    });
    facebookLifecycleHelper.onCreate(savedInstanceState);
  }

  @Override
  protected void onResume() {
    super.onResume();

    Session session = Session.getActiveSession();
    if (session != null && (session.isOpened() || session.isClosed())) {
      onSessionStateChanged(session, session.getState(), null);
    }

    facebookLifecycleHelper.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    facebookLifecycleHelper.onPause();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    facebookLifecycleHelper.onDestroy();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    facebookLifecycleHelper.onSaveInstanceState(outState);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    facebookLifecycleHelper.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_onboarding, menu);
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
    }

    return super.onOptionsItemSelected(item);
  }

  private void onSessionStateChanged(Session session, SessionState sessionState, Exception e) {
    if (sessionState.isOpened()) {
      Log.d(LOG_TAG, "Logged in to facebook");
      loginToServer(session);
    } else if (sessionState.isClosed()) {
      Log.d(LOG_TAG, "Logged out from facebook");
      logout();
    }
  }

  private void loginToServer(Session session) {
    Intent intent = new Intent(this, ContactDetailsActivity.class);
    startActivity(intent);
  }

  private void logout() {

  }
}
