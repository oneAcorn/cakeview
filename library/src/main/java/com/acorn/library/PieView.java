package com.acorn.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.acorn.library.drawable.BaseSectorDrawable;
import com.acorn.library.drawable.SectorDrawable;
import com.acorn.library.entry.PieEntry;
import com.acorn.library.listener.OnPieViewItemClickListener;
import com.acorn.library.listener.SectorFactory;
import com.acorn.library.utils.CircleUtil;

import java.util.ArrayList;
import java.util.List;

public class PieView extends View {
    private static final int CIRCLE_TOTAL_ANGLE = 360;
    //当总值小于1时,"其他"扇形的默认颜色
    private static final int DEFAULT_OTHER_SECTOR_COLOR = 0xFFCED5CE;
    private final int[] defaultSectorColors = new int[]{0xff4F50A0, 0xff649B9A,
            0xffF9BB08, 0xffA4529C, 0xffff6f2f, 0xff990099, 0xff999999,
            0xff663300};

    private List<BaseSectorDrawable> mSectorDrawables;
    //当总值小于1时,扇形的文本
    private static final String otherWord = "其他";

    private OnPieViewItemClickListener mOnPieViewItemClickListener;
    private Handler mHandler = new Handler();
    private static final int MAX_SINGLE_CLICK_TIME = 50;// 单击最长等待时间
    private int downX, downY;
    //最小触摸移动距离
    private int minTouchSlop;
    /**
     * 滑动速度检测类
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 最小滑动速率
     */
    private int minFlingVelocity;
    /**
     * 最大滑动速率
     */
    private int maxFlingVelocity;
    private ValueAnimator inertiaAnim;

    //圆心
    private int cx, cy;
    //半径
    private int radius;

    //是否点击高亮
    private boolean isHighlightEnable = true;
    private boolean isAutoUnpressOther = false;

