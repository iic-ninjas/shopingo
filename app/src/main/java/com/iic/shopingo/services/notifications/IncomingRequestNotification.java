package com.iic.shopingo.services.notifications;

import com.iic.shopingo.api.models.ApiContact;
import java.util.List;

/**
 * A notification that is intended to the shopper.
 * Created by IICMacbook1 on 3/16/15.
 */
public class IncomingRequestNotification implements ShopingoNotification {

  private String id;

  private String status;

  private List<String> items;

  private int offer;

  private ApiContact requester;

  public String getId() {
    return id;
  }

  public String getStatus() {
    return status;
  }

  public List<String> getItems() {
    return items;
  }

  public int getOffer() {
    return offer;
  }

  public ApiContact getRequester() {
    return requester;
  }
}
