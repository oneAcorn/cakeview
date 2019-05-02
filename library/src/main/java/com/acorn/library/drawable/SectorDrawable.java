package com.acorn.library.drawable;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.AccelerateInterpolator;

import com.acorn.library.entry.PieEntry;
import com.acorn.library.utils.CircleUtil;

/**
 * 扇形
 */
public class SectorDrawable extends BaseSectorDrawable<PieEntry> {
    //默认扇形区占比(给弹出动画预留空间)
    private static final float DEFAULT_SECTOR_RADIUS_RATE = 0.45f;
    //高亮动画突出距离比0~1
    private static final float DEFAULT_HIGHLIGHT_DISTANCE_RATE = 0.9f;
    //扇形半径占比0f~0.5f
    private float sectorRadiusRate;
    //高亮动画弹出距离占比
    private float highlightDistanceRate;

    private Paint mPaint;
    private RectF mRectF;
    //圆心
    private int cx, cy;
    private int lastCx = -1, lastCy = -1;
    //半径
    private int radius;
    private ValueAnimator pressAnim;
    private boolean isStartAngleChanged;

    public SectorDrawable(PieEntry pieEntry) {
        this(pieEntry, DEFAULT_SECTOR_RADIUS_RATE, DEFAULT_HIGHLIGHT_DISTANCE_RATE);
    }

    /**
     * @param pieEntry              pieEntry
     * @param sectorRadiusRate      扇形半径占比0f~0.5f
     * @param highlightDistanceRate 高亮动画弹出距离占比((0.5f-sectorRadiusRate)*highlightDistanceRate)
     */
    public SectorDrawable(PieEntry pieEntry, float sectorRadiusRate, float highlightDistanceRate) {
        super(pieEntry);
        this.sectorRadiusRate = sectorRadiusRate;
        this.highlightDistanceRate = highlightDistanceRate;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);  //抗锯齿
        mPaint.setColor(pieEntry.getColor());
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        if (null != mRectF && null != getPieEntry()) {
            mPaint.setColor(mPieEntry.getColor());
            canvas.drawArc(mRectF, mPieEntry.getStartAngle(), mPieEntry.getSweepAngle(), true, mPaint);
        }
    }

    @Override
    public void offsetAngle(float offsetAngle) {
        mPieEntry.setStartAngle((360f + mPieEntry.getStartAngle() + offsetAngle) % 360f);
        isStartAngleChanged = true;
        invalidateSelf();
    }

    @Override
    public void press() {
        if (isHighlighting())
            return;
        if (null == pressAnim || isStartAngleChanged) {
            initPressAnim();
            isStartAngleChanged = false;
        }
        if (!pressAnim.isStarted())
            pressAnim.start();
    }

    @Override
    public void unPress() {
        if (!isHighlighting())
            return;
        if (null == pressAnim || isStartAngleChanged) {
            initPressAnim();
            isStartAngleChanged = false;
        }
        if (pressAnim.isRunning())
            pressAnim.cancel();
        pressAnim.reverse();
    }

    private void initPressAnim() {
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cx = ((Number) animation.getAnimatedValue("cx")).intValue();
                cy = ((Number) animation.getAnimatedValue("cy")).intValue();
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

    @Override
    public boolean containAngle(float clickX, float clickY) {
        if (getPieEntry() == null)
            return false;
        float clickAngle = CircleUtil.getAngleByPosition(clickX, clickY, cx, cy, radius);
        PieEntry pieEntry = getPieEntry();
        if (Float.compare(pieEntry.getStartAngle() + pieEntry.getSweepAngle(), 360f) == 1) { //扇形区覆盖起始角度的情况
            return CircleUtil.isContainAngle(clickAngle, pieEntry.getStartAngle(), 360f) ||
                    CircleUtil.isContainAngle(clickAngle, 0, (pieEntry.getStartAngle() + pieEntry.getSweepAngle()) % 360f);
        } else {
            return CircleUtil.isContainAngle(clickAngle, pieEntry.getStartAngle(), pieEntry.getStartAngle() + pieEntry.getSweepAngle());
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        cx = getOriginCx();
        cy = getOriginCy();
        float size = Math.min(bounds.width(), bounds.height());
        radius = (int) (size * sectorRadiusRate);
        computeRectF(cx, cy);
    }

    private void computeRectF(int cx, int cy) {
        mRectF = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
    }

    private void updateGraphics(int cx, int cy) {
        if (lastCx == -1)
            lastCx = cx;
        if (lastCy == -1)
            lastCy = cy;
        mRectF.offset(cx - lastCx, cy - lastCy);
        lastCx = cx;
        lastCy = cy;
    }
}
