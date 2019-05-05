package com.acorn.library.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.acorn.library.entry.HollowPieEntry;
import com.acorn.library.utils.CircleUtil;
import com.acorn.library.utils.DensityUtils;

public class HollowSectorTextDrawable extends BaseTextDrawable<HollowPieEntry> {
    private static final int DEFAULT_TEXT_SIZE = 12;
    private static final int DEFAULT_TEXT_COLOR = 0xffffffff;
    private Paint mTextPaint;

    public HollowSectorTextDrawable(Context context) {
        super(context);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);  //抗锯齿
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (getPieEntry() != null && null != mTextPoint && getPieEntry().isShowPieText())
            canvas.drawText(getPieEntry().getTitle(), mTextPoint.x, mTextPoint.y, mTextPaint);
    }

    @Override
    public void setTextPoint(HollowPieEntry pieEntry, int cx, int cy, int radius, int source) {
        if (source == BaseSectorDrawable.Source.INIT) {
            mTextPaint.setColor(pieEntry.getTextColor() == 0 ? DEFAULT_TEXT_COLOR : pieEntry.getTextColor());
            mTextPaint.setTextSize(pieEntry.getTextSize() <= 0 ? DensityUtils.sp2px(getContext(), DEFAULT_TEXT_SIZE) :
                    DensityUtils.sp2px(getContext(), pieEntry.getTextSize()));
        }
        mTextPoint = CircleUtil.getPositionByAngle(pieEntry.getStartAngle(), pieEntry.getSweepAngle(),
                (int) ((radius * pieEntry.getHollowLengthRate()) + (radius - (radius * pieEntry.getHollowLengthRate())) / 2), cx, cy);
        invalidateSelf();
    }
}
