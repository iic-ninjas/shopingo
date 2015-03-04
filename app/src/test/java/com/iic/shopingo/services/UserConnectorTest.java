package com.iic.shopingo.services;

import android.content.SharedPreferences;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test for UserConnector
 * Created by ifeins on 3/4/15.
 */
public class UserConnectorTest {

  private UserConnector subject;

  private SharedPreferences sharedPreferences;

  @Before
  public void setUp()  {
    sharedPreferences = Mockito.mock(SharedPreferences.class);
    subject = new UserConnector(sharedPreferences);
  }

  @Test
  public void testConnectWithFacebook() {
    
  }

}
