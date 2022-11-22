/* Copyright 2022 Luzhuo. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.luzhuo.lib_common_ui.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_core.ui.calculation.UICalculation;

/**
 * 单字输入框
 * 适用于: 密码输入框, 验证码输入框
 *
 * 样式:
 * 内置: 下划线, 矩形框
 * 自定义: Drawable
 */
public class EditWordView extends AppCompatEditText {
    public EditWordView(@NonNull Context context) {
        this(context, null);
    }

    public EditWordView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.editTextStyle);
    }

    public EditWordView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private UICalculation ui;
    // 模式: 默认, 下划线, 矩形
    private int mode;
    // 字的数量限制
    private int wordLimitSize;
    // 字的间距
    private float wordSpace;
    // 字的矩形宽高
    private float wordWidth;
    // 是否显示光标
    private boolean showCursor = true;
    // 输入框样式
    private WordShape wordShape;
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        ui = new UICalculation(context);
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.EditWordView, defStyleAttr, 0);
        Drawable background, cursor;
        try {
            mode = typedArray.getInt(R.styleable.EditWordView_edit_mode, 1);
            wordLimitSize = typedArray.getInt(R.styleable.EditWordView_edit_word_length, 6);
            wordSpace = typedArray.getDimension(R.styleable.EditWordView_edit_word_space, ui.dp2px(5));
            wordWidth = typedArray.getDimension(R.styleable.EditWordView_edit_word_width, ui.dp2px(45));
            showCursor = typedArray.getBoolean(R.styleable.EditWordView_edit_cursor_show, true);
            background = typedArray.getDrawable(R.styleable.EditWordView_edit_background);
            cursor = typedArray.getDrawable(R.styleable.EditWordView_edit_cursor);
        } finally {
            typedArray.recycle();
        }

        // 限制输入的长度
        limitWordSize();
        setBackgroundColor(Color.TRANSPARENT);

        // 输入框样式
        if (mode == 1) wordShape = new WordShape(ContextCompat.getDrawable(getContext(), R.drawable.ui_edit_word_rec), ContextCompat.getDrawable(getContext(), R.drawable.ui_edit_word_cursor_rec));
        else if (mode == 2) wordShape = new WordShape(ContextCompat.getDrawable(getContext(), R.drawable.ui_edit_word_line), ContextCompat.getDrawable(getContext(), R.drawable.ui_edit_word_cursor_rec));
        else wordShape = new WordShape(background, cursor);
    }

    private void limitWordSize() {
        setLongClickable(false);
        if (wordLimitSize > 0) setFilters(new InputFilter[]{new InputFilter.LengthFilter(wordLimitSize)});
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        wordShape.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        wordShape.onDraw(canvas);
    }

    @Nullable
    public Editable delete() {
        Editable text = getText();
        if (text == null) return null;

        if (text.length() <= 0) return null;
        return text.delete(text.length() - 1, text.length());
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        return false; // 不显示长按菜单
    }

    public class WordShape {
        private final @Nullable Drawable background;
        private final @Nullable Drawable cursor;

        public WordShape(@Nullable Drawable background, @Nullable Drawable cursor) {
            this.background = background;
            this.cursor = cursor;
        }

        /**
         * 测量大小
         */
        public void setMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);

            if (widthMode == MeasureSpec.AT_MOST) {
                float newWidth = wordWidth /*验证码宽度*/ * wordLimitSize /*验证码个数*/ + (wordLimitSize - 1) * wordSpace /*间距*/;
                EditWordView.this.setMeasuredDimension(MeasureSpec.makeMeasureSpec((int) newWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) wordWidth, MeasureSpec.EXACTLY));
            } else {
                wordWidth/*验证码宽度*/ = ((widthSize - wordSpace * (wordLimitSize - 1)/*减掉间距的长度*/) / wordLimitSize/*验证码个数*/);
                EditWordView.this.setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) wordWidth, MeasureSpec.EXACTLY));
            }
        }

        /**
         * 绘制
         */
        public void onDraw(Canvas canvas) {
            drawBackground(canvas);
            drawText(canvas);
            drawCursor(canvas);
        }

        private void drawBackground(Canvas canvas) {
            if (background == null) throw new IllegalArgumentException("缺少指定的参数");
            int currentIndex = Math.max(getEditableText().length(), 0);
            int count = canvas.save();

            for (int i = 0; i < wordLimitSize; i++) {
                background.setBounds(0, 0, (int) wordWidth, (int) wordWidth);
                if (currentIndex == i) background.setState(new int[]{android.R.attr.state_selected});
                else background.setState(new int[]{android.R.attr.state_enabled});
                background.draw(canvas);
                canvas.translate(wordWidth + wordSpace, 0f);
            }

            canvas.restoreToCount(count);
        }

        private void drawText(Canvas canvas) {
            int count = canvas.save();
            canvas.translate(0f, 0f);
            int textColor = getCurrentTextColor();

            for (int i = 0; i < getEditableText().length(); i++) {
                float textWidth = getPaint().measureText(String.valueOf(getEditableText().charAt(i)));
                final Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
                getPaint().getFontMetrics(fontMetrics);
                getPaint().setColor(textColor);
                float x = (wordWidth + wordSpace) * i + wordWidth / 2f - textWidth / 2f;
                float y = wordWidth / 2f - (fontMetrics.top + fontMetrics.bottom) / 2f;
                canvas.drawText(String.valueOf(getEditableText().charAt(i)), x, y, getPaint());
            }

            canvas.restoreToCount(count);
        }

        private boolean cursorFlag = false;
        private void drawCursor(Canvas canvas) {
            if (!showCursor) return;
            if (cursor == null) throw new IllegalArgumentException("缺少指定的参数");
            cursorFlag = !cursorFlag;
            if (!cursorFlag) return;

            final int currentIndex = Math.max(getEditableText().length(), 0);
            int count = canvas.save();

            int line = getLayout().getLineForOffset(getSelectionStart());
            int top = getLayout().getLineTop(line);
            int bottom = getLayout().getLineBottom(line);
            Rect tempRect = new Rect();
            cursor.getPadding(tempRect);
            cursor.setBounds(0, top - tempRect.top/*0*/, cursor.getIntrinsicWidth()/*6 固有高度*/, bottom + tempRect.bottom);
            canvas.translate(
                    // 边框的中心位置
                    (wordWidth + wordSpace) * currentIndex + wordWidth / 2f - cursor.getIntrinsicWidth() / 2f,
                    // rec - cursor = cursor的topY
                    (wordWidth - cursor.getBounds().height()) / 2f
            );
            cursor.draw(canvas);

            canvas.restoreToCount(count);
        }
    }
}
