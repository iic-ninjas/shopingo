package com.iic.shopingo.ui.request_flow.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iic.shopingo.R;
import com.iic.shopingo.ui.request_flow.Views.SelectShopperListItemView;
import java.util.ArrayList;

public class SelectShopperActivity extends ActionBarActivity {
  @InjectView(R.id.select_shopper_list)
  ListView mShopperList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_shopper);
    ButterKnife.inject(this);

    SelectShopperAdapter adapter = new SelectShopperAdapter(this, new ArrayList<SelectShopperActivity.SelectShopperAdapter.User>());
    mShopperList.setAdapter(adapter);
  }

  public static class SelectShopperAdapter extends ArrayAdapter<SelectShopperActivity.SelectShopperAdapter.User> {
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
}