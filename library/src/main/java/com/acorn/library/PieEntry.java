package com.acorn.library;

public class PieEntry {
    //此饼图项占比0~1
    private float value;
    private int color;
    private String title;
    //字体大小sp
    private int textSize;
    private float startAngle;
    private float sweepAngle;

    public PieEntry(float value, String title) {
        this.value = value;
        this.title = title;
    }

    public PieEntry(float value, String title, int textSize) {
        this.value = value;
        this.title = title;
        this.textSize = textSize;
    }

    public PieEntry(float value, int color, String title, int textSize) {
        this.value = value;
        this.color = color;
        this.title = title;
        this.textSize = textSize;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }
}
