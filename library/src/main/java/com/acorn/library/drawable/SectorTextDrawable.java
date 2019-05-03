package com.acorn.library.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.acorn.library.entry.PieEntry;
import com.acorn.library.utils.CircleUtil;

public class SectorTextDrawable extends BaseTextDrawable<PieEntry> {
    private Paint mTextPaint;

    public SectorTextDrawable(PieEntry pieEntry, int textSize) {
        super(pieEntry);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);  //抗锯齿
        mTextPaint.setColor(pieEntry.getTextColor());
        mTextPaint.setTextSize(textSize);
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
