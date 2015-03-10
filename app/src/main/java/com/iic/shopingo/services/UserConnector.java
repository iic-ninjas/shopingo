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
public class UserConnector {
  private UserInfo currentUser;
  private UserStorage userStorage;

  public UserConnector(SharedPreferences sharedPreferences) {
    userStorage = new UserStorage(sharedPreferences);
  }

  public Task<UserInfo> connectWithFacebook(Session session) {
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

        UserInfo
            user = new UserInfo(graphUser.getId(), graphUser.getFirstName(), graphUser.getLastName(), street, city, null);

        // TODO: Make a call to create/fetch the user from the server
        SharedUserConnector.getInstance().setCurrentUser(user);
        taskCompletionSource.setResult(user);
      }
    }).executeAsync();
    return taskCompletionSource.getTask();
  }

  public void logout(Context context) {
    setCurrentUser(null);

    // Just because getActiveSession returns null, doesn't mean there is no session.
    // It only means that it's not cached, so we need to create the session to close and clear its token information.
    Session session = Session.getActiveSession();
    if (session == null) {
      session = new Session(context);
      Session.setActiveSession(session);
    }
    session.closeAndClearTokenInformation();
  }

  public boolean isUserSignedIn() {
    return getCurrentUser() != null;
  }

  public UserInfo getCurrentUser() {
    if (currentUser == null) {
      currentUser = userStorage.getUserInfo();
    }

    return currentUser;
  }

  public void setCurrentUser(UserInfo currentUser) {
    this.currentUser = currentUser;
    userStorage.storeUserInfo(currentUser);
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
