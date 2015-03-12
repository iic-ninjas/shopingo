package com.iic.shopingo.services;

import android.content.Context;
import android.content.SharedPreferences;
import bolts.Task;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.iic.shopingo.dal.models.UserInfo;

/**
 * Created by ifeins on 3/3/15.
 */
public class FacebookConnector {
  public static Task<UserInfo> connectWithFacebook(Session session) {
    final Task<UserInfo>.TaskCompletionSource taskCompletionSource = Task.create();
    Request.newMeRequest(session, new Request.GraphUserCallback() {
      @Override
      public void onCompleted(GraphUser graphUser, Response response) {
        if (response.getError() != null) {
          taskCompletionSource.setError(new UserConnectorException(response.getError()));
          return;
        }

        String street = null;
        String city = null;
        GraphPlace place = graphUser.getLocation();
        GraphLocation location = (place != null ? place.getLocation() : null);
        if (location != null) {
          street = location.getStreet();
          city = location.getCity();
        }

        UserInfo user = new UserInfo(graphUser.getId(), graphUser.getFirstName(), graphUser.getLastName(), street, city, null);

        taskCompletionSource.setResult(user);
      }
    }).executeAsync();
    return taskCompletionSource.getTask();
  }

  public static void logout(Context context) {
    // Just because getActiveSession returns null, doesn't mean there is no session.
    // It only means that it's not cached, so we need to create the session to close and clear its token information.
    Session session = Session.getActiveSession();
    if (session == null) {
      session = new Session(context);
      Session.setActiveSession(session);
    }
    session.closeAndClearTokenInformation();
  }

  public static class UserConnectorException extends Exception {

    private FacebookRequestError fbError;

    public UserConnectorException(FacebookRequestError fbError) {
      super(fbError.getErrorMessage());
      this.fbError = fbError;
    }

    public FacebookRequestError getFbError() {
      return fbError;
    }
  }
}
