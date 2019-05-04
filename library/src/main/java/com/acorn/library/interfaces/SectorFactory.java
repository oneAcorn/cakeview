package com.acorn.library.interfaces;

import com.acorn.library.drawable.BaseSectorDrawable;
import com.acorn.library.entry.PieEntry;

public interface SectorFactory <T extends PieEntry,K extends BaseSectorDrawable> {
    K createSector(T pieEntry,int position);
}
