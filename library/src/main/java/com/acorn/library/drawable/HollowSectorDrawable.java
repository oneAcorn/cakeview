package com.acorn.library.drawable;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;

import com.acorn.library.entry.HollowPieEntry;
import com.acorn.library.entry.PieEntry;
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

    private Paint sectorPaint;
    private RectF hollowRectF, sectorRectF;
    //圆心
    private int cx, cy;
    private int lastCx = -1, lastCy = -1;
    //半径
    private int radius;
    //空洞半径
    private float hollowRadius;
    private Path sectorPath;
    private ValueAnimator pressAnim;

    public HollowSectorDrawable(HollowPieEntry pieEntry) {
        this(pieEntry, DEFAULT_SECTOR_RADIUS_RATE, DEFAULT_HIGHLIGHT_DISTANCE_RATE);
    }

    public HollowSectorDrawable(HollowPieEntry pieEntry, float sectorRadiusRate, float highlightDistanceRate) {
        super(pieEntry);
        this.sectorRadiusRate = sectorRadiusRate;
        this.highlightDistanceRate = highlightDistanceRate;
        sectorPaint = new Paint();
        sectorPaint.setAntiAlias(true);  //抗锯齿
        sectorPaint.setStyle(Paint.Style.FILL);

        sectorPaint.setColor(pieEntry.getColor());
    }

    @Override
    public void press() {
        if (isHighlighting())
            return;
        if (null == pressAnim)
            initPressAnim();
        if (!pressAnim.isStarted())
            pressAnim.start();
    }

    @Override
    public void unPress() {
        if (!isHighlighting())
            return;
        if (null == pressAnim)
            initPressAnim();
        if (pressAnim.isRunning())
            pressAnim.cancel();
        pressAnim.reverse();
    }

    @Override
    public boolean containAngle(float clickX, float clickY) {
        if (getPieEntry() == null)
            return false;
        float clickAngle = CircleUtil.getAngleByPosition(clickX, clickY, cx, cy, radius);
        //触摸点距离圆心距离
        float clickRadius = CircleUtil.getDistanceByPosition(cx, cy, clickX, clickY);
        PieEntry pieEntry = getPieEntry();
        if (Float.compare(pieEntry.getStartAngle() + pieEntry.getSweepAngle(), 360f) == 1) { //扇形区覆盖起始角度的情况
            return Float.compare(clickRadius, hollowRadius) == 1 &&
                    CircleUtil.isContainAngle(clickAngle, pieEntry.getStartAngle(), 360f) ||
                    CircleUtil.isContainAngle(clickAngle, 0, (pieEntry.getStartAngle() + pieEntry.getSweepAngle()) % 360f);
        } else {
            return Float.compare(clickRadius, hollowRadius) == 1 &&
                    CircleUtil.isContainAngle(clickAngle, pieEntry.getStartAngle(), pieEntry.getStartAngle() + pieEntry.getSweepAngle());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (null != hollowRectF && null != sectorPath && null != mPieEntry) {
            canvas.drawPath(sectorPath, sectorPaint);
        }
    }

    @Override
    public void offsetAngle(float offsetAngle) {
        if (null == mPieEntry)
            return;
        mPieEntry.setStartAngle((360f + mPieEntry.getStartAngle() + offsetAngle) % 360f);
        computeGraphics(getOriginCx(), getOriginCy());
        invalidateSelf();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        float size = Math.min(bounds.width(), bounds.height());
        cx = getOriginCx();
        cy = getOriginCy();
        radius = (int) (size * sectorRadiusRate);
        computeRect(getOriginCx(), getOriginCy());
        computeGraphics(getOriginCx(), getOriginCy());
    }

    private void computeRect(int cx, int cy) {
        float hollowLengthRate = isEqualOrLessThanZero(mPieEntry.getHollowLengthRate()) ? DEFAULT_HOLLOW_LENGTH_RATE : mPieEntry.getHollowLengthRate();
        hollowRadius = radius * hollowLengthRate;
        //计算小扇形（空出来的扇形）的rect
        hollowRectF = new RectF(cx - hollowRadius, cy - hollowRadius, cx + hollowRadius, cy + hollowRadius);

        //计算出大扇形的rect
        float inset = hollowRadius - (float) radius;
        sectorRectF = new RectF(hollowRectF);
        sectorRectF.inset(inset, inset);
    }

    private void computeGraphics(int cx, int cy) {
        sectorPath = new Path();
        sectorPath.arcTo(hollowRectF, mPieEntry.getStartAngle(), mPieEntry.getSweepAngle());
        PointF point1 = CircleUtil.getPositionByAngle(mPieEntry.getStartAngle() + mPieEntry.getSweepAngle(), radius, cx, cy);
        sectorPath.lineTo(point1.x, point1.y);
        sectorPath.arcTo(sectorRectF, mPieEntry.getStartAngle() + mPieEntry.getSweepAngle(), -mPieEntry.getSweepAngle());
        PointF point2 = CircleUtil.getPositionByAngle(mPieEntry.getStartAngle(), (int) hollowRadius, cx, cy);
        sectorPath.lineTo(point2.x, point2.y);
    }

    private void updateGraphics(int cx, int cy) {
        if (lastCx == -1)
            lastCx = cx;
        if (lastCy == -1)
            lastCy = cy;
        sectorPath.offset(cx - lastCx, cy - lastCy);
        lastCx = cx;
        lastCy = cy;
    }

    private boolean isEqualOrLessThanZero(float value) {
        int res = Float.compare(value, 0);
        return res == 0 || res == -1;
    }

    private void initPressAnim() {
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cx = ((Number) animation.getAnimatedValue("cx")).intValue();
                cy = ((Number) animation.getAnimatedValue("cy")).intValue();
                Log.i("tasssss", "fra:" + animation.getAnimatedFraction() + "," + cx);
                updateGraphics(cx, cy);
                invalidateSelf();
            }
        };
        PointF toPointF = CircleUtil.getPositionByAngle(mPieEntry.getStartAngle() + mPieEntry.getSweepAngle() / 2,
                (int) (radius * (0.5 - sectorRadiusRate) * highlightDistanceRate),
                getOriginCx(), getOriginCy());
        PropertyValuesHolder cxHolder = PropertyValuesHolder.ofInt("cx", getOriginCx(), (int) toPointF.x);
        PropertyValuesHolder cyHolder = PropertyValuesHolder.ofInt("cy", getOriginCy(), (int) toPointF.y);
        pressAnim = ValueAnimator.ofPropertyValuesHolder(cxHolder, cyHolder);

        pressAnim.setInterpolator(new AccelerateInterpolator());
        pressAnim.addUpdateListener(listener);
        pressAnim.setDuration(300);
        pressAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setHighlighting(cx == getOriginCx() && cy == getOriginCy());
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
