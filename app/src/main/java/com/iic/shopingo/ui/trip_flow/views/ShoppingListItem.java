package com.iic.shopingo.ui.trip_flow.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.R;
import com.iic.shopingo.ui.trip_flow.activities.ShoppingListActivity;
import com.iic.shopingo.ui.trip_flow.data.ShoppingList;

/**
 * Created by asafg on 04/03/15.
 */
public class ShoppingListItem extends FrameLayout {

  public interface OnCallListener {
    public void onCall(String phoneNumber);
  }

  @InjectView(R.id.shopping_list_header_item_requester_name)
  TextView requesterName;

  @InjectView(R.id.shopping_list_item_items_container)
  LinearLayout itemsContainer;

  private OnCallListener listener;

  private ShoppingList shoppingList;

  public ShoppingListItem(Context context) {
    super(context);
    init();
  }

  public ShoppingListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ShoppingListItem(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void setOnCallListener(OnCallListener listener) {
    this.listener = listener;
  }

  public void setShoppingList(ShoppingList shoppingList) {
    this.shoppingList = shoppingList;
    requesterName.setText(shoppingList.requesterName);
    itemsContainer.removeAllViews();
    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    for (ShoppingList.Item item : shoppingList.items) {
      View shoppingListItemView = createShoppingListItemView(item);
      itemsContainer.addView(shoppingListItemView, lp);
      shoppingListItemView.setOnClickListener(new ShoppingListClickListener(item) {
        @Override
        public void onClick(View v, ShoppingList.Item item) {
          listItemClicked((CheckBox)v, item);
        }
      });
    }
  }

  private void listItemClicked(CheckBox view, ShoppingList.Item item) {
    item.checked = !item.checked;
    view.setChecked(item.checked);
  }

  private View createShoppingListItemView(ShoppingList.Item item) {
    CheckBox cb = new CheckBox(getContext());
    cb.setText(item.title);
    cb.setChecked(item.checked);
    return cb;
  }

  private void init() {
    LayoutInflater inflater = LayoutInflater.from(getContext());

    inflater.inflate(R.layout.shopping_list_item, this, true);
    ButterKnife.inject(this);
  }

  @OnClick(R.id.shopping_list_header_item_call_button)
  public void onCallClick(View view) {
    if (listener != null) {
      listener.onCall(shoppingList.phoneNumber);
    }
  }

  private abstract class ShoppingListClickListener implements OnClickListener {
    private ShoppingList.Item item;

    public ShoppingListClickListener(ShoppingList.Item item) {
      this.item = item;
    }

    @Override
    public void onClick(View v) {
      onClick(v, this.item);
    }

    public abstract void onClick(View v, ShoppingList.Item item);
  }
}
