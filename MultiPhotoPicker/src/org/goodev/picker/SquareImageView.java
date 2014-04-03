package org.goodev.picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

public class SquareImageView extends ImageView implements Checkable{

  static Drawable sCheckedDrawable;
  public SquareImageView(Context context) {
    super(context);
    sCheckedDrawable = context.getResources().getDrawable(R.drawable.grid_selected);
  }

  public SquareImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    sCheckedDrawable = context.getResources().getDrawable(R.drawable.grid_selected);
  }

  public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    sCheckedDrawable = context.getResources().getDrawable(R.drawable.grid_selected);
  }

  @SuppressWarnings("unused")
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      // For simple implementation, or internal size is always 0.
      // We depend on the container to specify the layout size of
      // our view. We can't really know what it is since we will be
      // adding and removing different arbitrary views and do not
      // want the layout to change as this happens.
      setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

      // Children are just made to fill our space.
      int childWidthSize = getMeasuredWidth();
      int childHeightSize = getMeasuredHeight();
      //高度和宽度一样
      heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  private boolean mChecked;
  private Drawable mDrawable;
  @SuppressWarnings("deprecation")
  @Override
  public void setChecked(boolean checked) {
    if(checked != mChecked) {
      mChecked = checked;
      mDrawable = checked ? sCheckedDrawable : null;
      setBackgroundDrawable(mDrawable);
    }
  }
  
  
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if(mDrawable != null) {
      mDrawable.draw(canvas);
    }
  }

  @Override
  public boolean isChecked() {
    return mChecked;
  }

  @Override
  public void toggle() {
    mChecked = !mChecked;
  }
}
