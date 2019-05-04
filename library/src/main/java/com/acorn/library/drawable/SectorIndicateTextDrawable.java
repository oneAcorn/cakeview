package com.acorn.library.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.text.TextUtils;

import com.acorn.library.entry.PieEntry;
import com.acorn.library.utils.CircleUtil;
import com.acorn.library.utils.DensityUtils;

public class SectorIndicateTextDrawable extends BaseTextDrawable<PieEntry> {
    private static final int DEFAULT_TEXT_SIZE = 12;
    private static final int DEFAULT_TEXT_COLOR = 0xff000000;
    private static final int DEFAULT_LINE_COLOR = 0xff000000;
    private static final float indicateLine1Rate = 0.15f;
    private static final float indicateLine2MaxRate = 0.5f;
    private float textMargin;
    private Path mPath;
    private Paint mTextPaint, mPathPaint;

    public SectorIndicateTextDrawable(Context context, PieEntry pieEntry) {
        super(pieEntry);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);  //抗锯齿
        mTextPaint.setColor(pieEntry.getIndicateTextColor() == 0 ? DEFAULT_TEXT_COLOR : pieEntry.getIndicateTextColor());
        mTextPaint.setTextSize(pieEntry.getIndicateTextSize() <= 0 ? DensityUtils.sp2px(context, DEFAULT_TEXT_SIZE) :
                DensityUtils.sp2px(context, pieEntry.getIndicateTextSize()));

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);  //抗锯齿
        mPathPaint.setColor(pieEntry.getIndicateLineColor() == 0 ? DEFAULT_LINE_COLOR : pieEntry.getIndicateLineColor());
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(DensityUtils.dp2px(context, 1));

        textMargin = DensityUtils.dp2px(context, 4);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (getPieEntry() != null && mPath != null) {
            canvas.drawPath(mPath, mPathPaint);
            canvas.drawText(getPieEntry().getIndicateText(), mTextPoint.x, mTextPoint.y, mTextPaint);
        }
    }

    @Override
    public void setTextPoint(PieEntry pieEntry, int cx, int cy, int radius) {
        if (TextUtils.isEmpty(pieEntry.getIndicateText())) {
            mPath = null;
            return;
        }
        PointF centerPoint = CircleUtil.getSectorCenterPosition(pieEntry.getStartAngle(), pieEntry.getSweepAngle(), cx, cy, radius);
        mPath = new Path();
        mPath.moveTo(centerPoint.x, centerPoint.y);
        PointF lineEnd = CircleUtil.getPositionByAngle(pieEntry.getStartAngle(), pieEntry.getSweepAngle(),
                getLine1Radius(pieEntry, radius), cx, cy);
        mPath.lineTo(lineEnd.x, lineEnd.y);
        PointF line2End = getLine2Point(pieEntry, lineEnd, radius);
        mPath.lineTo(line2End.x, line2End.y);

        float textPointX;
        if (Float.compare(line2End.x, lineEnd.x) == 1) {
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            textPointX = line2End.x + textMargin;
        } else {
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            textPointX = line2End.x - textMargin;
        }
        float textPointY = lineEnd.y + mTextPaint.getTextSize() / 2;
        mTextPoint = new PointF(textPointX, textPointY);
    }

    private int getLine1Radius(PieEntry pieEntry, int radius) {
        //把圆四等分为0~90,90~180,180~270,270~360,得出0~90的角度变化
        float angle = 0;
        float curAngle = pieEntry.getStartAngle() + pieEntry.getSweepAngle() / 2;
        curAngle = curAngle % 360f;
        if (isMoreOrEqual(curAngle, 0) && Float.compare(curAngle, 90f) == -1) {
            angle = curAngle;
        } else if (isMoreOrEqual(curAngle, 90) && Float.compare(curAngle, 180f) == -1) {
            angle = 90f - curAngle % 90f;
        } else if (isMoreOrEqual(curAngle, 180) && Float.compare(curAngle, 270) == -1) {
            angle = curAngle % 180f;
        } else if (isMoreOrEqual(curAngle, 270f) && Float.compare(curAngle, 360f) == -1) {
            angle = 90f - curAngle % 270f;
        }
        float line1Radius = radius + radius * indicateLine1Rate * angle / 90f;
        return (int) line1Radius;
    }

    private PointF getLine2Point(PieEntry pieEntry, PointF line1, int radius) {
        //把圆四等分为0~90,90~180,180~270,270~360,得出0~90的角度变化
        float angle = 0;
        float curAngle = pieEntry.getStartAngle() + pieEntry.getSweepAngle() / 2;
        curAngle = curAngle % 360f;
        boolean isLeft = false;
        if (isMoreOrEqual(curAngle, 0) && Float.compare(curAngle, 90f) == -1) {
            angle = curAngle;
            isLeft = false;
        } else if (isMoreOrEqual(curAngle, 90) && Float.compare(curAngle, 180f) == -1) {
            angle = 90f - curAngle % 90f;
            isLeft = true;
        } else if (isMoreOrEqual(curAngle, 180) && Float.compare(curAngle, 270) == -1) {
            angle = curAngle % 180f;
            isLeft = true;
        } else if (isMoreOrEqual(curAngle, 270f) && Float.compare(curAngle, 360f) == -1) {
            angle = 90f - curAngle % 270f;
            isLeft = false;
        }
        float pointX;
        float offset = radius * indicateLine2MaxRate * angle / 90f;
        if (isLeft) {
            pointX = line1.x - offset;
        } else {
            pointX = line1.x + offset;
        }
        return new PointF(pointX, line1.y);
    }


    private boolean isMoreOrEqual(float f1, float f2) {
        return Float.compare(f1, f2) == 1 || Float.compare(f1, f2) == 0;
    }
}
