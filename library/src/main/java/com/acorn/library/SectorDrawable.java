package com.acorn.library;

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

/**
 * 扇形
 */
public class SectorDrawable extends Drawable {
    private static final int DEFAULT_COLOR = 0xFFB4B3B2;
    //默认扇形区占比(给弹出动画预留空间)
    private static final float DEFAULT_SECTOR_RADIUS_RATE = 0.45f;
    //高亮动画突出距离比0~1
    private static final float DEFAULT_HIGHLIGHT_DISTANCE_RATE = 0.9f;
    private RectF mRectF;
    private Paint mPaint;
    //圆心
    private int cx, cy;
    private int originCx, originCy;
    //半径
    private int radius;
    private ValueAnimator pressAnim;
    private boolean isHighlighting;

    private PieEntry mPieEntry;

    public SectorDrawable() {
        this(DEFAULT_COLOR);
    }

    public SectorDrawable(int color) {
        super();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);  //抗锯齿
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        if (null != mRectF && null != mPieEntry) {
            mPaint.setColor(mPieEntry.getColor());
            canvas.drawArc(mRectF, mPieEntry.getStartAngle(), mPieEntry.getSweepAngle(), true, mPaint);
        }
    }

    public void press() {
        if (null == pressAnim)
            initPressAnim();
        if (!pressAnim.isStarted())
            pressAnim.start();
    }

    public void unPress() {
        if (null == pressAnim)
            initPressAnim();
        if (pressAnim.isRunning())
            pressAnim.cancel();
        pressAnim.reverse();
    }

    public boolean isHighlighting() {
        if (null != pressAnim && pressAnim.isRunning())
            return false;
        return cx != originCx && cy != originCy;
    }

    private void initPressAnim() {
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cx = ((Number) animation.getAnimatedValue("cx")).intValue();
                cy = ((Number) animation.getAnimatedValue("cy")).intValue();
                computeRectF(cx, cy);
                invalidateSelf();
            }
        };
        PointF toPointF = CircleUtil.getPositionByAngle(mPieEntry.getStartAngle() + mPieEntry.getSweepAngle() / 2,
                (int) (radius * (0.5 - DEFAULT_SECTOR_RADIUS_RATE) * DEFAULT_HIGHLIGHT_DISTANCE_RATE),
                originCx, originCy);
        PropertyValuesHolder cxHolder = PropertyValuesHolder.ofInt("cx", originCx, (int) toPointF.x);
        PropertyValuesHolder cyHolder = PropertyValuesHolder.ofInt("cy", originCy, (int) toPointF.y);
        pressAnim = ValueAnimator.ofPropertyValuesHolder(cxHolder, cyHolder);

        pressAnim.setInterpolator(new AccelerateInterpolator());
        pressAnim.addUpdateListener(listener);
        pressAnim.setDuration(300);
    }

    public void setPieEntry(PieEntry pieEntry) {
        mPieEntry = pieEntry;
        invalidateSelf();
    }

    public PieEntry getPieEntry() {
        return mPieEntry;
    }

    public boolean containAngle(float clickX, float clickY) {
        float clickAngle = CircleUtil.getAngleByPosition(clickX, clickY, cx, cy, radius);
        return mPieEntry != null && (Float.compare(clickAngle, mPieEntry.getStartAngle()) == 0 ||
                Float.compare(clickAngle, mPieEntry.getStartAngle()) == 1) &&
                Float.compare(clickAngle, mPieEntry.getStartAngle() + mPieEntry.getSweepAngle()) == -1;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        originCx = cx = (int) (bounds.width() / 2f);
        originCy = cy = (int) (bounds.height() / 2f);
        float size = Math.min(bounds.width(), bounds.height());
        radius = (int) (size * DEFAULT_SECTOR_RADIUS_RATE);
        computeRectF(cx, cy);
    }

    private void computeRectF(int cx, int cy) {
        mRectF = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
