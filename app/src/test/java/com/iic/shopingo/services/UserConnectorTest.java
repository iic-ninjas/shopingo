package com.iic.shopingo.services;

import android.content.SharedPreferences;
import bolts.Task;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.iic.shopingo.dal.models.User;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.junit.After;
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
    SharedUserConnector.setInstance(subject);
  }

  @After
  public void afterEach() {
    SharedUserConnector.setInstance(null);
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

  /**
   * It should return a promise that resolves a new User, and set it as the current user.
   */
  @Test
  public void testConnectWithFacebookWhenLoginSucceeds(
      @Injectable final GraphUser graphUser,
      @Injectable final GraphPlace graphPlace,
      @Injectable final GraphLocation graphLocation) {
    new Expectations() {{
      graphUser.getId(); result = "fb-id-1";
      graphUser.getFirstName(); result = "Bozaglo";
      graphUser.getLastName(); result = "Blat";
      graphUser.getLocation(); result = graphPlace;
      graphPlace.getLocation(); result = graphLocation;
      graphLocation.getStreet(); result = "Iban Gvirol";
      graphLocation.getCity(); result = "Tel Aviv";

      fbResponse.getError(); result = null;

      Request.newMeRequest(fbSession, (Request.GraphUserCallback) any);
      result = new Delegate<Request>() {
        Request delegate(Session session, Request.GraphUserCallback callback) {
          fbCallback = callback;
          return anyFbRequest;
        }
      };

      anyFbRequest.executeAsync();
      result = new Delegate<RequestAsyncTask>() {
        RequestAsyncTask delegate() {
          fbCallback.onCompleted(graphUser, fbResponse);
          return null;
        }
      };
    }};

    Task<User> task = subject.connectWithFacebook(fbSession);

    User user = task.getResult();
    Assert.assertEquals("fb-id-1", user.getUid());
    Assert.assertEquals("Bozaglo", user.getFirstName());
    Assert.assertEquals("Blat", user.getLastName());
    Assert.assertEquals("Iban Gvirol", user.getStreet());
    Assert.assertEquals("Tel Aviv", user.getCity());
    Assert.assertEquals(user, SharedUserConnector.getInstance().getCurrentUser());
  }

}
