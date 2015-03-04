package com.iic.shopingo.services;

import android.content.SharedPreferences;
import android.test.AndroidTestCase;
import org.mockito.Mockito;

/**
 * Test for UserConnector
 * Created by ifeins on 3/4/15.
 */
public class UserConnectorTest extends AndroidTestCase {

  private UserConnector subject;

  private SharedPreferences sharedPreferences;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    sharedPreferences = Mockito.mock(SharedPreferences.class);
    subject = new UserConnector(sharedPreferences);
  }

  public void testConnectWithFacebook() {
    
  }

}
