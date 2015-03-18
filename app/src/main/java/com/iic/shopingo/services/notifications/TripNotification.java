package com.iic.shopingo.services.notifications;

import com.iic.shopingo.api.models.ApiContact;

/**
 * Notification when a trip is started or cancelled.
 * Created by ifeins on 3/18/15.
 */
public class TripNotification implements ShopingoNotification {

  private String status;

  private ApiContact shopper;

  public String getStatus() {
    return status;
  }

  public ApiContact getShopper() {
    return shopper;
  }
}