    private Runnable mSingleClickRunnable = new Runnable() {
        @Override
        public void run() {
            BaseSectorDrawable clickSectorDrawable = getTouchSectorDrawable(downX, downY);
            if (null != mOnPieViewItemClickListener) {
                mOnPieViewItemClickListener.onPieClick(clickSectorDrawable, 0);
            }
            if (isHighlightEnable && null != clickSectorDrawable) {
                if (clickSectorDrawable.isHighlighting()) {
                    clickSectorDrawable.unPress();
                } else {
                    clickSectorDrawable.press();
                    if (isAutoUnpressOther) {
                        for (BaseSectorDrawable sectorDrawable : mSectorDrawables) {
                            if (sectorDrawable.equals(clickSectorDrawable))
                                continue;
                            sectorDrawable.unPress();
                        }
                    }
                }
            }
        }
    };

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        minTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //初始化最小和最大滑动速率
        ViewConfiguration vc = ViewConfiguration.get(context);
        minFlingVelocity = vc.getScaledMinimumFlingVelocity() * 8;
        maxFlingVelocity = vc.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != mSectorDrawables && !mSectorDrawables.isEmpty()) {
            for (BaseSectorDrawable sectorDrawable : mSectorDrawables) {
                sectorDrawable.draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled())
            return false;
        //兼容
        int actionMasked = MotionEventCompat.getActionMasked(event);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                if (mSingleClickRunnable != null) { //移除单击runnable
                    mHandler.removeCallbacks(mSingleClickRunnable);
                }
                startDrag(event);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                if (null != mSingleClickRunnable && (Math.abs(moveX - downX) > minTouchSlop || Math.abs(moveY - downY) > minTouchSlop)) { //大于单击最小移动距离,移除单击runnable
                    mHandler.removeCallbacks(mSingleClickRunnable);
                }
                if (isDragging || Math.abs(moveX - downX) > minTouchSlop || Math.abs(moveY - downY) > minTouchSlop) {
                    onDragging(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) <= minTouchSlop && Math.abs(upY - downY) <= minTouchSlop && (null == inertiaAnim || !inertiaAnim.isRunning())) { //单击
                    mHandler.postDelayed(mSingleClickRunnable, MAX_SINGLE_CLICK_TIME);
                }
                stopDragging();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private boolean isDownOnSector;
    private boolean isDragging;
    private float lastAngle;

    private void startDrag(MotionEvent event) {
        if (isHighlightEnable && isHighlighting()) {
            releaseDrag();
            return;
        }
        isDragging = false;
        isDownOnSector = getTouchSectorDrawable((int) event.getX(), (int) event.getY()) != null;
        lastAngle = CircleUtil.getAngleByPosition(event.getX(), event.getY(), cx, cy);
        if (isDownOnSector) {
            //加入速度检测
            mVelocityTracker = VelocityTracker.obtain();
            mVelocityTracker.addMovement(event);
        }
        log("startDrag " + lastAngle);
    }

    private void onDragging(MotionEvent event) {
        if (!isDownOnSector)
            return;
        isDragging = true;
        float moveAngle = CircleUtil.getAngleByPosition(event.getX(), event.getY(), cx, cy);
        float offsetAngle = moveAngle - lastAngle;
        log("onDragging " + ",moveAngle " + moveAngle + ",lastAngle " + lastAngle + ",offset " + offsetAngle);
        for (BaseSectorDrawable sectorDrawable : mSectorDrawables) {
            sectorDrawable.offsetAngle(offsetAngle);
        }
        lastAngle = moveAngle;
        mVelocityTracker.addMovement(event);
    }

    private void stopDragging() {
        if (isDragging) {
            //通过滑动的距离计算出X,Y方向的速度
            mVelocityTracker.computeCurrentVelocity(1000);
            float velocityX = Math.abs(mVelocityTracker.getXVelocity());
            float velocityY = Math.abs(mVelocityTracker.getYVelocity());
            computeInertiaAnimator(velocityX, velocityY);
        }
        releaseDrag();
    }

    private void releaseDrag() {
        isDownOnSector = false;
        isDragging = false;
        if (mVelocityTracker != null) { //移除速度检测
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void computeInertiaAnimator(float velocityX, float velocityY) {
        float velocity = Math.max(velocityX, velocityY);
        if (velocity > minFlingVelocity) {
            if (inertiaAnim == null)
                initInertiaAnim();
            inertiaAnim.setDuration((long) (velocity / 10f));
            log("computeInertiaAnimator " + velocity);
            inertiaAnim.start();
        }
    }


    private float lastInertiaFraction;

    private void initInertiaAnim() {
        inertiaAnim = ValueAnimator.ofFloat(0f, 1f);
        inertiaAnim.setInterpolator(new DecelerateInterpolator());
        inertiaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                for (BaseSectorDrawable sectorDrawable : mSectorDrawables) {
                    sectorDrawable.offsetAngle((fraction - lastInertiaFraction) * 1000);
                }
                log("initInertiaAnim " + fraction + "," + lastInertiaFraction);
                lastInertiaFraction = fraction;
            }
        });
    }

    private boolean isHighlighting() {
        if (null == mSectorDrawables || mSectorDrawables.isEmpty())
            return false;
        boolean res = false;
        for (BaseSectorDrawable sectorDrawable : mSectorDrawables) {
            if (sectorDrawable.isHighlighting()) {
                res = true;
                break;
            }
        }
        return res;
    }

    private void log(String string) {
        Log.i("PieView", string);
    }

    public void setPieEntries(List<PieEntry> pieEntries) {
        setPieEntries(pieEntries, new SectorFactory<PieEntry>() {
            @Override
            public BaseSectorDrawable createSector(PieEntry pieEntry, int position) {
                return new SectorDrawable(pieEntry);
            }
        });
    }

    public void setPieEntries(List<PieEntry> pieEntries, SectorFactory sectorFactory) {
        ensureSectorDrawables(revisePieEntries(pieEntries), sectorFactory);
        invalidate();
    }

    private List<PieEntry> revisePieEntries(List<PieEntry> pieEntries) {
        float valueSum = 0;
        if (null != pieEntries && !pieEntries.isEmpty()) {
            //上个扇形的结束边界角度
            float lastEndAngle = 0;
            //PieEntry中已设置color的数量
            int colorCount = 0;
            int lastDefaultColorIndex = 0;
            for (PieEntry pie : pieEntries) {
                if (Float.compare(pie.getValue(), 0) == -1 || Float.compare(pie.getValue(), 1) == 1) {
                    throw new IllegalStateException("PieEntry.value must more than 0 and less than 1");
                }
                if (Float.compare(valueSum, 1) == 1) {
                    throw new IllegalStateException("sum of PieEntry.value more than 1");
                }
                pie.setStartAngle(lastEndAngle);
                pie.setSweepAngle(pie.getValue() * CIRCLE_TOTAL_ANGLE);
                lastEndAngle = pie.getStartAngle() + pie.getSweepAngle();
                valueSum += pie.getValue();
                if (pie.getColor() != 0)
                    colorCount++;
                else {
                    if (lastDefaultColorIndex >= defaultSectorColors.length)
                        lastDefaultColorIndex = 0;
                    pie.setColor(defaultSectorColors[lastDefaultColorIndex]);
                    lastDefaultColorIndex++;
                }
            }
            if (colorCount != 0 && colorCount != pieEntries.size()) {
                throw new IllegalStateException("you must set all of the color of PieEntry when you already set one of them");
            }
        } else if (null == pieEntries) {
            pieEntries = new ArrayList<>();
        }
//        if (Float.compare(valueSum, 1) == -1) { //总数小于1,用其他补全
//            PieEntry defaultEntry = new PieEntry(1f - valueSum, otherWord,pieEntries.get(0).getTextSize(),DEFAULT_OTHER_SECTOR_COLOR);
//            defaultEntry.setStartAngle(valueSum * CIRCLE_TOTAL_ANGLE);
//            defaultEntry.setSweepAngle(defaultEntry.getValue() * CIRCLE_TOTAL_ANGLE);
//            defaultEntry.setDefaultPie(true);
//            pieEntries.add(defaultEntry);
//        }
        return pieEntries;
    }

    private void ensureSectorDrawables(List<? extends PieEntry> pieEntries, SectorFactory sectorFactory) {
        mSectorDrawables = new ArrayList<>();
        int length = pieEntries.size();
        for (int i = 0; i < length; i++) {
            PieEntry pieEntry = pieEntries.get(i);
            BaseSectorDrawable sectorDrawable;
            if (pieEntry.isDefaultPie()) {
                sectorDrawable = new SectorDrawable(pieEntry);
            } else {
                sectorDrawable = sectorFactory.createSector(pieEntry, i);
            }
            mSectorDrawables.add(sectorDrawable);
            sectorDrawable.setCallback(this);
        }
    }

    private BaseSectorDrawable getTouchSectorDrawable(int x, int y) {
        if (null == mSectorDrawables || mSectorDrawables.isEmpty())
            return null;
        BaseSectorDrawable clickDrawable = null;
        for (BaseSectorDrawable sectorDrawable : mSectorDrawables) {
            if (sectorDrawable.containAngle(x, y)) {
                clickDrawable = sectorDrawable;
                break;
            }
        }
        return clickDrawable;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = (int) (w / 2f);
        cy = (int) (h / 2f);

        if (null != mSectorDrawables && !mSectorDrawables.isEmpty()) {
            Rect rect = new Rect(0, 0, w, h);
            for (BaseSectorDrawable sectorDrawable : mSectorDrawables) {
                sectorDrawable.setBounds(rect);
            }
        }
    }

    public boolean isHighlightEnable() {
        return isHighlightEnable;
    }

    public void setHighlightEnable(boolean highlightEnable) {
        isHighlightEnable = highlightEnable;
    }

    public boolean isAutoUnpressOther() {
        return isAutoUnpressOther;
    }

    public void setAutoUnpressOther(boolean autoUnpressOther) {
        isAutoUnpressOther = autoUnpressOther;
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who instanceof BaseSectorDrawable || super.verifyDrawable(who);
    }

    public void setOnPieViewItemClickListener(OnPieViewItemClickListener onPieViewItemClickListener) {
        mOnPieViewItemClickListener = onPieViewItemClickListener;
    }
}
