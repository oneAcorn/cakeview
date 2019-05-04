package com.acorn.library.interfaces;

import android.support.annotation.NonNull;

import com.acorn.library.drawable.BaseTextDrawable;
import com.acorn.library.entry.PieEntry;

public interface PieTextFactory<T extends PieEntry,K extends BaseTextDrawable> {
    K createPieText(@NonNull T pieEntry);
}
