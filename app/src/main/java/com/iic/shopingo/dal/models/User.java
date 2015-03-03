package com.iic.shopingo.dal.models;

/**
 * Created by ifeins on 3/3/15.
 */
public class User {

  private int id;

  private String facebookUid;

  public User(int id, String facebookUid) {
    this.id = id;
    this.facebookUid = facebookUid;
  }

  public String getFacebookUid() {
    return facebookUid;
  }

  public int getId() {
    return id;
  }
}
