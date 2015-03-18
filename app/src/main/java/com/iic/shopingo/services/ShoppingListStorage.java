package com.iic.shopingo.services;

import android.content.SharedPreferences;
import android.util.Log;
import com.iic.shopingo.api.request.ShoppersApiResult;
import com.iic.shopingo.dal.models.ShoppingList;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by asafg on 15/03/15.
 */
public class ShoppingListStorage {
  private static final String SHOPPING_LIST_OFFER = "shopping_list_offer";

  private static final String SHOPPING_LIST_ITEMS = "shopping_list_items";

  private static final String TAG = ShoppingListStorage.class.getSimpleName();

  private final SharedPreferences prefs;

  public ShoppingListStorage(SharedPreferences prefs) {
    this.prefs = prefs;
  }

  public void store(ShoppingList shoppingList) {
    if (shoppingList != null) {
      JSONArray array = new JSONArray(shoppingList.getItems());
      SharedPreferences.Editor editor = prefs.edit();
      editor.putInt(SHOPPING_LIST_OFFER, shoppingList.getOffer());
      editor.putString(SHOPPING_LIST_ITEMS, array.toString());
      editor.apply();
    } else {
      clear();
    }
  }

  public ShoppingList load() {
    int offer = prefs.getInt(SHOPPING_LIST_OFFER, -1);
    String itemsString = prefs.getString(SHOPPING_LIST_ITEMS, null);
    if (offer > -1 && itemsString != null) {
      try {
        JSONArray array = new JSONArray(itemsString);
        List<String> items = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); ++i) {
          items.add(array.getString(i));
        }
        return new ShoppingList(items, offer);
      } catch (JSONException ex) {
        Log.e(TAG, "Can't parse items list json", ex);
        return null;
      }
    } else {
      return null;
    }
  }

  public void clear() {
    SharedPreferences.Editor editor = prefs.edit();
    editor.remove(SHOPPING_LIST_OFFER);
    editor.remove(SHOPPING_LIST_ITEMS);
    editor.apply();
  }
}
