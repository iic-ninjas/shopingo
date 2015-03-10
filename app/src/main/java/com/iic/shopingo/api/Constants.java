package com.iic.shopingo.api;

/**
 * Created by assafgelber on 3/10/15.
 */
public class Constants {
  public static class Routes {
    public static final String USERS_LOGIN_PATH = "/users/login";
    public static final String USERS_UPDATE_PATH = "/users/update";

    public static final String TRIPS_INDEX_PATH = "/trips/";
    public static final String TRIPS_START_PATH = "/trips/start";
    public static final String TRIPS_END_PATH = "/trips/end";

    public static final String REQUESTS_INDEX_PATH = "/requests/";
    public static final String REQUESTS_CREATE_PATH = "/requests/create";
    public static final String REQUESTS_CANCEL_PATH = "/requests/cancel";
    public static final String REQUESTS_SETTLE_PATH = "/requests/settle";
    public static final String REQUESTS_ACCEPT_PATH_TEMPLATE = "/requests/%s/accpet";
    public static final String REQUESTS_DECLINE_PATH_TEMPLATE = "/requests/%s/decline";
  }

  public static class Parameters {
    public static final String USERS_FACEBOOK_ID = "facebook_id";
    public static final String USERS_FIRST_NAME = "first_name";
    public static final String USERS_LAST_NAME = "last_name";
    public static final String USERS_STREET_ADDRESS = "street_address";
    public static final String USERS_CITY = "city";
    public static final String USERS_PHONE_NUMBER = "phone_number";

    public static final String REQUESTS_SHOPPER_ID = "shopper_id";
    public static final String REQUESTS_ITEMS = "items";
    public static final String REQUESTS_OFFER = "offer";
  }
}
