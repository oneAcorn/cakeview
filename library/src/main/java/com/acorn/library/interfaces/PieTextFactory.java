package com.acorn.library.interfaces;

import com.acorn.library.drawable.BaseTextDrawable;
import com.acorn.library.entry.PieEntry;

public interface PieTextFactory<T extends PieEntry> {
    BaseTextDrawable<T> createPieText(T pieEntry);
}
