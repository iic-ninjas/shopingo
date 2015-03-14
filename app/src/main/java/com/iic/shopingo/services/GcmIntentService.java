package com.iic.shopingo.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by ifeins on 3/14/15.
 */
public class GcmIntentService extends IntentService {

  private static final String SERVICE_NAME = GcmIntentService.class.getSimpleName();

  /**
   * Creates an IntentService.  Invoked by your subclass's constructor.
   */
  public GcmIntentService() {
    super(SERVICE_NAME);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    
  }
}
