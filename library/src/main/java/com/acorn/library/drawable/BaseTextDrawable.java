package com.acorn.library.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import com.acorn.library.entry.PieEntry;
import com.acorn.library.interfaces.OnSectorChangeListener;

public abstract class BaseTextDrawable<T extends PieEntry> extends Drawable implements OnSectorChangeListener<T> {
    private T pieEntry;
    protected PointF mTextPoint;

    public BaseTextDrawable(T pieEntry) {
        this.pieEntry = pieEntry;
    }

    public abstract void setTextPoint(T pieEntry, int cx, int cy, int radius);

    @Override
    public void onSectorChange(T pieEntry, int cx, int cy, int radius) {
        setTextPoint(pieEntry, cx, cy, radius);
    }

    public PointF getTextPoint() {
        return mTextPoint;
    }

    public T getPieEntry() {
        return pieEntry;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
