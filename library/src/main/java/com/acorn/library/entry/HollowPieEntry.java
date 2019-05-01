package com.acorn.library.entry;

public class HollowPieEntry extends PieEntry {
    //距离圆心多少距离内中空(0~1)
    private float hollowLengthRate;
    private int hollowColor;

    public HollowPieEntry(float value, String title) {
        super(value, title);
    }

    public HollowPieEntry(float value, String title, int textSize) {
        super(value, title, textSize);
    }

    public HollowPieEntry(float value, String title, int textSize, int color) {
        super(value, title, textSize, color);
    }

    public HollowPieEntry(float value, String title, int textSize, int color, float hollowLengthRate) {
        super(value, title, textSize, color);
        this.hollowLengthRate = hollowLengthRate;
    }

    public HollowPieEntry(float value, String title, int textSize, int color, float hollowLengthRate, int hollowColor) {
        super(value, title, textSize, color);
        this.hollowLengthRate = hollowLengthRate;
        this.hollowColor = hollowColor;
    }

    public float getHollowLengthRate() {
        return hollowLengthRate;
    }

    public void setHollowLengthRate(float hollowLengthRate) {
        this.hollowLengthRate = hollowLengthRate;
    }

    public int getHollowColor() {
        return hollowColor;
    }

    public void setHollowColor(int hollowColor) {
        this.hollowColor = hollowColor;
    }
}
