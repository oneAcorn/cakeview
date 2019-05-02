package com.acorn.library.drawable;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateInterpolator;

import com.acorn.library.entry.PieEntry;
import com.acorn.library.utils.CircleUtil;

/**
 * 扇形
 */
public abstract class BaseSectorDrawable<T extends PieEntry> extends Drawable {
    //圆心
    private int originCx, originCy;
    private boolean isHighlighting;
    protected T mPieEntry;

    public BaseSectorDrawable(T pieEntry) {
        this.mPieEntry = pieEntry;
    }

    /**
     * 按下扇形区,进入高亮状态isHighlighting=true
     */
    public abstract void press();

    /**
     * 收回扇形区,退出高亮状态isHighlighting=false
     */
    public abstract void unPress();

    /**
     * 设置是否在高亮状态
     *
     * @param isHighlighting
     */
    protected void setHighlighting(boolean isHighlighting) {
        this.isHighlighting = isHighlighting;
    }

    public boolean isHighlighting() {
        return isHighlighting;
    }

    public void setPieEntry(T pieEntry) {
        mPieEntry = pieEntry;
    }

    public T getPieEntry() {
        return mPieEntry;
    }

    public int getOriginCx() {
        return originCx;
    }

    public int getOriginCy() {
        return originCy;
    }

    /**
     * 触摸区域是否在此扇形区内
     *
     * @param clickX
     * @param clickY
     * @return
     */
    public abstract boolean containAngle(float clickX, float clickY);

    public abstract void offsetAngle(float offsetAngle);

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        originCx = (int) (bounds.width() / 2f);
        originCy = (int) (bounds.height() / 2f);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
