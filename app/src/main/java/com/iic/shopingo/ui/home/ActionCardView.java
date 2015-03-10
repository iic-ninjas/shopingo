package com.iic.shopingo.ui.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.R;

/**
 * Created by ifeins on 3/10/15.
 */
public class ActionCardView extends LinearLayout {

  private Listener listener;

  @InjectView(R.id.action_card_description)
  TextView descriptionTextView;

  @InjectView(R.id.action_card_button)
  Button actionButton;

  @InjectView(R.id.action_card_icon)
  ImageView icon;

  public ActionCardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public ActionCardView(Context context) {
    super(context);
    init(null);
  }

  public ActionCardView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ActionCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(attrs);
  }

  public static ActionCardView inflate(ViewGroup parent) {
    return (ActionCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_action_card, parent, false);
  }

  private void init(AttributeSet attrs) {
    LayoutInflater.from(getContext()).inflate(R.layout.view_action_card_children, this, true);
    ButterKnife.inject(this);

    if (attrs == null) return;

    TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ActionCardView, 0, 0);
    try {
      descriptionTextView.setText(typedArray.getString(R.styleable.ActionCardView_cardDescription));
      actionButton.setText(typedArray.getString(R.styleable.ActionCardView_cardButtonText));
      int resourceId = typedArray.getResourceId(R.styleable.ActionCardView_cardIcon, -1);
      if (resourceId != -1) {
        icon.setImageResource(resourceId);
      }
    } finally {
      typedArray.recycle();
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);

    int[] position = new int[2];
    actionButton.getLocationOnScreen(position);
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @OnClick(R.id.action_card_button)
  public void onClick(View target) {
    if (listener != null) {
      listener.onCardClicked(this);
    }
  }

  public interface Listener {
    public void onCardClicked(ActionCardView cardView);
  }
}
