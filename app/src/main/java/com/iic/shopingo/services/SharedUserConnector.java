package com.iic.shopingo.services;

/**
 * Created by ifeins on 3/3/15.
 */
public class SharedUserConnector {

  private static UserConnector instance;

  private SharedUserConnector() {
    // prevent instantiation
  }

  public static synchronized UserConnector getInstance() {
    return instance;
  }

  public static synchronized void setInstance(UserConnector connector) {
    instance = connector;
  }
}
