package com.acorn.library.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import com.acorn.library.entry.PieEntry;
import com.acorn.library.interfaces.OnSectorChangeListener;

public abstract class BaseTextDrawable<T extends PieEntry> extends Drawable implements OnSectorChangeListener<T> {
    private Context mContext;
    private T pieEntry;
    protected PointF mTextPoint;

    public BaseTextDrawable(Context context) {
        this.mContext = context;
    }

    public abstract void setTextPoint(T pieEntry, int cx, int cy, int radius, int source);

    @Override
    public void onSectorChange(T pieEntry, int cx, int cy, int radius, int source) {
        this.pieEntry = pieEntry;
        setTextPoint(pieEntry, cx, cy, radius, source);
    }

    public Context getContext() {
        return mContext;
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
