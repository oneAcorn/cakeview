package com.acorn.library.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.acorn.library.entry.HollowPieEntry;
import com.acorn.library.utils.CircleUtil;

public class HollowSectorDrawable extends BaseSectorDrawable<HollowPieEntry> {
    private static final float DEFAULT_HOLLOW_LENGTH_RATE = 0.2f;
    //默认扇形区占比(给弹出动画预留空间)
    private static final float DEFAULT_SECTOR_RADIUS_RATE = 0.45f;
    //高亮动画突出距离比0~1
    private static final float DEFAULT_HIGHLIGHT_DISTANCE_RATE = 0.9f;
    //扇形半径占比0f~0.5f
    private float sectorRadiusRate;
    //高亮动画弹出距离占比
    private float highlightDistanceRate;

    private Paint hollowPaint, sectorPaint;
    private RectF hollowRectF;
    //圆心
    private int cx, cy;
    //半径
    private int radius;
    private Path sectorPath;

    public HollowSectorDrawable(HollowPieEntry pieEntry) {
        this(pieEntry, DEFAULT_SECTOR_RADIUS_RATE, DEFAULT_HIGHLIGHT_DISTANCE_RATE);
    }

    public HollowSectorDrawable(HollowPieEntry pieEntry, float sectorRadiusRate, float highlightDistanceRate) {
        super(pieEntry);
        this.sectorRadiusRate = sectorRadiusRate;
        this.highlightDistanceRate = highlightDistanceRate;
        hollowPaint = new Paint();
        hollowPaint.setAntiAlias(true);  //抗锯齿
        hollowPaint.setStyle(Paint.Style.FILL);
        sectorPaint = new Paint(hollowPaint);

        hollowPaint.setColor(pieEntry.getHollowColor());
        sectorPaint.setColor(pieEntry.getColor());
    }

    @Override
    public void press() {

    }

    @Override
    public void unPress() {

    }

    @Override
    public boolean containAngle(float clickX, float clickY) {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (null != hollowRectF && null != sectorPath && null != mPieEntry) {
            canvas.drawArc(hollowRectF, mPieEntry.getStartAngle(), mPieEntry.getSweepAngle(), true, hollowPaint);
            canvas.drawPath(sectorPath, sectorPaint);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        float size = Math.min(bounds.width(), bounds.height());
        radius = (int) (size * sectorRadiusRate);
        computeGraphics(getOriginCx(), getOriginCy());
    }

    private void computeGraphics(int cx, int cy) {
        float hollowLengthRate = isEqualOrLessThanZero(mPieEntry.getHollowLengthRate()) ? DEFAULT_HOLLOW_LENGTH_RATE : mPieEntry.getHollowLengthRate();
        float hollowRadius = radius * hollowLengthRate;
        hollowRectF = new RectF(cx - hollowRadius, cy - hollowRadius, cx + hollowRadius, cy + hollowRadius);
        PointF[] sectorPathArr = new PointF[]{
                CircleUtil.getPositionByAngle(mPieEntry.getStartAngle(), (int) hollowRadius, cx, cy),
                CircleUtil.getPositionByAngle(mPieEntry.getStartAngle(), radius, cx, cy),
                CircleUtil.getPositionByAngle(mPieEntry.getStartAngle() + mPieEntry.getSweepAngle(), radius, cx, cy),
                CircleUtil.getPositionByAngle(mPieEntry.getStartAngle() + mPieEntry.getSweepAngle(), (int) hollowRadius, cx, cy)
        };
        sectorPath = new Path();
        sectorPath.moveTo(sectorPathArr[0].x, sectorPathArr[0].y);
        sectorPath.lineTo(sectorPathArr[1].x, sectorPathArr[1].y);
        sectorPath.lineTo(sectorPathArr[2].x, sectorPathArr[2].y);
        sectorPath.lineTo(sectorPathArr[3].x, sectorPathArr[3].y);
    }

    private boolean isEqualOrLessThanZero(float value) {
        int res = Float.compare(value, 0);
        return res == 0 || res == -1;
    }
}
