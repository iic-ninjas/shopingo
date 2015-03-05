package com.iic.shopingo.services;

import android.content.SharedPreferences;
import bolts.Task;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.iic.shopingo.dal.models.User;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;

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

  @Mocked
  private Session fbSession;

  @Before
  public void beforeEach() {
    subject = new UserConnector(sharedPreferences);
  }

  public void testConnectWithFacebookWhenLoginFails() {
    new Expectations() {{
      fbResponse.getError();
      result = new FacebookRequestError(0, "no_internet", "No internet");

      Request.newMeRequest(fbSession, (Request.GraphUserCallback) any);
      result = new Delegate<RequestAsyncTask>() {
        RequestAsyncTask delegate(Session session, Request.GraphUserCallback callback) {
          callback.onCompleted(null, fbResponse);
          return null;
        }
      };
    }};

    Task<User> task = subject.connectWithFacebook(fbSession);
    Assert.assertTrue("task should be faulted", task.isFaulted());
  }

}
