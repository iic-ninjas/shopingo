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
import mockit.Injectable;
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

  @Injectable
  private SharedPreferences sharedPreferences;

  @Injectable
  private Session fbSession;

  @Mocked
  private Request anyFbRequest;

  @Injectable
  private Response fbResponse;

  private Request.GraphUserCallback fbCallback;

  @Before
  public void beforeEach() {
    subject = new UserConnector(sharedPreferences);
  }

  @Test
  public void testConnectWithFacebookWhenLoginFails() {
    new Expectations() {{
      Request.newMeRequest(fbSession, (Request.GraphUserCallback) any);
      result = new Delegate<Request>() {
        Request delegate(Session session, Request.GraphUserCallback callback) {
          fbCallback = callback;
          return anyFbRequest;
        }
      };

      fbResponse.getError();
      result = new FacebookRequestError(0, "no_internet", "No internet");

      anyFbRequest.executeAsync();
      result = new Delegate<RequestAsyncTask>() {
        RequestAsyncTask delegate() {
          fbCallback.onCompleted(null, fbResponse);
          return null;
        }
      };
    }};

    Task<User> task = subject.connectWithFacebook(fbSession);
    Assert.assertTrue("task should be faulted", task.isFaulted());
  }

}
