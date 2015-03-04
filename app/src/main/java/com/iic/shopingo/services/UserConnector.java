package com.iic.shopingo.services;

import android.content.SharedPreferences;
import bolts.Task;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphUser;
import com.iic.shopingo.dal.models.User;

/**
 * Created by ifeins on 3/3/15.
 */
public class UserConnector {

  private static final String CURRENT_USER_UID_KEY = "current_user_uid";

  private User currentUser;

  private SharedPreferences sharedPreferences;

  public UserConnector(SharedPreferences sharedPreferences) {
    this.sharedPreferences = sharedPreferences;
  }

  public Task<User> connectWithFacebook(Session session) {
    final Task<User>.TaskCompletionSource taskCompletionSource = Task.create();

    Request.newMeRequest(session, new Request.GraphUserCallback() {
      @Override
      public void onCompleted(GraphUser graphUser, Response response) {
        GraphLocation location = graphUser.getLocation().getLocation();

        User user = new User(graphUser.getId(), graphUser.getFirstName(), graphUser.getLastName(), location.getStreet(),
            location.getCountry(), null);

        // TODO: Make a call create/fetch the user from the server
        SharedUserConnector.getInstance().setCurrentUser(user);
        taskCompletionSource.setResult(user);
      }
    }).executeAsync();

    return taskCompletionSource.getTask();
  }

  public boolean isUserSignedIn() {
    return getCurrentUser() != null;
  }

  public User getCurrentUser() {
    if (currentUser == null) {
      String uid = sharedPreferences.getString(CURRENT_USER_UID_KEY, null);
      // TODO: fetch user from local db
    }

    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(CURRENT_USER_UID_KEY, currentUser.getUid());
    editor.apply();
  }
}
