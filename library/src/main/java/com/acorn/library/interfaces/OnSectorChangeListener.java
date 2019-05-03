package com.acorn.library.interfaces;

import com.acorn.library.entry.PieEntry;

public interface OnSectorChangeListener <T extends PieEntry> {
    void onSectorChange(T pieEntry,int cx,int cy,int radius);
}
