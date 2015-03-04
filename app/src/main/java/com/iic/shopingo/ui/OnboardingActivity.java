package com.iic.shopingo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
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

  private UiLifecycleHelper facebookLifecycleHelper;

  private boolean duringLogin;

  @InjectView(R.id.onboarding_login_btn)
  LoginButton loginBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_onboarding);

    ButterKnife.inject(this);

    loginBtn.setReadPermissions("public_profile", "user_location");
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

    // For scenarios where the main activity is launched and user session is not null, the session state change notification
    // may not be triggered. Trigger it if it's open/closed.
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

  private void onSessionStateChanged(Session session, SessionState sessionState, Exception e) {
    if (sessionState.isOpened() && !duringLogin) {
      Log.d(LOG_TAG, "Logged in to facebook");
      login(session);
    } else if (sessionState.isClosed()) {
      Log.d(LOG_TAG, "Facebook session is closed");
      logout();
    }
  }

  private void login(Session session) {
    duringLogin = true;
    SharedUserConnector.getInstance().connectWithFacebook(session).onSuccess(new Continuation<User, Void>() {
      @Override
      public Void then(Task<User> task) throws Exception {
        Intent intent = new Intent(OnboardingActivity.this, ContactDetailsActivity.class);
        startActivity(intent);
        finish();
        duringLogin = false;
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  private void logout() {
    // TODO: unset current user
  }
}
