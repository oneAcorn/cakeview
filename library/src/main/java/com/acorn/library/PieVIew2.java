package com.acorn.library;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PieVIew2 extends SurfaceView implements
        SurfaceHolder.Callback {
    private RectF cakeRect;
    /**
     * 饼图占view空间的比率
     */
    private static final float CAKE_RATE = 0.8f;
    private static final float ANGLE_NUM = 3.6f;
    private static final boolean isDrawByAnim = true;
    /**
     * 文字大小,为了方便用的dp,因为颜色块也用这个尺寸
     */
    private static final float TEXT_SIZE = 16;
    /**
     * 说明文字的行间距
     */
    private static final float TEXT_LINE_SPACE = 20;
    private final int[] ARC_COLORS = new int[]{0xff4F50A0, 0xff649B9A,
            0xffF9BB08, 0xffA4529C, 0xffff6f2f, 0xff990099, 0xff999999,
            0xff663300};
    private Paint paint;
    /**
     * 起始角度
     */
    private float startAngle = 0;

    /**
     * 角数组
     */
    private List<CakeSurfaceView.CakeValue> cakeValues = new ArrayList<CakeSurfaceView.CakeValue>();
    /**
     * 传过来的真正的值(而不是比值)
     */
    private float[] counts;

    /************* 动画 **********/
    private float curAngle;
    /**
     * 当前绘制的项
     */
    private int curItem = 0;
    private float[] itemFrame;
    private SurfaceHolder holder = null;

    /**
     * 旋转展现动画
     */
    private ValueAnimator cakeValueAnimator;
    private int drawCount = 0;
    /**
     * 动画持续时间
     */
    private static final int DURATION = 1500;
    /**********************/

    // /** 布局模式:自动布局,手动布局 */
    // private static final int AUTO_LAYOUT = 0;
    // private static final int HAND_LAYOUT = 1;
    // private int layoutMode = AUTO_LAYOUT;
    /**
     * 靠右模式下,detail的默认宽度
     */
    private static final float DEFAULT_DETAIL_WIDTH_FOR_GRAVITY_RIGHT = 80;
    /*********** 点击 *******/
    private float firstDownX, firstDownY, lastDownX, lastDownY;
    private CakeSurfaceView.OnItemClickListener l;
    /********************/

    /**
     * 点击效果动画
     */
    private ValueAnimator rotaValueAnimator;
    private PropertyValuesHolder rotaValues;
    // 当前点击的item
    private int curClickItem;
    private ValueAnimator highLightValueAnimator;
    private boolean isHighLigntMode = false;
    /**
     * 焦点模式下,向下位移的值
     */
    private float HIGHLIGHT_OFFSET = 20f;
    /**
     * 向下偏移的比率(相对于饼图大小)
     */
    private float OFFSET_RATE = 0.02f;
    /**
     * 模糊色
     */
    private static final int FUZZY_COLOR = 0xff999999;
    /**
     * 饼图与信息的间隔
     */
    private int LEFT_SPACING = 3;
    private int TOP_SPACING = 5;

    private String unitName = "笔";
    private boolean isShowDecimals = true;
    /**
     * 饼图信息排列方式
     */
    private CakeSurfaceView.RankType rankType = CakeSurfaceView.RankType.RANK_BY_ROW;

    /**
     * 是不是在拖动旋转中
     */
    private boolean isDraggingRotation;
    /**
     * 是不是手指拖动中出了饼图的范围
     */
    private boolean isDraggedOutOfBounds = false;
    /**
     * 触发拖动的最小手指按下时间
     */
    private long minDragTimeMillis = 300;
    private long startDownTime;
    /**
     * 记录拖动前的角度
     */
    private float startAngelBeforeDragged = 0;
    /**
     * 滑动动画
     */
    private ValueAnimator swipValueAnimator;
    private PropertyValuesHolder swipValues;
    /**
     * 速度跟踪器
     */
    private VelocityTracker mVelocityTracker;

    public PieVIew2(Context context) {
        super(context);
        init();
    }

    public PieVIew2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 使用渐减interpolator
        holder = this.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);
        this.setFocusable(true);
        this.setBackgroundColor(Color.parseColor("#00ffffff"));
        initValueAnimator();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(dip2px(getContext(), TEXT_SIZE));
        // paint.setColor(ARC_COLORS[0]);
        // getViewTreeObserver().addOnGlobalLayoutListener(
        // new OnGlobalLayoutListener() {
        //
        // @Override
        // public void onGlobalLayout() {
        // Log.v("ts", "这个测到的" + getWidth() + "," + getHeight());
        // getViewTreeObserver()
        // .removeGlobalOnLayoutListener(this);
        // }
        // });

        // this.setOnTouchListener(new OnTouchListener() {
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // // Log.v("ts", "触摸"+event.getAction());
        //
        // return true;
        // }
        // });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstDownX = event.getX();
                firstDownY = event.getY();
                startDownTime = System.currentTimeMillis();
                /**
                 * 速度跟踪. 获取一个VelocityTracker对象, 用完后记得回收
                 * 回收后代表你不需要使用了，系统将此对象在此分配到其他请求者
                 */
                mVelocityTracker = VelocityTracker.obtain();
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                lastDownX = event.getX();
                lastDownY = event.getY();
                mVelocityTracker.addMovement(event);

                float deltaAngle = getAngleByPosition(lastDownX, lastDownY)
                        - getAngleByPosition(firstDownX, firstDownY);
                if (isDraggedOutOfBounds || isDraggingRotation
                        && isOutOfCakeBounds(lastDownX, lastDownY)) { // 拖动中超饼图范围了
                    // startAngelBeforeDragged=0;
                    isDraggedOutOfBounds = true;
                    break;
                }
                if ((isDraggingRotation || (Math.abs(deltaAngle) > 10 && System
                        .currentTimeMillis() - startDownTime > minDragTimeMillis))
                        && !isHighLigntMode
                        && !isOutOfCakeBounds(lastDownX, lastDownY)) { // 拖动动画
                    // Log.i("ts", "deltaAngle:" + deltaAngle);
                    if (startAngelBeforeDragged == 0) {
                        startAngelBeforeDragged = startAngle;
                        Toast.makeText(getContext(), "拖~动~模~式~已~启~动~", Toast.LENGTH_LONG).show();
                        isDraggingRotation = true;
                    }
                    startAngle = deltaAngle + startAngelBeforeDragged;
                    drawCake();
                }

                break;
            case MotionEvent.ACTION_UP:
                /** 处理itemclick */
                float deltaX = Math.abs(lastDownX - firstDownX);
                float deltaY = Math.abs(lastDownY - firstDownY);
                // Log.v("ts", "变化的xy:" + deltaX + "," + deltaY);
                if (deltaX < 10 && deltaY < 10 && null != itemFrame) {

                    int clickPosition = getClickPosition();
                    if (clickPosition == -1)
                        break;
                    if (cakeValues.get(clickPosition).value != 100f) { // 如果只有一个的情况下,就不高亮
                        if (!rotaValueAnimator.isRunning()
                                && !cakeValueAnimator.isRunning()
                                && !highLightValueAnimator.isRunning()
                                && !swipValueAnimator.isRunning()) {
                            if (isHighLigntMode) {
                                isHighLigntMode = false;
                                highLightValueAnimator.reverse();
                                break;
                            } else {
                                isHighLigntMode = true;
                            }

                            curClickItem = clickPosition;
                            float toRotaAngle = 0;
                            float sAngle = startAngle
                                    + (clickPosition > 0 ? itemFrame[clickPosition - 1]
                                    : 0) * ANGLE_NUM;
                            float tAngle = startAngle + itemFrame[clickPosition]
                                    * ANGLE_NUM;
                            // 当前点击的扇形的中心点的角度
                            float curItemCenterAngle = sAngle + (tAngle - sAngle)
                                    / 2;
                            toRotaAngle = startAngle + (90 - curItemCenterAngle);
                            rotaValues = PropertyValuesHolder.ofFloat("rotation",
                                    startAngle, toRotaAngle);
                            rotaValueAnimator.setDuration(Math
                                    .abs((int) (toRotaAngle - startAngle)) * 5);
                            rotaValueAnimator.setValues(rotaValues);
                            rotaValueAnimator.start();
                        }
                    }
                    if (null != l)
                        l.onItemClick(clickPosition);

                }
                /** end */
                /** 处理滑动 */
                else if (!isDraggingRotation) {
                    mVelocityTracker.addMovement(event);
                    /**
                     * 计算当前速度, 其中units是单位表示， 1代表px/毫秒, 1000代表px/秒, ..
                     * maxVelocity此次计算速度你想要的最大值
                     */
                    mVelocityTracker.computeCurrentVelocity(1000);
                    /**
                     * 经过一次computeCurrentVelocity后你就可以用一下几个方法获取此次计算的值 //id是touch
                     * event触摸点的ID, 来为多点触控标识，有这个标识在计算时可以忽略 //其他触点干扰，当然干扰肯定是有的 public
                     * float getXVelocity(); public float getYVelocity(); public
                     * float getXVelocity(int id); public float getYVelocity(int
                     * id);
                     */
                    float velocityX = mVelocityTracker.getXVelocity();
                    float velocityY = mVelocityTracker.getYVelocity();
                    /** x,y轴的移动距离 */
                    float moveX = event.getX() - firstDownX;
                    float moveY = event.getY() - firstDownY;
                    // float absVelocityX = Math.abs(velocityX);
                    // float absVelocityY =
                    // Math.abs(mVelocityTracker.getYVelocity());
                    Log.i("ts", "x轴速度:" + velocityX + ",y轴速度:" + velocityY
                            + "\n移动距离X:" + moveX + ",移动距离Y:" + moveY);
                    dealSwip(velocityX, velocityY, moveX, moveY, firstDownX,
                            firstDownY);
                }
                resetStatus();
                /** end */
                break;
            case MotionEvent.ACTION_CANCEL:
                resetStatus();
                break;
        }
        return true;
    }

    private void dealSwip(float velocityX, float velocityY, float moveX,
                          float moveY, float firstDownX, float firstDownY) {
        if (swipValueAnimator.isRunning() || isHighLigntMode)
            return;
        // 圆心
        float circleX = cakeRect.left + ((cakeRect.right - cakeRect.left) / 2f);
        float circleY = cakeRect.top + ((cakeRect.bottom - cakeRect.top) / 2f);
        float absVelocityX = Math.abs(velocityX);
        float absVelocityY = Math.abs(velocityY);
        float absMoveX = Math.abs(moveX);
        float absMoveY = Math.abs(moveY);
        // Log.i("ts",
        // "圆心:("+circleX+","+circleY+"),初始点:("+firstDownX+","+firstDownY+")");
        // 总速度
        float velocitySum = absVelocityX + absVelocityY;
        // true on 顺时针(用速度)
        boolean direction;
        if (absMoveX > absMoveY) {
            if (firstDownY > circleY) { // 饼图下侧
                // Log.i("ts", "饼图下侧");
                direction = moveX <= 0;
            } else
                direction = moveX > 0;
        } else {
            if (firstDownX > circleX) { // 饼图右侧
                direction = moveY >= 0;
                // Log.i("ts", "饼图右侧");
            } else
                direction = moveY < 0;
        }

        // Log.i("ts", direction ? "顺时针" : "逆时针");
        float toSwipAngle = (absMoveX + absMoveY) / 30
                + (absVelocityX + absVelocityY) / 50;
        if (direction)
            toSwipAngle = toSwipAngle + startAngle;
        else
            toSwipAngle = startAngle - toSwipAngle;
        swipValues = PropertyValuesHolder.ofFloat("swip", startAngle,
                toSwipAngle);
        swipValueAnimator.setDuration((long) (Math.abs(toSwipAngle) * 3));
        swipValueAnimator.setValues(swipValues);
        swipValueAnimator.start();
    }

    /**
     * 重置各种东西
     */
    private void resetStatus() {
        // 回收
        if (null != mVelocityTracker)
            mVelocityTracker.recycle();
        mVelocityTracker = null;
        startAngelBeforeDragged = 0;
        isDraggingRotation = false;
        isDraggedOutOfBounds = false;
    }

    private void initRect(int width, int height) {
        cakeRect = new RectF();

        float cakeSize = Math.min(width, height) * CAKE_RATE;
        cakeRect.set(0, 0, cakeSize, cakeSize);
        // Log.v("ts", "onmeasure");
        HIGHLIGHT_OFFSET = cakeSize * OFFSET_RATE;
        PropertyValuesHolder topHolder = PropertyValuesHolder.ofFloat("top", 0,
                HIGHLIGHT_OFFSET);
        highLightValueAnimator.setValues(topHolder);
    }

    public void setData(List<CakeSurfaceView.CakeValue> cakes) {
        if (null != cakeValues) {
            // 初始化cakeValues;
            float sum = getSum(cakes);
            counts = new float[cakes.size()];
            for (int i = 0; i < cakes.size(); i++) {
                counts[i] = cakes.get(i).value;
                float value = 0;
                // if (i == cakes.size() - 1) {
                // float cakeSum = getSum(cakeValues);
                // value = 100f - cakeSum;
                // Log.v("ts", "value:" + value + ",sum" + cakeSum);
                // } else {
                value = cakes.get(i).value / sum * 100;
                // }
                cakeValues.add(new CakeSurfaceView.CakeValue(cakes.get(i).content, value, cakes
                        .get(i).detail));
            }
            // logCakevalue();
            settleCakeValues(cakeValues.size() - 1);
            // logCakevalue();
            // 初始化itemframe
            itemFrame = new float[cakeValues.size()];
            for (int i = 0; i < cakeValues.size(); i++) {
                if (i == 0) {
                    itemFrame[i] = cakeValues.get(i).value;
                    continue;
                }
                itemFrame[i] = cakeValues.get(i).value + itemFrame[i - 1];
            }
        }
    }

    public void setOnItemClickListener(CakeSurfaceView.OnItemClickListener l) {
        this.l = l;
    }

    /**
     * 设置饼图信息的左间距
     *
     * @param leftSpacing
     */
    public void setDetailLeftSpacing(int leftSpacing) {
        this.LEFT_SPACING = leftSpacing;
    }

    /**
     * 设置饼图与饼图信息的间隔
     *
     * @param topSpacing
     */
    public void setDetailTopSpacing(int topSpacing) {
        this.TOP_SPACING = topSpacing;
    }

    public boolean isShowDecimals() {
        return isShowDecimals;
    }

    /**
     * 是否显示小数
     *
     * @param isShowDecimals
     */
    public void setShowDecimals(boolean isShowDecimals) {
        this.isShowDecimals = isShowDecimals;
    }

    /**
     * 设置饼图信息排列方式
     *
     * @param rankType
     */
    public void setRankType(CakeSurfaceView.RankType rankType) {
        this.rankType = rankType;
    }

    public enum RankType {
        /**
         * 按行排列,每2个换行
         */
        RANK_BY_ROW,
        /**
         * 按1列排序
         */
        RANK_BY_COLUMN
    }

    private float getSum(List<CakeSurfaceView.CakeValue> mCakes) {
        float sum = 0;
        for (int i = 0; i < mCakes.size(); i++) {
            sum += mCakes.get(i).value;
        }
        return sum;
    }

    private float getSum(List<CakeSurfaceView.CakeValue> mCakes, int index) {
        float sum = 0;
        for (int i = 0; i < mCakes.size() && i < index; i++) {
            sum += mCakes.get(i).value;
        }
        return sum;
    }

    /**
     * 使用递归保证cakeValues的值的总和必为100
     *
     * @param i
     */
    private void settleCakeValues(int i) {
        // int i = values.size() - 1;
        float sum = getSum(cakeValues, i);
        CakeSurfaceView.CakeValue value = cakeValues.get(i);
        if (sum <= 100f) {
            value.value = 100f - sum;
            cakeValues.set(i, value);
        } else {
            value.value = 0;
            settleCakeValues(i - 1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        // Log.v("ts", "ondraw");
        if (drawCount == 0 && isDrawByAnim) {
            drawCakeByAnim();
        }
        drawCount = 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        initRect(widthSpecSize, heightSpecSize);
    }

    // private int measureHeight(int measureSpec) {
    // int specMode = MeasureSpec.getMode(measureSpec);
    // int specSize = MeasureSpec.getSize(measureSpec);
    //
    // // Default size if no limits are specified.
    //
    // int result = dip2px(getContext(), 400);
    // // wrap_content的情况触发,这种情况下specSize就是最大能到的尺寸
    // if (specMode == MeasureSpec.AT_MOST) {
    //
    // // Calculate the ideal size of your
    // // control within this maximum size.
    // // If your control fills the available
    // // space return the outer bound.
    //
    // result = specSize;
    // Log.v("ts", "specSize" + specSize);
    // if (layoutMode == HAND_LAYOUT && null != cakeValues
    // && textGravity == Gravity.bottom) {
    // int totalLines = (int) (cakeValues.size() / 2f + 0.5f);
    // int detailHeight = totalLines
    // * dip2px(getContext(), TEXT_LINE_SPACE);
    // int measureHeight = result + detailHeight;
    // Log.v("ts", "文字高度" + detailHeight);
    //
    // result = measureHeight;
    // }
    // } else if (specMode == MeasureSpec.EXACTLY) {
    //
    // // If your control can fit within these bounds return that value.
    // result = specSize;
    // }
    //
    // return result;
    // }
    //
    // private int measureWidth(int measureSpec) {
    // int specMode = MeasureSpec.getMode(measureSpec);
    // int specSize = MeasureSpec.getSize(measureSpec);
    //
    // // Default size if no limits are specified.
    // int result = 340;
    // if (specMode == MeasureSpec.AT_MOST) {
    // // Calculate the ideal size of your control
    // // within this maximum size.
    // // If your control fills the available space
    // // return the outer bound.
    // // result = specSize;
    // }
    //
    // else if (specMode == MeasureSpec.EXACTLY) {
    // // If your control can fit within these bounds return that value.
    //
    // result = specSize;
    // }
    //
    // return result;
    // }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 动画绘制
     */
    private void drawCakeByAnim() {
        // Canvas canvas = null;
        // curItem = 0;
        // canvas = holder.lockCanvas();
        // if (null != canvas) {
        // // 清屏
        // canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        // holder.unlockCanvasAndPost(canvas);
        // }
        cakeValueAnimator.start();

    }

    /**
     * 绘制饼图
     */
    private void drawCake() {
        if (null == itemFrame)
            return;
        Canvas canvas = null;
        Rect lockRect = new Rect();
        cakeRect.round(lockRect);
        canvas = holder.lockCanvas(lockRect);
        if (null != canvas) {
            for (int i = 0; i < cakeValues.size(); i++) {
                int colorIndex = i % ARC_COLORS.length;
                if (i == cakeValues.size() - 1 && colorIndex == 0) {
                    colorIndex = 1;
                }
                paint.setColor(ARC_COLORS[colorIndex]);
                if (i == 0) {
                    canvas.drawArc(cakeRect, startAngle,
                            cakeValues.get(i).value * ANGLE_NUM, true, paint);
                    continue;
                }
                // 如果越界(超过100,即360度),就不画了
                if (itemFrame[i - 1] >= 100
                        || cakeValues.get(i).value + itemFrame[i - 1] > 100) {
                    break;
                }
                canvas.drawArc(cakeRect, startAngle + itemFrame[i - 1]
                                * ANGLE_NUM, cakeValues.get(i).value * ANGLE_NUM, true,
                        paint);
            }
            drawCakeText(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
        // drawDetail();
    }

    private void drawItem(float top, int item) {
        Canvas canvas = null;

        RectF itemRectf = new RectF();
        itemRectf
                .set(cakeRect.left, top, cakeRect.right, top + cakeRect.bottom);
        Rect lockRect = new Rect();
        // itemRectf.round(lockRect);
        lockRect.set((int) cakeRect.left, 0, (int) cakeRect.right,
                40 + (int) cakeRect.bottom);
        canvas = holder.lockCanvas(lockRect);
        if (null != canvas) {
            // 清屏
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            for (int i = 0; i < cakeValues.size(); i++) {
                RectF drawRect;
                int colorIndex = i % ARC_COLORS.length;
                if (i == item) {
                    drawRect = itemRectf;
                } else {
                    drawRect = cakeRect;
                    // if (top != 0){
                    // colorIndex = 6;
                    // }
                }
                if (i == cakeValues.size() - 1 && colorIndex == 0) {
                    colorIndex = 1;
                }
                paint.setColor(ARC_COLORS[colorIndex]);
                if (i == 0) {
                    canvas.drawArc(drawRect, startAngle,
                            cakeValues.get(i).value * ANGLE_NUM, true, paint);
                    if (i != item && top != 0) {
                        paint.setColor(FUZZY_COLOR);
                        paint.setAlpha((int) (top * 200 / HIGHLIGHT_OFFSET));
                        canvas.drawArc(drawRect, startAngle,
                                cakeValues.get(i).value * ANGLE_NUM, true,
                                paint);
                    }
                    continue;
                }
                // 如果越界(超过100,即360度),就不画了
                if (itemFrame[i - 1] >= 100
                        || cakeValues.get(i).value + itemFrame[i - 1] > 100) {
                    break;
                }
                canvas.drawArc(drawRect, startAngle + itemFrame[i - 1]
                                * ANGLE_NUM, cakeValues.get(i).value * ANGLE_NUM, true,
                        paint);
                if (i != item && top != 0) {
                    paint.setColor(FUZZY_COLOR);
                    paint.setAlpha((int) (top * 200 / HIGHLIGHT_OFFSET));
                    canvas.drawArc(drawRect, startAngle + itemFrame[i - 1]
                                    * ANGLE_NUM, cakeValues.get(i).value * ANGLE_NUM,
                            true, paint);
                }
            }
            if (top != 0)
                drawCakeText(canvas, item);
            else
                drawCakeText(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
        if (top == 0) {
            // isHighLigntMode=false;
            canvas = holder.lockCanvas();
            if (null != canvas) {
                // 清屏
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }


    /**
     * 绘制饼图上的文字
     */
    private void drawCakeText(Canvas canvas) {
        // Canvas canvas = null;
        // Rect lockRect = new Rect();
        // cakeRect.round(lockRect);
        // canvas = holder.lockCanvas(lockRect);
        if (null != canvas) {
            paint.setColor(0xffffffff);
            float textSize;
            textSize = (cakeRect.right - cakeRect.left) * 0.03f;
            if (textSize > 18) {
                textSize = 18;
            } else if (textSize < 10) {
                textSize = 10;
            }
            paint.setTextSize(dip2px(getContext(), textSize));
            for (int i = 0; i < cakeValues.size(); i++) {
                float sAngle, tAngle;
                if (i == 0) {
                    sAngle = 0;
                    tAngle = cakeValues.get(i).value;
                } else {
                    sAngle = itemFrame[i - 1];
                    tAngle = itemFrame[i];
                }
                // 小于30度,不写字
                if (tAngle * ANGLE_NUM - sAngle * ANGLE_NUM < 30) {
                    continue;
                }
                float[] position = getArcCenterPosition(startAngle + sAngle
                        * ANGLE_NUM, startAngle + tAngle * ANGLE_NUM);
                String drawTxt = "";
                if (isShowDecimals()) {
                    drawTxt = counts[i] + unitName;
                } else {
                    drawTxt = (int) counts[i] + unitName;
                }
                canvas.drawText(drawTxt, position[0], position[1], paint);
            }
            // holder.unlockCanvasAndPost(canvas);
        }
        paint.setTextSize(dip2px(getContext(), TEXT_SIZE));
    }

    /**
     * 绘制饼图上的文字
     */
    private void drawCakeText(Canvas canvas, int item) {
        // Canvas canvas = null;
        // Rect lockRect = new Rect();
        // cakeRect.round(lockRect);
        // canvas = holder.lockCanvas(lockRect);
        if (null != canvas) {
            paint.setColor(0xffffffff);
            float textSize;
            textSize = (cakeRect.right - cakeRect.left) * 0.03f;
            if (textSize > 18) {
                textSize = 18;
            } else if (textSize < 10) {
                textSize = 10;
            }
            paint.setTextSize(dip2px(getContext(), textSize));
            float sAngle, tAngle;
            if (item == 0) {
                sAngle = 0;
                tAngle = cakeValues.get(item).value;
            } else {
                sAngle = itemFrame[item - 1];
                tAngle = itemFrame[item];
            }
            // // 小于30度,不写字
            // if (tAngle * ANGLE_NUM - sAngle * ANGLE_NUM < 30) {
            // continue;
            // }
            float[] position = getArcCenterPosition(startAngle + sAngle
                    * ANGLE_NUM, startAngle + tAngle * ANGLE_NUM);
            String drawTxt = "";
            if (isShowDecimals()) {
                drawTxt = counts[item] + unitName;
            } else {
                drawTxt = (int) counts[item] + unitName;
            }
            canvas.drawText(drawTxt, position[0], position[1], paint);
            // holder.unlockCanvasAndPost(canvas);
        }
        paint.setTextSize(dip2px(getContext(), TEXT_SIZE));
    }

    private void drawArc() {
        if (null == itemFrame)
            return;
        Canvas canvas = null;
        Rect lockRect = new Rect();
        cakeRect.round(lockRect);
        canvas = holder.lockCanvas(lockRect);
        // 清屏
        // canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        // 绘制已经动画结束的项
        if (null != canvas) {
            for (int i = 0; i < curItem; i++) {
                int colorIndex = i % ARC_COLORS.length;
                if (i == cakeValues.size() - 1 && colorIndex == 0) {
                    colorIndex = 1;
                }
                paint.setColor(ARC_COLORS[colorIndex]);
                if (i == 0) {
                    canvas.drawArc(cakeRect, startAngle,
                            cakeValues.get(i).value * ANGLE_NUM, true, paint);
                    // holder.unlockCanvasAndPost(canvas);
                    continue;
                }
                canvas.drawArc(cakeRect, itemFrame[i - 1] * ANGLE_NUM,
                        cakeValues.get(i).value * ANGLE_NUM, true, paint);
            }
            curItem = getCurItem(curAngle);
            int colorIndex = curItem % ARC_COLORS.length;
            if (curItem == itemFrame.length - 1 && colorIndex == 0) {
                colorIndex = 1;
            }
            paint.setColor(ARC_COLORS[colorIndex]);
            float curStartAngle = 0;
            float curSweepAngle = curAngle;
            if (curItem > 0) {
                curStartAngle = itemFrame[curItem - 1] * ANGLE_NUM;
                curSweepAngle = curAngle - (itemFrame[curItem - 1] * ANGLE_NUM);
            }

            canvas.drawArc(cakeRect, curStartAngle, curSweepAngle, true, paint);
            // 画雷达
            if (curAngle + 5 < 360) {
                // paint.setColor(ARC_COLORS[0]);
                // canvas.drawArc(cakeRect, curAngle + 5, 1, true, paint);
            } else {
                drawCakeText(canvas);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * 根据弧度得到饼图每一项的中心点
     *
     * @param sAngle
     * @param tAngle
     */
    private float[] getArcCenterPosition(float sAngle, float tAngle) {
        // 计算中心角度
        // 如:一个扇形,起始角度为10,结束角度为40,则arc=10+(40-10)/2
        float arc = sAngle + (tAngle - sAngle) / 2;
        float x = (cakeRect.right - cakeRect.left) / 2f;
        float y = (cakeRect.bottom - cakeRect.top) / 2f;
        float angle = arc / 180f;
        double x1, y1;
        x1 = x + x / 2f * Math.cos(angle * Math.PI) + cakeRect.left;
        y1 = y + y / 2f * Math.sin(angle * Math.PI);
        x1 = x1 - dip2px(getContext(), 10);
        float[] res = new float[2];
        res[0] = (float) x1;
        res[1] = (float) y1;
        return res;
    }

    /**
     * 点(x1,y1)是否超出饼图的范围
     *
     * @param x1
     * @param y1
     * @return true if out of bounds
     */
    private boolean isOutOfCakeBounds(float x1, float y1) {
        // 圆心
        float x = cakeRect.left + ((cakeRect.right - cakeRect.left) / 2f);
        float y = cakeRect.top + ((cakeRect.bottom - cakeRect.top) / 2f);
        // 半径
        float r = (cakeRect.right - cakeRect.left) / 2f;
        // 对边长
        float dBian = y1 - y;
        // 邻边
        float lBian = x1 - x;
        // 超出饼图范围
        if (Math.abs(dBian) > r || Math.abs(lBian) > r) {
            Log.i("ts", "超范围了");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据坐标计算点相对圆心的角度
     *
     * @param x1
     * @param y1
     * @return 角度, 当点击的位置超出了圆的范围也能正常运行
     */
    private float getAngleByPosition(float x1, float y1) {
        // 圆心
        float x = cakeRect.left + ((cakeRect.right - cakeRect.left) / 2f);
        float y = cakeRect.top + ((cakeRect.bottom - cakeRect.top) / 2f);
        float r = (cakeRect.right - cakeRect.left) / 2f;
        // 对边长
        float dBian = y1 - y;
        // 邻边
        float lBian = x1 - x;
        // 超出饼图范围
        if (Math.abs(dBian) > r || Math.abs(lBian) > r) {
            return -1f;
        }
        // 如果点到圆心长度超过了半径
        if (Math.sqrt(dBian * dBian + lBian * lBian) > r) {
            return -1f;
        }
        double arc = Math.atan(dBian / lBian);
        double angle;
        if (x1 < x) {
            angle = 180d + TransUtil.radians2angle(arc);
        } else {
            if (y1 > y) {
                angle = TransUtil.radians2angle(arc);
            } else {
                angle = 360d + TransUtil.radians2angle(arc);
            }
        }
        return (float) angle;
    }

    /**
     * 根据给定原角度计算改变startAngle后的当前角度 即计算startAngle为0时,此角度的值
     *
     * @return
     */
    private float getRecoverStartAngle(float angle) {
        // 先还原为360度以内
        float res = angle == 360 ? 360 : angle % 360;
        return res < 0 ? 360 + res : res;
    }

    private int getClickPosition() {
        int clickPosition = -1;
        float clickAngle = getAngleByPosition(firstDownX, firstDownY);
        if (clickAngle != -1) {
            for (int i = 0; i < itemFrame.length; i++) {
                if (i == 0) {
                    float aAngle = startAngle;
                    float bAngle = startAngle + itemFrame[i] * ANGLE_NUM;
                    aAngle = getRecoverStartAngle(aAngle);
                    bAngle = getRecoverStartAngle(bAngle);
                    if (aAngle <= bAngle) {
                        if (clickAngle >= aAngle && clickAngle <= bAngle) {
                            clickPosition = i;
                            break;
                        }
                    } else {
                        if ((clickAngle >= aAngle && clickAngle < 360)
                                || (clickAngle >= 0 && clickAngle <= bAngle)) {
                            clickPosition = i;
                            break;
                        }
                    }
                    continue;
                }
                float aAngle = startAngle + itemFrame[i - 1] * ANGLE_NUM;
                float bAngle = startAngle + itemFrame[i] * ANGLE_NUM;
                aAngle = getRecoverStartAngle(aAngle);
                bAngle = getRecoverStartAngle(bAngle);
                if (aAngle <= bAngle) {
                    if (clickAngle >= aAngle && clickAngle <= bAngle) {
                        clickPosition = i;
                        break;
                    }
                } else {
                    if ((clickAngle >= aAngle && clickAngle < 360)
                            || (clickAngle >= 0 && clickAngle <= bAngle)) {
                        clickPosition = i;
                        break;
                    }
                }
            }
        }
        return clickPosition;
    }

    /**
     * 获得当前绘制的饼.
     *
     * @param curAngle
     * @return
     */
    private int getCurItem(float curAngle) {
        int res = 0;
        for (int i = 0; i < itemFrame.length; i++) {
            if (curAngle <= itemFrame[i] * ANGLE_NUM) {
                res = i;
                break;
            }
        }
        return res;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Log.v("ts", "surface create");
        // 只有刚开始打开的时候播放,防止重播闪烁,当点击home键返回时,直接绘制.
        if (drawCount == 0
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
                && isDrawByAnim) {
            // 因为在surfaceCreated完成之后,还没有onMeasure完成,所以这里的播放动画
            // 移到ondraw完成后哪里了
            // drawCakeByAnim();
            // Log.v("ts", "byanim");
        } else {
            Log.v("ts", "画饼图");
            drawCake();
            // Log.v("ts", "zhijie");
        }

    }

    // 在surface的大小发生改变时触发
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // Log.v("ts", "surface change:" + width + "," + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Log.v("ts", "destory");
        cakeValueAnimator.cancel();
        rotaValueAnimator.cancel();
        swipValueAnimator.cancel();
        highLightValueAnimator.cancel();
        isHighLigntMode = false;
        startAngle = 0;
    }

    private void initValueAnimator() {

        PropertyValuesHolder angleValues = PropertyValuesHolder.ofFloat(
                "angle", 0f, 360f);
        cakeValueAnimator = ValueAnimator.ofPropertyValuesHolder(angleValues);
        cakeValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curAngle = obj2Float(animation.getAnimatedValue("angle"));
                drawArc();
            }
        });
        cakeValueAnimator.setDuration(DURATION);
        cakeValueAnimator.setRepeatCount(0);
        cakeValueAnimator.setInterpolator(new DecelerateInterpolator());
        cakeValueAnimator.setRepeatMode(ValueAnimator.RESTART);

        rotaValues = PropertyValuesHolder.ofFloat("rotation", 0f, 90f);
        rotaValueAnimator = ValueAnimator.ofPropertyValuesHolder(rotaValues);
        rotaValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rota = obj2Float(animation.getAnimatedValue("rotation"));
                startAngle = rota;
                drawCake();
                if (animation.getAnimatedFraction() == 1) {
                    // Log.v("ts", "模式" + isHighLigntMode);
                    highLightValueAnimator.start();
                }
            }
        });
        rotaValueAnimator.setRepeatCount(0);
        rotaValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotaValueAnimator.setInterpolator(new OvershootInterpolator());

        PropertyValuesHolder topValuesHolder = PropertyValuesHolder.ofFloat(
                "top", 0, HIGHLIGHT_OFFSET);
        highLightValueAnimator = ValueAnimator
                .ofPropertyValuesHolder(topValuesHolder);
        highLightValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float top = obj2Float(animation.getAnimatedValue("top"));
                drawItem(top, curClickItem);
            }
        });
        highLightValueAnimator.setDuration(700);
        highLightValueAnimator.setRepeatCount(0);
        highLightValueAnimator.setRepeatMode(ValueAnimator.REVERSE);

        swipValues = PropertyValuesHolder.ofFloat("swip", 0, 90);
        swipValueAnimator = ValueAnimator.ofPropertyValuesHolder(swipValues);
        swipValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rota = obj2Float(animation.getAnimatedValue("swip"));
                startAngle = rota;
                drawCake();
            }
        });
        swipValueAnimator.setInterpolator(new DecelerateInterpolator());
    }

    private float obj2Float(Object o) {
        return ((Number) o).floatValue();
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public static class CakeValue {
        /**
         * 名称
         */
        String content;
        /**
         * 所占百分比
         */
        float value;
        /**
         * 详细描述
         */
        String detail;

        /**
         * @param content 名称
         * @param value   所占百分比
         */
        public CakeValue(String content, float value) {
            this.content = content;
            this.value = value;
        }

        public CakeValue(String content, float value, String detail) {
            this.content = content;
            this.value = value;
            this.detail = detail;
        }
    }

    public void setUnitName(String string) {
        this.unitName = string;
    }
}
