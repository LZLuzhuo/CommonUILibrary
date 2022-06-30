package me.luzhuo.lib_common_ui.index_bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_common_ui.index_bar.style.DefaultStyle;
import me.luzhuo.lib_core.app.color.ColorManager;
import me.luzhuo.lib_core.ui.calculation.UICalculation;

/**
 * 索引View
 */
public class IndexBarView extends View {
    public IndexBarView(Context context) {
        this(context, null);
    }

    public IndexBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, defStyleAttr);
    }

    private void init(@Nullable AttributeSet attrs, int defStyleAttr) {
        UICalculation ui = new UICalculation(getContext());
        ColorManager color = new ColorManager(getContext());
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.IndexBarView, defStyleAttr, 0);
        try {
            normalTextSize = typedArray.getDimension(R.styleable.IndexBarView_index_text_size_normal, ui.sp2px(14f));
            pressedTextSize = typedArray.getDimension(R.styleable.IndexBarView_index_text_size_pressed, ui.sp2px(14f));
            normalTextColor = typedArray.getColor(R.styleable.IndexBarView_index_text_color_normal, color.getTextColorDefault());
            pressedTextColor = typedArray.getColor(R.styleable.IndexBarView_index_text_color_pressed, color.getTextColorPrimary());
            textSpace = typedArray.getDimension(R.styleable.IndexBarView_index_text_space, ui.dp2px(5f));
        } finally {
            typedArray.recycle();
        }

        setNormalStyle(new DefaultStyle(normalTextSize, normalTextColor));
        setPressedStyle(new DefaultStyle(pressedTextSize, pressedTextColor));
    }

    public void setNormalStyle(@NonNull IndexBarStyle normalStyle) {
        this.normalStyle = normalStyle;
        invalidate();
    }

    public void setPressedStyle(@NonNull IndexBarStyle pressedStyle) {
        this.pressedStyle = pressedStyle;
        invalidate();
    }

    private String[] indexStrings = {"↑", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private float normalTextSize = 15f; // ox
    private int normalTextColor = Color.BLACK;
    private float pressedTextSize = 15f; // sp
    private int pressedTextColor = Color.WHITE;
    private float textSpace = 10f; // dp

    @Nullable
    private OnIndexBarCallback callback;
    @NonNull
    private IndexBarStyle normalStyle, pressedStyle;

    private final Rect indexRect = new Rect();
    private int measureWidth = 0;
    private int measureHeight = 0;
    private int measureTextSize = 0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 计算文字占用的宽高, 以占用最大的文字的宽高为准
        measureHeight = 0;
        measureWidth = 0;
        for (int i = 0; i < indexStrings.length; i++) {
            final String indexStr = indexStrings[i];
            // normalPaint.getTextBounds(indexStr, 0, indexStr.length(), indexRect);
            normalStyle.getPaint().getTextBounds(indexStr, 0, indexStr.length(), indexRect);
            measureWidth = Math.max(indexRect.width(), measureWidth);
            measureHeight = Math.max(indexRect.height(), measureHeight);
        }
        measureTextSize = measureHeight;
        // 计算所有文字占用的总高
        measureHeight *= indexStrings.length;
        // 加上每个文件之间的字间距
        measureHeight += (textSpace * (indexStrings.length - 1));

        // 加上padding
        measureHeight += (getPaddingTop() + getPaddingBottom());
        measureWidth += (getPaddingLeft() + getPaddingRight());

        // 调整宽高
        if (withMode == MeasureSpec.EXACTLY) measureWidth = withSize;
        else if (withMode == MeasureSpec.AT_MOST) measureWidth = Math.min(measureWidth, withSize);
        if (heightMode == MeasureSpec.EXACTLY) {
            measureHeight = heightSize;
            // 如果是全部局部的话, 需要充值 textSpace
            resetTextSpace();
        } else if (heightMode == MeasureSpec.AT_MOST) measureHeight = Math.min(measureHeight, heightSize);

        setMeasuredDimension(measureWidth, measureHeight);
    }

    private void resetTextSpace() {
        textSpace = ((float) measureHeight - getPaddingTop() - getPaddingBottom() - measureTextSize * indexStrings.length) / indexStrings.length;
    }

    private int currentIndex = -1; // 当前选中的索引
    @Override
    protected void onDraw(Canvas canvas) {
        // 减去Padding值, 才是真正需要绘制的区域
        int with = measureWidth - getPaddingLeft() - getPaddingRight();
        int height = measureHeight - getPaddingTop() - getPaddingBottom();
        // 绘制区域的坐标
        int startX = getPaddingLeft();
        int startY = getPaddingTop();
        int endX = startX + with;
        int endY = startY + height;


        // --- 文字区域 (区域)
        /*Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(startX, startY, endX, endY, paint);*/
        // ---

        for (int i = 0; i < indexStrings.length; i++) {

            // 字母区域
            int space = (int) (textSpace * i);
            int rectStartX = startX;
            int rectStartY = measureTextSize * i + space + startY;
            int rectEndX = endX;
            int rectEndY = measureTextSize * (i + 1) + space + startY;

            // --- 文字框 (线框)
            /*Paint paint1 = new Paint();
            paint1.setColor(Color.BLUE);
            paint1.setStrokeWidth(1f);
            paint1.setStyle(Paint.Style.STROKE);
            canvas.drawRect(rectStartX, rectStartY, rectEndX, rectEndY, paint1);*/
            // ---

            // 绘制文字
            final String indexStr = indexStrings[i];
            if (i == currentIndex) {
                pressedStyle.onPressed(this, canvas, i, indexStr, rectStartX, rectStartY, rectEndX, rectEndY);
            } else {
                normalStyle.onPressed(this, canvas, i, indexStr, rectStartX, rectStartY, rectEndX, rectEndY);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            int pressedId = (int) ((event.getY() - getPaddingTop()) / (measureTextSize + textSpace));
            if (pressedId < 0) pressedId = 0;
            else if (pressedId >= indexStrings.length) pressedId = indexStrings.length - 1;

            currentIndex = pressedId;
            invalidate();
            if (pressedId > -1 && pressedId < indexStrings.length) {
                if (callback != null) callback.onPressing(currentIndex, indexStrings[currentIndex]);
            }
        } else {
            currentIndex = -1;
            invalidate();
            if (callback != null) callback.onPressed();
        }
        return true;
    }

    public interface OnIndexBarCallback {
        /**
         * 按下
         * @param index 索引
         * @param indexStr 索引处的文字
         */
        public void onPressing(int index, String indexStr);

        /**
         * 按下结束
         */
        public void onPressed();
    }

    public void setOnIndexBarCallback(OnIndexBarCallback callback) {
        this.callback = callback;
    }

    /**
     * 容器的宽, 含padding
     */
    public int getMeasureWidth() {
        return this.measureWidth;
    }

    /**
     * 容器的高, 含padding
     */
    public int getMeasureHeight() {
        return this.measureHeight;
    }

    /**
     * normal文字的大小
     */
    public int getMeasureTextSize() {
        return this.measureTextSize;
    }

    public float getTextSpace() {
        return this.textSpace;
    }

    public float getNormalTextSize() {
        return this.normalTextSize;
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public float getPressedTextSize() {
        return pressedTextSize;
    }

    public int getPressedTextColor() {
        return pressedTextColor;
    }

    public void setIndexStrings(String[] indexStrings) {
        this.indexStrings = indexStrings;
        invalidate();
    }

    /**
     * 按压的样式
     */
    public interface IndexBarStyle {
        Paint getPaint();

        /**
         * 按压后的样式
         * @param canvas Canvas
         * @param index 索引位置
         * @param indexStr 字母
         * @param rectStartX 字母矩形 左上角x坐标
         * @param rectStartY 字母矩形 左上角y坐标
         * @param rectEndX 字母矩形 右下角x坐标
         * @param rectEndY 字母矩形 右下角y坐标
         */
        void onPressed(IndexBarView view, Canvas canvas, int index, String indexStr, int rectStartX, int rectStartY, int rectEndX, int rectEndY);
    }
}
