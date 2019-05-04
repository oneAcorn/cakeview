package com.acorn.library.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.acorn.library.entry.PieEntry;
import com.acorn.library.utils.CircleUtil;
import com.acorn.library.utils.DensityUtils;

public class SectorTextDrawable extends BaseTextDrawable<PieEntry> {
    private static final int DEFAULT_TEXT_SIZE = 12;
    private static final int DEFAULT_TEXT_COLOR = 0xffffffff;
    private Paint mTextPaint;

    public SectorTextDrawable(Context context, PieEntry pieEntry) {
        super(pieEntry);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);  //抗锯齿
        mTextPaint.setColor(pieEntry.getTextColor() == 0 ? DEFAULT_TEXT_COLOR : pieEntry.getTextColor());
        mTextPaint.setTextSize(pieEntry.getTextSize() <= 0 ? DensityUtils.sp2px(context, DEFAULT_TEXT_SIZE) :
                DensityUtils.sp2px(context, pieEntry.getTextSize()));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (getPieEntry() != null && getPieEntry().isShowPieText())
            canvas.drawText(getPieEntry().getTitle(), mTextPoint.x, mTextPoint.y, mTextPaint);
    }

    @Override
    public void setTextPoint(PieEntry pieEntry, int cx, int cy, int radius) {
        mTextPoint = CircleUtil.getSectorCenterPosition(getPieEntry().getStartAngle(), getPieEntry().getSweepAngle(), cx, cy, radius);
    }
}
