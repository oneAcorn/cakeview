package com.acorn.library.interfaces;

import com.acorn.library.drawable.BaseTextDrawable;

public interface PieTextFactory<T extends BaseTextDrawable> {
    T createPieText();
}
