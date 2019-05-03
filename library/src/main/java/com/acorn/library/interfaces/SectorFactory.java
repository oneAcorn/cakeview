package com.acorn.library.interfaces;

import com.acorn.library.drawable.BaseSectorDrawable;
import com.acorn.library.entry.PieEntry;

public interface SectorFactory <T extends PieEntry> {
    BaseSectorDrawable createSector(T pieEntry,int position);
}
