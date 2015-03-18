package com.iic.shopingo.ui.onboarding.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.iic.shopingo.R;

/**
 * Created by assafgelber on 3/18/15.
 */
public class PagerIndicatorView extends View {
  private float position;

  private int pages = 0;

  public PagerIndicatorView(Context context) {
    super(context);
  }

  public PagerIndicatorView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
gaa
  public PagerIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    float verticalMiddle = canvas.getClipBounds().height() / 2;

    int maxAlpha = 255;
    int minAlpha = maxAlpha / 3;

    float maxPointDiameter = canvas.getClipBounds().height();
    float minPointDiameter = maxPointDiameter / 2;
    float pointMargin = maxPointDiameter / 4;
    float indicatorWidth = (maxPointDiameter * pages + pointMargin * (pages - 1));
    int beforePadding = (int) ((canvas.getClipBounds().width() - indicatorWidth) / 2 + maxPointDiameter / 2);

    Paint paint = new Paint();
    paint.setColor(getResources().getColor(R.color.gopherColor));
    paint.setStrokeWidth(maxPointDiameter);
    paint.setStrokeCap(Paint.Cap.ROUND);

    for (int i = 0; i < pages; i++) {
      float transformationValue = 1 - Math.min(Math.abs(position - i), 1);
      paint.setAlpha((int) ((maxAlpha - minAlpha) * transformationValue + minAlpha));
      paint.setStrokeWidth((int) ((maxPointDiameter - minPointDiameter) * transformationValue + minPointDiameter));

      canvas.drawPoint(beforePadding + (int)((maxPointDiameter + pointMargin) * i), verticalMiddle, paint);
    }
  }

  public void setPages(int pages) {
    this.pages = pages;
    postInvalidate();
  }

  public void setPosition(float position) {
    this.position = position;
    postInvalidate();
  }
}
