package com.acorn.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PieView extends View {
    private static final int CIRCLE_TOTAL_ANGLE = 360;
    //当总值小于1时,"其他"扇形的默认颜色
    private static final int DEFAULT_OTHER_SECTOR_COLOR = 0xFFCED5CE;
    private final int[] defaultSectorColors = new int[]{0xff4F50A0, 0xff649B9A,
            0xffF9BB08, 0xffA4529C, 0xffff6f2f, 0xff990099, 0xff999999,
            0xff663300};

    private List<SectorDrawable> mSectorDrawables;
    //当总值小于1时,扇形的文本
    private static final String otherWord = "其他";

    private OnPieViewItemClickListener mOnPieViewItemClickListener;
    private int minTouchSlop;
    private Handler mHandler = new Handler();
    private static final int MAX_SINGLE_CLICK_TIME = 50;// 单击最长等待时间
    private int downX, downY;

    private Runnable mSingleClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (null != mOnPieViewItemClickListener) {
                mOnPieViewItemClickListener.onPieClick(getClickDrawable(), 0);
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != mSectorDrawables && !mSectorDrawables.isEmpty()) {
            for (SectorDrawable sectorDrawable : mSectorDrawables) {
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
                if (mSingleClickRunnable != null) {
                    mHandler.removeCallbacks(mSingleClickRunnable);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                if (null != mSingleClickRunnable && (Math.abs(moveX - downX) > minTouchSlop || Math.abs(moveY - downY) > minTouchSlop)) { //大于单击最小移动距离,移除单击runnable
                    mHandler.removeCallbacks(mSingleClickRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) <= minTouchSlop && Math.abs(upY - downY) <= minTouchSlop) {
                    mHandler.postDelayed(mSingleClickRunnable, MAX_SINGLE_CLICK_TIME);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }


    public void setPieEntries(List<PieEntry> pieEntries) {
        ensureSectorDrawables(revisePieEntries(pieEntries));
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
        if (Float.compare(valueSum, 1) == -1) { //总数小于1,用其他补全
            pieEntries.add(new PieEntry(1f - valueSum, DEFAULT_OTHER_SECTOR_COLOR, otherWord, pieEntries.get(0).getTextSize()));
        }
        return pieEntries;
    }

    private void ensureSectorDrawables(List<PieEntry> pieEntries) {
        mSectorDrawables = new ArrayList<>();
        for (PieEntry pieEntry : pieEntries) {
            SectorDrawable sectorDrawable = new SectorDrawable();
            sectorDrawable.setPieEntry(pieEntry);
            mSectorDrawables.add(sectorDrawable);
            sectorDrawable.setCallback(this);
        }
    }

    private SectorDrawable getClickDrawable() {
        if (null == mSectorDrawables || mSectorDrawables.isEmpty())
            return null;
        int clickX = downX;
        int clickY = downY;
        SectorDrawable clickDrawable = null;
        for (SectorDrawable sectorDrawable : mSectorDrawables) {
            if (sectorDrawable.containAngle(clickX, clickY)) {
                clickDrawable = sectorDrawable;
                break;
            }
        }
        return clickDrawable;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (null != mSectorDrawables && !mSectorDrawables.isEmpty()) {
            Rect rect = new Rect(0, 0, w, h);
            for (SectorDrawable sectorDrawable : mSectorDrawables) {
                sectorDrawable.setBounds(rect);
            }
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        boolean res = false;
        if (null != mSectorDrawables && !mSectorDrawables.isEmpty()) {
            for (SectorDrawable sectorDrawable : mSectorDrawables) {
                if (who == sectorDrawable) {
                    res = true;
                    break;
                }
            }
        }
        return res || super.verifyDrawable(who);
    }

    public void setOnPieViewItemClickListener(OnPieViewItemClickListener onPieViewItemClickListener) {
        mOnPieViewItemClickListener = onPieViewItemClickListener;
    }
}
