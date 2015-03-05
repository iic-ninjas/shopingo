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

  @Mocked
  private Request fbRequest;

  @Mocked
  private Session fbSession;

  private Request.GraphUserCallback fbCallback;

  @Before
  public void beforeEach() {
    subject = new UserConnector(sharedPreferences);
  }

  @Test
  public void testConnectWithFacebookWhenLoginFails() {
    new Expectations() {{
      fbResponse.getError();
      result = new FacebookRequestError(0, "no_internet", "No internet");

      fbRequest.executeAsync();
      result = new Delegate<RequestAsyncTask>() {
        RequestAsyncTask delegate() {
          fbCallback.onCompleted(null, fbResponse);
          return null;
        }
      };

      final Request fbRequest = Request.newMeRequest(fbSession, (Request.GraphUserCallback) any);
      result = new Delegate<Request>() {
        Request delegate(Session session, Request.GraphUserCallback callback) {
          fbCallback = callback;
          return fbRequest;
        }
      };

    }};

    Task<User> task = subject.connectWithFacebook(fbSession);
    Assert.assertTrue("task should be faulted", task.isFaulted());
  }



}
