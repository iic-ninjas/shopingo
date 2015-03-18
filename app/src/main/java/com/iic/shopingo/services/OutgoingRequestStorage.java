package com.iic.shopingo.services;

import android.content.SharedPreferences;
import android.util.Log;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.OutgoingRequest;
import com.iic.shopingo.dal.models.ShoppingList;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by asafg on 15/03/15.
 */
public class OutgoingRequestStorage {

  private final static String OR_ID = "or_id";
  private final static String OR_SHOPPING_LIST = "or_shopping_list";
  private final static String OR_SHOPPING_OFFER = "or_shopping_offer";
  private final static String OR_STATUS = "or_status";
  private final static String OR_SHOPPER_ID = "or_shopper_id";
  private final static String OR_SHOPPER_FIRST_NAME = "or_shopper_first_name";
  private final static String OR_SHOPPER_LAST_NAME = "or_shopper_last_name";
  private final static String OR_SHOPPER_PHONE_NUMBER = "or_shopper_phone_number";
  private final static String OR_SHOPPER_STREET_ADDRESS = "or_shopper_street_address";
  private final static String OR_SHOPPER_CITY = "or_shopper_city";
  private final static String OR_SHOPPER_LAT = "or_shopper_lat";
  private final static String OR_SHOPPER_LON = "or_shopper_lon";

  private static final String TAG = OutgoingRequestStorage.class.getSimpleName();

  private final SharedPreferences prefs;

  public OutgoingRequestStorage(SharedPreferences prefs) {
    this.prefs = prefs;
  }

  public void store(OutgoingRequest req) {
    if (req != null) {
      SharedPreferences.Editor editor = this.prefs.edit();
      JSONArray array = new JSONArray(req.getShoppingList().getItems());
      editor.putString(OR_ID, req.getId());
      editor.putString(OR_SHOPPING_LIST, array.toString());
      editor.putInt(OR_SHOPPING_OFFER, req.getShoppingList().getOffer());
      editor.putInt(OR_STATUS, req.getStatus().ordinal());
      editor.putString(OR_SHOPPER_ID, req.getShopper().getId());
      editor.putString(OR_SHOPPER_FIRST_NAME, req.getShopper().getFirstName());
      editor.putString(OR_SHOPPER_LAST_NAME, req.getShopper().getLastName());
      editor.putString(OR_SHOPPER_PHONE_NUMBER, req.getShopper().getPhoneNumber());
      editor.putString(OR_SHOPPER_STREET_ADDRESS, req.getShopper().getStreetAddress());
      editor.putString(OR_SHOPPER_CITY, req.getShopper().getCity());
      editor.putFloat(OR_SHOPPER_LAT, (float) req.getShopper().getLatitude());
      editor.putFloat(OR_SHOPPER_LON, (float) req.getShopper().getLongitude());
      editor.apply();
    } else {
      clear();
    }
  }

  public OutgoingRequest load() {
    String id = prefs.getString(OR_ID, null);
    int status = prefs.getInt(OR_STATUS, -1);
    if (id != null && status > -1) {
      OutgoingRequest req = new OutgoingRequest(id);
      req.setStatus(BaseRequest.RequestStatus.values()[status]);
      req.setShopper(new Contact(
          prefs.getString(OR_SHOPPER_ID, null),
          prefs.getString(OR_SHOPPER_FIRST_NAME, null),
          prefs.getString(OR_SHOPPER_LAST_NAME, null),
          prefs.getString(OR_SHOPPER_PHONE_NUMBER, null),
          prefs.getString(OR_SHOPPER_STREET_ADDRESS, null),
          prefs.getString(OR_SHOPPER_CITY, null),
          prefs.getFloat(OR_SHOPPER_LAT, 0.0f),
          prefs.getFloat(OR_SHOPPER_LON, 0.0f)
      ));

      String shoppingListItems = prefs.getString(OR_SHOPPING_LIST, null);
      int offer = prefs.getInt(OR_SHOPPING_OFFER, -1);
      if (shoppingListItems != null && offer > -1) {
        try {
          JSONArray array = new JSONArray(shoppingListItems);
          List<String> items = new ArrayList<>(array.length());
          for (int i = 0; i < array.length(); ++i) {
            items.add(array.getString(i));
          }
          req.setShoppingList(new ShoppingList(items, offer));
        } catch (JSONException ex) {
          Log.e(TAG, "Can't parse items list json", ex);
        }
      } else {
        req.setShoppingList(new ShoppingList());
      }
      return req;
    } else {
      return null;
    }
  }

  public void clear() {
    SharedPreferences.Editor editor = this.prefs.edit();
    editor.remove(OR_SHOPPING_LIST);
    editor.remove(OR_SHOPPING_OFFER);
    editor.remove(OR_STATUS);
    editor.remove(OR_SHOPPER_ID);
    editor.remove(OR_SHOPPER_FIRST_NAME);
    editor.remove(OR_SHOPPER_LAST_NAME);
    editor.remove(OR_SHOPPER_PHONE_NUMBER);
    editor.remove(OR_SHOPPER_STREET_ADDRESS);
    editor.remove(OR_SHOPPER_CITY);
    editor.remove(OR_SHOPPER_LAT);
    editor.remove(OR_SHOPPER_LON);
    editor.apply();
  }
}
