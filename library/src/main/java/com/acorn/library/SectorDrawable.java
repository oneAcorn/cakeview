package com.acorn.library;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * 扇形
 */
public class SectorDrawable extends Drawable {
    private static final int DEFAULT_COLOR = 0xFFB4B3B2;
    //默认扇形区占比(给弹出动画预留空间)
    private static final float DEFAULT_SECTOR_RADIUS_RATE = 0.45f;
    private RectF mRectF;
    private Paint mPaint;
    //圆心
    private int cx, cy;
    //半径
    private int radius;

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
        cx = (int) (bounds.width() / 2f);
        cy = (int) (bounds.height() / 2f);
        float size = Math.min(bounds.width(), bounds.height());
        radius = (int) (size * DEFAULT_SECTOR_RADIUS_RATE);
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
