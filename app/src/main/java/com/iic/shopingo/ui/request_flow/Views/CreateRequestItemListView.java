package com.iic.shopingo.ui.request_flow.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by assafgelber on 3/4/15.
 */
public class CreateRequestItemListView extends LinearLayout {
  private List<String> items = new ArrayList<>();

  private List<CreateRequestListItemView> views = new ArrayList<>();

  public CreateRequestItemListView(Context context) {
    super(context);
  }

  public CreateRequestItemListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public CreateRequestItemListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public int size() {
    return items.size();
  }

  public int getPositionForView(View view) {
    return views.indexOf(view);
  }

  @Override
  public View getChildAt(int index) {
    return views.get(index);
  }

  public void addItem(String title) {
    CreateRequestListItemView view = createListItem(title);
    if (size() > 0) {
      views.get(size() - 1).setRemoveEnabled(true);
    }
    view.setRemoveEnabled(false);
    addView(view);
    items.add(title);
    views.add(view);
  }

  public void addAllItems(List<String> titles) {
    for (int i = 0; i < titles.size(); i++) {
      addItem(titles.get(i));
    }
  }

  public void setItem(int position, String title) {
    items.set(position, title);
    views.get(position).setTitle(title);
  }

  public void removeItem(int position) {
    CreateRequestListItemView view = views.get(position);
    removeView(view);
    items.remove(position);
    views.remove(view);
  }

  public List<String> getAllItems() {
    List<String> titles = new ArrayList<>();
    for (int i = 0; i < views.size(); i++) {
      String title = views.get(i).getTitle();
      if (!title.equals("")) {
        titles.add(title);
      }
    }
    return titles;
  }

  private CreateRequestListItemView createListItem(String title) {
    CreateRequestListItemView itemView = CreateRequestListItemView.inflate(this);
    itemView.setTitle(title);
    return itemView;
  }
}
