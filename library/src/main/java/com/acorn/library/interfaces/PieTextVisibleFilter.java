package com.acorn.library.interfaces;

import com.acorn.library.entry.PieEntry;

public interface PieTextVisibleFilter<T extends PieEntry> {
    boolean isShowText(T pieEntry);
}
