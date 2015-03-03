package com.iic.shopingo.ui.request_flow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.iic.shopingo.ui.request_flow.Views.SelectShopperListItemView;
import java.util.ArrayList;

/**
 * Created by assafgelber on 3/3/15.
 */
public class SelectShopperAdapter extends ArrayAdapter<SelectShopperAdapter.User> {
  public SelectShopperAdapter(Context context, ArrayList<User> shoppers) {
    super(context, 0, shoppers);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    SelectShopperListItemView itemView = (SelectShopperListItemView)convertView;
    if (itemView == null) {
      itemView = SelectShopperListItemView.inflate(parent);
    }
    itemView.setUser(getItem(position));
    return itemView;
  }

  // TODO: Move actual model
  public static class User {
    public String photo;
    public String name;
    public Long latitude;
    public Long longitude;

    public User(String photo, String name, Long latitude, Long longitude) {
      this.photo = photo;
      this.name = name;
      this.latitude = latitude;
      this.longitude = longitude;
    }
  }
}
