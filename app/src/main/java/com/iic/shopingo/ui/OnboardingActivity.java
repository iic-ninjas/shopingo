package com.iic.shopingo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import butterknife.InjectView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.iic.shopingo.R;
import com.iic.shopingo.dal.models.User;
import com.iic.shopingo.services.SharedUserConnector;

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
    // TODO: create/fetch user from server by facebook uid and navigate to contact details activity if it's a new user
    SharedUserConnector.getInstance().setCurrentUser(new User(1, null));

    Intent intent = new Intent(this, ContactDetailsActivity.class);
    startActivity(intent);
  }

  private void logout() {
    // TODO: unset current user
  }
}
