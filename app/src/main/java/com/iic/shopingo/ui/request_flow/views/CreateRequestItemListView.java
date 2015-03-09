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
public class CreateRequestItemListView extends LinearLayout implements CreateRequestListItemView.OnListItemChanged {
  private List<String> items = new ArrayList<>();

  private List<CreateRequestListItemView> views = new ArrayList<>();

  private OnRequestItemListChanged listener;

  public CreateRequestItemListView(Context context) {
    super(context);
  }

  public CreateRequestItemListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public CreateRequestItemListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setListener(OnRequestItemListChanged listener) {
    this.listener = listener;
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
    notifyListener();
  }

  public void addAllItems(List<String> titles) {
    for (String title : titles) {
      addItem(title);
    }
  }

  public void setItem(int position, String title) {
    items.set(position, title);
    views.get(position).setTitle(title);
    notifyListener();
  }

  public void removeItem(int position) {
    CreateRequestListItemView view = views.get(position);
    removeView(view);
    items.remove(position);
    views.remove(view);
    notifyListener();
  }

  public List<String> getAllItems() {
    List<String> titles = new ArrayList<>();
    for (CreateRequestListItemView view : views) {
      String title = view.getTitle();
      if (!title.equals("")) {
        titles.add(title);
      }
    }
    return titles;
  }

  private CreateRequestListItemView createListItem(String title) {
    CreateRequestListItemView itemView = CreateRequestListItemView.inflate(this);
    itemView.setTitle(title);
    itemView.setListener(this);
    return itemView;
  }

  private void notifyListener() {
    if (listener != null) {
      listener.onItemListChanged();
    }
  }

  @Override
  public void onRemoveButtonClicked(View view) {
    removeItem(getPositionForView(view));
  }

  @Override
  public void onItemEdited(View view, String value) {
    int position = getPositionForView(view);
    if (value.equals("")) {
      if (position != size() - 1) {
        removeItem(position);
      }
    } else {
      setItem(position, value);
      if (position == size() - 1) {
        addItem("");
        views.get(position + 1).focus();
      }
    }
  }

  public interface OnRequestItemListChanged {
    public void onItemListChanged();
  }
}
