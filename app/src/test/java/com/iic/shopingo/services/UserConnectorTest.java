package com.iic.shopingo.services;

import android.content.Context;
import android.content.SharedPreferences;
import bolts.Task;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.iic.shopingo.dal.models.User;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
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

    // we want this behavior to occur in all the tests that exercise the Request.newMeRequest static method.
    // Basically what we want it do is to return a mocked instance of Request and store the callback in
    // a member variable, so we can later call methods on it.
    new NonStrictExpectations() {{
      Request.newMeRequest(fbSession, (Request.GraphUserCallback) any);
      result = new Delegate<Request>() {
        Request delegate(Session session, Request.GraphUserCallback callback) {
          fbCallback = callback;
          return anyFbRequest;
        }
      };
    }};
  }

  @After
  public void afterEach() {
    SharedUserConnector.setInstance(null);
  }

  @Test
  public void testConnectWithFacebookWhenLoginFails() {
    new Expectations() {{
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
  public void testConnectWithFacebookWhenLoginSucceeds(@Injectable final GraphUser graphUser) {
    new Expectations() {{
      graphUser.getId(); result = "fb-id-1";
      graphUser.getFirstName(); result = "Bozaglo";
      graphUser.getLastName(); result = "Blat";
      // graphUser.getLocation() returns a cascaded mock, and so does the other chained methods.
      graphUser.getLocation().getLocation().getStreet(); result = "Iban Gvirol";
      graphUser.getLocation().getLocation().getCity(); result = "Tel Aviv";

      fbResponse.getError(); result = null;

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

  @Test
  public void testLogout(@Injectable Context context, @Mocked final Session anySession) {
    User user = new User("1", "Bozaglo", "Blat", "Iban Gvirol", "Tel Aviv", "054-1234567");
    subject.setCurrentUser(user);
    new Expectations() {{
      Session.getActiveSession();
      result = anySession;
    }};

    subject.logout(context);

    new Verifications() {{
      anySession.closeAndClearTokenInformation();
    }};
    Assert.assertNull(subject.getCurrentUser());
  }
  
}
