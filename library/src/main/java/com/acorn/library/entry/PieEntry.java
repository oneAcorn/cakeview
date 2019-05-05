package com.acorn.library.entry;

public class PieEntry {
    //此饼图项占比0~1
    private float value;
    //饼图颜色
    private int color;
    //是否在饼图上显示文字
    private boolean isShowPieText = true;
    private String title;
    private int textColor;
    //字体大小sp
    private int textSize;
    //是否在饼图上显示指示文字
    private boolean isShowPieIndicateText = true;
    //外层指示文字
    private String indicateText;
    //外层指示文字线颜色
    private int indicateLineColor;
    //外层指示文字颜色
    private int indicateTextColor;
    //外层指示文字字体大小
    private int indicateTextSize;
    private float startAngle;
    private float sweepAngle;

    public PieEntry(PieEntry pieEntry) {
        this.value = pieEntry.value;
        this.color = pieEntry.color;
        this.isShowPieText = pieEntry.isShowPieText;
        this.title = pieEntry.title;
        this.textColor = pieEntry.textColor;
        this.textSize = pieEntry.textSize;
        this.isShowPieIndicateText = pieEntry.isShowPieIndicateText;
        this.indicateText = pieEntry.indicateText;
        this.indicateLineColor = pieEntry.indicateLineColor;
        this.indicateTextColor = pieEntry.indicateTextColor;
        this.indicateTextSize = pieEntry.indicateTextSize;
        this.startAngle = pieEntry.startAngle;
        this.sweepAngle = pieEntry.sweepAngle;
    }

    public PieEntry(float value) {
        this.value = value;
    }

    public PieEntry(float value, String title) {
        this.value = value;
        this.title = title;
    }

    public PieEntry(float value, String title, String indicateText) {
        this.value = value;
        this.title = title;
        this.indicateText = indicateText;
    }

    public PieEntry(float value, String title, int textColor, int textSize, String indicateText, int indicateLineColor, int indicateTextColor, int indicateTextSize) {
        this.value = value;
        this.title = title;
        this.textColor = textColor;
        this.textSize = textSize;
        this.indicateText = indicateText;
        this.indicateLineColor = indicateLineColor;
        this.indicateTextColor = indicateTextColor;
        this.indicateTextSize = indicateTextSize;
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

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isShowPieText() {
        return isShowPieText;
    }

    public void setShowPieText(boolean showPieText) {
        isShowPieText = showPieText;
    }

    public int getIndicateLineColor() {
        return indicateLineColor;
    }

    public void setIndicateLineColor(int indicateLineColor) {
        this.indicateLineColor = indicateLineColor;
    }

    public int getIndicateTextSize() {
        return indicateTextSize;
    }

    public void setIndicateTextSize(int indicateTextSize) {
        this.indicateTextSize = indicateTextSize;
    }

    public String getIndicateText() {
        return indicateText;
    }

    public void setIndicateText(String indicateText) {
        this.indicateText = indicateText;
    }

    public int getIndicateTextColor() {
        return indicateTextColor;
    }

    public void setIndicateTextColor(int indicateTextColor) {
        this.indicateTextColor = indicateTextColor;
    }

    public boolean isShowPieIndicateText() {
        return isShowPieIndicateText;
    }

    public void setShowPieIndicateText(boolean showPieIndicateText) {
        isShowPieIndicateText = showPieIndicateText;
    }
}
