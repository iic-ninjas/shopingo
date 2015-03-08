package com.iic.shopingo.ui.request_flow.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import com.iic.shopingo.R;

/**
 * Created by assafgelber on 3/4/15.
 */
public class CreateRequestListItemView extends LinearLayout {
  @InjectView(R.id.create_request_item_title)
  EditText titleView;

  @InjectView(R.id.create_request_remove_button)
  Button removeButton;

  private OnListViewChanged listener;

  public CreateRequestListItemView(Context context) {
    super(context);
    init();
  }

  public CreateRequestListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public CreateRequestListItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public static CreateRequestListItemView inflate(ViewGroup parent) {
    CreateRequestListItemView itemView = (CreateRequestListItemView) LayoutInflater.from(parent.getContext())
        .inflate(R.layout.create_request_list_item, parent, false);
    return itemView;
  }

  private void init() {
    LayoutInflater.from(getContext()).inflate(R.layout.create_request_list_item_children, this, true);
    ButterKnife.inject(this);
  }

  public void setTitle(String title) {
    titleView.setText(title);
  }

  public String getTitle() {
    return titleView.getText().toString();
  }

  public void focus() {
    if (titleView.requestFocus()) {
      ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
  }

  public void setRemoveEnabled(boolean enabled) {
    removeButton.setEnabled(enabled);
  }

  public void setListener(OnListViewChanged listener) {
    this.listener = listener;
  }

  @OnClick(R.id.create_request_remove_button)
  public void onRemoveClick(View view) {
    if (listener != null) {
      listener.onRemoveButtonClicked(this);
    }
  }

  @OnFocusChange(R.id.create_request_item_title)
  public void onTitleFocusChanged(EditText view, boolean hasFocus) {
    if (!hasFocus) {
      if (listener != null) {
        listener.onItemEdited(this, view.getText().toString());
    }
    }
  }

  @OnEditorAction(R.id.create_request_item_title)
  public boolean onTitleEditorAction(EditText view, int actionId, KeyEvent event) {
    if (actionId == EditorInfo.IME_ACTION_GO) {
      if (listener != null) {
        listener.onItemEdited(this, view.getText().toString());
      }
      return true;
    }
    return false;
  }

  public interface OnListViewChanged {
    public void onItemEdited(View view, String value);

    public void onRemoveButtonClicked(View view);
  }
}

