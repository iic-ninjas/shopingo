package com.iic.shopingo.ui.onboarding.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.iic.shopingo.R;
import com.iic.shopingo.api.user.LoginCommand;
import com.iic.shopingo.api.user.UserApiResult;
import com.iic.shopingo.dal.models.UserInfo;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.FacebookConnector;
import com.iic.shopingo.ui.ApiTask;
import com.iic.shopingo.ui.ContactDetailsActivity;
import com.iic.shopingo.ui.onboarding.OnboardingPagerAdapter;
import com.iic.shopingo.ui.onboarding.views.PagerIndicatorView;

public class OnboardingActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

  private static final String LOG_TAG = OnboardingActivity.class.getSimpleName();

  private UiLifecycleHelper facebookLifecycleHelper;

  private boolean duringLogin;

  private OnboardingPagerAdapter pagerAdapter;

  @InjectView(R.id.onboarding_viewpager)
  ViewPager viewPager;

  @InjectView(R.id.onboarding_pager_indicator)
  PagerIndicatorView indicatorView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_onboarding);

    ButterKnife.inject(this);

    facebookLifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
      @Override
      public void call(Session session, SessionState sessionState, Exception e) {
        onSessionStateChanged(session, sessionState, e);
      }
    });
    facebookLifecycleHelper.onCreate(savedInstanceState);

    pagerAdapter = new OnboardingPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(pagerAdapter);
    viewPager.setOnPageChangeListener(this);
    indicatorView.setPages(pagerAdapter.getCount());
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
    FacebookConnector.login(session).continueWith(new Continuation<UserInfo, Void>() {
      @Override
      public Void then(Task<UserInfo> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          final CurrentUser currentUser = CurrentUser.getInstance();

          UserInfo info = task.getResult();

          ApiTask<UserApiResult> apiTask = new ApiTask<UserApiResult>(getSupportFragmentManager(), "Logging you in...", new LoginCommand(
              info.getUid(),
              info.getFirstName(),
              info.getLastName(),
              info.getStreet(),
              info.getCity(),
              info.getPhoneNumber()
          ));

          apiTask.execute().continueWith(new Continuation<UserApiResult, Object>() {
            @Override
            public Object then(Task<UserApiResult> task) throws Exception {
              if (!task.isFaulted() && !task.isCancelled()) {
                currentUser.state = task.getResult().userState;
                currentUser.userInfo = task.getResult().userContactInfo;
                currentUser.save();
                Intent intent = new Intent(OnboardingActivity.this, ContactDetailsActivity.class);
                startActivity(intent);
                finish();
                duringLogin = false;
              } else {
                Toast.makeText(OnboardingActivity.this, "Could not login: " + task.getError(), Toast.LENGTH_LONG).show();
              }
              return null;
            }
          }, Task.UI_THREAD_EXECUTOR);
        } else if (task.isFaulted()) {
          Toast.makeText(OnboardingActivity.this, "Could not login: " + task.getError(), Toast.LENGTH_LONG).show();
        }

        // task can also be cancelled, in that case we do nothing.
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  private void logout() {
    CurrentUser.getInstance().logout();
  }

  @Override
  public void onPageSelected(int position) {

  }

  @Override
  public void onPageScrollStateChanged(int state) {
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    indicatorView.setPosition(position + positionOffset);

  }
}
