package com.acorn.library.entry;

public class HollowPieEntry extends PieEntry {
    //距离圆心多少距离内中空(0~1)
    private float hollowLengthRate;

    public HollowPieEntry(PieEntry pieEntry) {
        super(pieEntry);
    }

    public HollowPieEntry(PieEntry pieEntry, float hollowLengthRate) {
        super(pieEntry);
        this.hollowLengthRate = hollowLengthRate;
    }

    public HollowPieEntry(float value) {
        super(value);
    }

    public HollowPieEntry(float value, String title) {
        super(value, title);
    }

    public HollowPieEntry(float value, String title, float hollowLengthRate) {
        super(value, title);
        this.hollowLengthRate = hollowLengthRate;
    }

    public HollowPieEntry(float value, String title, String indicateText) {
        super(value, title, indicateText);
    }

    public HollowPieEntry(float value, String title, String indicateText, float hollowLengthRate) {
        super(value, title, indicateText);
        this.hollowLengthRate = hollowLengthRate;
    }

    public HollowPieEntry(float value, String title, int textColor, int textSize, String indicateText, int indicateLineColor, int indicateTextColor, int indicateTextSize) {
        super(value, title, textColor, textSize, indicateText, indicateLineColor, indicateTextColor, indicateTextSize);
    }


    public HollowPieEntry(float value, String title, int textColor, int textSize, String indicateText, int indicateLineColor, int indicateTextColor, int indicateTextSize, float hollowLengthRate) {
        super(value, title, textColor, textSize, indicateText, indicateLineColor, indicateTextColor, indicateTextSize);
        this.hollowLengthRate = hollowLengthRate;
    }

    public float getHollowLengthRate() {
        return hollowLengthRate;
    }

    public void setHollowLengthRate(float hollowLengthRate) {
        this.hollowLengthRate = hollowLengthRate;
    }
}
