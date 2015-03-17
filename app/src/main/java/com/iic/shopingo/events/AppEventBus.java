package com.iic.shopingo.events;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Event bus used in the application.
 * Created by ifeins on 2/26/15.
 */
public class AppEventBus extends Bus {

  private static final Bus instance = new AppEventBus();

  private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

  public static Bus getInstance() {
    return instance;
  }

  private AppEventBus() {
    super(ThreadEnforcer.MAIN);
  }

  @Override
  public void post(final Object event) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      super.post(event);
    } else {
      mainThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          post(event);
        }
      });
    }
  }

}
