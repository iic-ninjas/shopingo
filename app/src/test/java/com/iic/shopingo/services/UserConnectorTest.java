package com.iic.shopingo.services;

import android.content.SharedPreferences;
import bolts.Task;
import com.facebook.FacebookRequestError;
import com.facebook.Response;
import com.iic.shopingo.dal.models.User;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for UserConnector
 * Created by ifeins on 3/4/15.
 */
public class UserConnectorTest {

  private UserConnector subject;

  @Mocked
  private SharedPreferences sharedPreferences;

  @Mocked
  private Response fbResponse;

  @Before
  public void beforeEach()  {
    subject = new UserConnector(sharedPreferences);
  }

  /**
   * It should return a faulted task
   */
  @Test
  public void testHandleFacebookFetchResultWhenLoginFails() {
    new Expectations() {{
      fbResponse.getError();
      result = new FacebookRequestError(0, "no_internet", "No internet");
    }};

    Task<User>.TaskCompletionSource deferred = Task.create();
    subject.handleFacebookFetchResult(null, fbResponse, deferred);
    Assert.assertTrue("Task should be faulted", deferred.getTask().isFaulted());
  }

}
