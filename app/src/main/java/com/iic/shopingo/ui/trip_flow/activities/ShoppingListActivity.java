package com.iic.shopingo.ui.trip_flow.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.iic.shopingo.ui.trip_flow.data.ShoppingList;
import com.iic.shopingo.ui.trip_flow.views.ShoppingListItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 04/03/15.
 */
public class ShoppingListActivity extends Activity implements ShoppingListItem.OnCallListener {
  private ListView list;
  private ShoppingListAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    list = new ListView(this);
    setContentView(list);

    List<ShoppingList> lists = new ArrayList<>(5);
    for (int i = 0; i < 5; ++i) {
      ShoppingList list = new ShoppingList();
      list.requesterName = "Moshe " + i;
      list.phoneNumber = "12345678";
      list.items.add(new ShoppingList.Item("1 Milk"));
      list.items.add(new ShoppingList.Item("1 Bread"));
      list.items.add(new ShoppingList.Item("1 Cheese"));
      list.items.add(new ShoppingList.Item("12 Eggs"));
      lists.add(list);
    }

    adapter = new ShoppingListAdapter(this, lists);
    adapter.setOnCallListener(this);
    list.setAdapter(adapter);
  }

  @Override
  public void onCall(String phoneNumber) {
    Intent intent = new Intent(Intent.ACTION_CALL);
    intent.setData(Uri.parse("tel:" + phoneNumber));
    startActivity(intent);
  }

  class ShoppingListAdapter extends BaseAdapter implements ShoppingListItem.OnCallListener {
    private Context context;
    private List<ShoppingList> shoppingLists;

    private ShoppingListItem.OnCallListener listener;

    public ShoppingListAdapter(Context context, List<ShoppingList> shoppingLists) {
      this.shoppingLists = shoppingLists;
      this.context = context;
    }

    public void setOnCallListener(ShoppingListItem.OnCallListener listener) {
      this.listener = listener;
    }

    @Override
    public int getCount() {
      return shoppingLists.size();
    }

    @Override
    public ShoppingList getItem(int position) {
      return shoppingLists.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ShoppingListItem itemView;
      if (convertView != null) {
        itemView = (ShoppingListItem)convertView;
      } else {
        itemView = new ShoppingListItem(context);
        itemView.setOnCallListener(this);
      }
      itemView.setShoppingList(getItem(position));
      return itemView;
    }

    @Override
    public void onCall(String phoneNumber) {
      if (listener != null) {
        listener.onCall(phoneNumber);
      }
    }
  }
}
