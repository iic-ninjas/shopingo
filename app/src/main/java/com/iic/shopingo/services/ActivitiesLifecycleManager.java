package com.iic.shopingo.services;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class ActivitiesLifecycleManager implements Application.ActivityLifecycleCallbacks {

  private static ActivitiesLifecycleManager instance;

  private int runningActivitiesCount;

  public static synchronized ActivitiesLifecycleManager getInstance() {
    if (instance == null) {
      instance = new ActivitiesLifecycleManager();
    }

    return instance;
  }

  private ActivitiesLifecycleManager() {
    // prevent instantiation
  }

  public boolean isInForeground() {
    return runningActivitiesCount > 0;
  }

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

  }

  @Override
  public void onActivityStarted(Activity activity) {
    runningActivitiesCount++;
  }

  @Override
  public void onActivityResumed(Activity activity) {
  }

  @Override
  public void onActivityPaused(Activity activity) {

  }

  @Override
  public void onActivityStopped(Activity activity) {
    runningActivitiesCount--;
  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

  }

  @Override
  public void onActivityDestroyed(Activity activity) {

  }
}
