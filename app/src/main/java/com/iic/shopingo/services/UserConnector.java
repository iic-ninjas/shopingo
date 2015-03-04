package com.iic.shopingo.services;

import android.content.Context;
import android.content.SharedPreferences;
import bolts.Task;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.iic.shopingo.dal.models.User;

/**
 * Created by ifeins on 3/3/15.
 */
public class UserConnector {

  private static final String CURRENT_USER_UID_KEY = "current_user_uid";
  private static final String CURRENT_USER_FIRST_NAME_KEY = "current_user_first_name";
  private static final String CURRENT_USER_LAST_NAME_KEY = "current_user_last_name";
  private static final String CURRENT_USER_STREET_KEY = "current_user_street";
  private static final String CURRENT_USER_CITY_KEY = "current_user_city";
  private static final String CURRENT_USER_PHONE_KEY = "current_user_phone";

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
        String street = null;
        String city = null;
        GraphPlace place = graphUser.getLocation();
        GraphLocation location = (place != null ? place.getLocation() : null);
        if (location != null) {
          street = location.getStreet();
          city = location.getCity();
        }

        User user = new User(graphUser.getId(), graphUser.getFirstName(), graphUser.getLastName(), street, city, null);

        // TODO: Make a call create/fetch the user from the server
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

  public User getCurrentUser() {
    if (currentUser == null) {
      String uid = sharedPreferences.getString(CURRENT_USER_UID_KEY, null);
      if (uid != null) {
        // TODO: fetch from local db
        String firstName = sharedPreferences.getString(CURRENT_USER_FIRST_NAME_KEY, null);
        String lastName = sharedPreferences.getString(CURRENT_USER_LAST_NAME_KEY, null);
        String street = sharedPreferences.getString(CURRENT_USER_STREET_KEY, null);
        String city = sharedPreferences.getString(CURRENT_USER_CITY_KEY, null);
        String phone = sharedPreferences.getString(CURRENT_USER_PHONE_KEY, null);
        currentUser = new User(uid, firstName, lastName, street, city, phone);
      }
    }

    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;

    SharedPreferences.Editor editor = sharedPreferences.edit();
    if (currentUser != null) {
      editor.putString(CURRENT_USER_UID_KEY, currentUser.getUid());
      editor.putString(CURRENT_USER_FIRST_NAME_KEY, currentUser.getFirstName());
      editor.putString(CURRENT_USER_LAST_NAME_KEY, currentUser.getLastName());
      editor.putString(CURRENT_USER_STREET_KEY, currentUser.getStreet());
      editor.putString(CURRENT_USER_CITY_KEY, currentUser.getCity());
      editor.putString(CURRENT_USER_PHONE_KEY, currentUser.getPhoneNumber());
    } else {
      editor.remove(CURRENT_USER_UID_KEY);
      editor.remove(CURRENT_USER_FIRST_NAME_KEY);
      editor.remove(CURRENT_USER_LAST_NAME_KEY);
      editor.remove(CURRENT_USER_STREET_KEY);
      editor.remove(CURRENT_USER_CITY_KEY);
      editor.remove(CURRENT_USER_PHONE_KEY);
    }
    editor.apply();
  }
}
