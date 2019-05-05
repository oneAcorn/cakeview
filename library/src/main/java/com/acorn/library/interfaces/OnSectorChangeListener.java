package com.acorn.library.interfaces;

import com.acorn.library.entry.PieEntry;

public interface OnSectorChangeListener <T extends PieEntry> {
    /**
     *
     * @param pieEntry
     * @param cx
     * @param cy
     * @param radius
     * @param source  BaseSectorDrawable.Source
     */
    void onSectorChange(T pieEntry,int cx,int cy,int radius,int source);
}
