/* Copyright 2020 Luzhuo. All rights reserved.
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
package me.luzhuo.lib_common_ui.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.FragmentActivity;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_core.app.color.ColorManager;
import me.luzhuo.lib_core.app.keyboard.KeyBoardManager;
import me.luzhuo.lib_core.ui.calculation.UICalculation;

public class ToolBarView extends ConstraintLayout implements View.OnClickListener, TextView.OnEditorActionListener {
    private ColorManager color;
    private UICalculation ui;
    public ToolBarView(@NonNull Context context) {
        this(context, null);
    }
    public ToolBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ToolBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }
    public ToolBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs, defStyleAttr, defStyleRes);
    }

    private int mode;
    private String title;
    private int title_color;
    private int title_size;
    private String hint;
    private String rtext;
    private int rtext_color;
    private int rtext_size;
    private int rimage;
    private int return_image;
    private boolean search_editable;
    private int search_maxLength;
    private boolean have_return;
    private boolean have_voice;
    private int background_color;
    private OnToolBarCallback callback;
    private OnTextWatcher onTextWatcher;

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        color = new ColorManager(getContext());
        ui = new UICalculation(getContext());
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ToolBarView, defStyleAttr, defStyleRes);
        try {
            mode = typedArray.getInt(R.styleable.ToolBarView_toolbar_mode, 0);
            title = typedArray.getString(R.styleable.ToolBarView_toolbar_title);
            title_color = typedArray.getColor(R.styleable.ToolBarView_toolbar_title_color, color.getTextColorPrimary());
            title_size = (int) typedArray.getDimension(R.styleable.ToolBarView_toolbar_title_size, ui.dp2px(17));
            hint = typedArray.getString(R.styleable.ToolBarView_toolbar_hint);
            rtext = typedArray.getString(R.styleable.ToolBarView_toolbar_rtext);
            rtext_color = typedArray.getColor(R.styleable.ToolBarView_toolbar_rtext_color, color.getTextColorDefault());
            rtext_size = (int) typedArray.getDimension(R.styleable.ToolBarView_toolbar_rtext_size, ui.dp2px(14));
            rimage = typedArray.getResourceId(R.styleable.ToolBarView_toolbar_rimage, 0);
            return_image = typedArray.getResourceId(R.styleable.ToolBarView_toolbar_return_image, R.mipmap.ui_toolbar_return);
            search_editable = typedArray.getBoolean(R.styleable.ToolBarView_toolbar_search_editable, true);
            search_maxLength = typedArray.getInt(R.styleable.ToolBarView_toolbar_search_maxLength, 5000);
            have_return = typedArray.getBoolean(R.styleable.ToolBarView_toolbar_have_return, true);
            have_voice = typedArray.getBoolean(R.styleable.ToolBarView_toolbar_have_voice, false);
            background_color = typedArray.getColor(R.styleable.ToolBarView_toolbar_background_color, color.getColorForeground());
        } finally {
            typedArray.recycle();
        }

        if (mode == 0) {
            LayoutInflater.from(getContext()).inflate(R.layout.ui_toolbar_title_return, this, true);
            initMode0();
        } else if (mode == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.ui_toolbar_search_return, this, true);
            initMode1();
        }
    }

    private ImageView ui_toolbar_return;
    private TextView ui_toolbar_title;
    private TextView ui_toolbar_rtext;
    private ImageView ui_toolbar_rimage;
    private ConstraintLayout ui_toolbar_parent;
    private void initMode0() {
        ui_toolbar_return = findViewById(R.id.ui_toolbar_return);
        ui_toolbar_title = findViewById(R.id.ui_toolbar_title);
        ui_toolbar_rtext = findViewById(R.id.ui_toolbar_rtext);
        ui_toolbar_rimage = findViewById(R.id.ui_toolbar_rimage);
        ui_toolbar_parent = findViewById(R.id.ui_toolbar_parent);

        ui_toolbar_return.setOnClickListener(this);
        ui_toolbar_rtext.setOnClickListener(this);
        ui_toolbar_rimage.setOnClickListener(this);

        setHaveReturn(have_return);
        setTitle(title);
        setTitleColor(title_color);
        setTitleSize(title_size, false);
        setRText(rtext);
        setRTextColor(rtext_color);
        setRTextSize(rtext_size, false);
        setRImage(rimage);
        setReturnImage(return_image);
        setBackgroundColor(background_color);
    }

    /**
     * 设置标题
     * 仅在 Title 模式下有效
     */
    public void setTitle(CharSequence title) {
        if (ui_toolbar_title == null) return;

        ui_toolbar_title.setText(title);
    }

    /**
     * 设置右侧按钮的文本
     */
    public void setRText(CharSequence rtext) {
        if (ui_toolbar_rtext == null) return;

        if (TextUtils.isEmpty(rtext)) ui_toolbar_rtext.setVisibility(GONE);
        else ui_toolbar_rtext.setVisibility(VISIBLE);

        ui_toolbar_rtext.setText(rtext);
    }

    /**
     * 设置右侧按钮的文本颜色
     * @param color A color value in the form 0xAARRGGBB.
     */
    public void setRTextColor(@ColorInt int color) {
        if (ui_toolbar_rtext == null) return;
        ui_toolbar_rtext.setTextColor(color);
    }

    public void setTitleColor(@ColorInt int color) {
        if (ui_toolbar_title == null) return;
        ui_toolbar_title.setTextColor(color);
    }

    public void setTitleSize(int sp) {
        setTitleSize(sp, true);
    }

    public void setTitleSize(int sp, boolean isSp) {
        if (ui_toolbar_title == null) return;
        ui_toolbar_title.setTextSize(isSp ? TypedValue.COMPLEX_UNIT_SP : TypedValue.COMPLEX_UNIT_PX, sp);
    }

    public void setRTextSize(int sp) {
        setRTextSize(sp, true);
    }

    public void setRTextSize(int sp, boolean isSp) {
        if (ui_toolbar_rtext == null) return;
        ui_toolbar_rtext.setTextSize(isSp ? TypedValue.COMPLEX_UNIT_SP : TypedValue.COMPLEX_UNIT_PX, sp);
    }

    /**
     * 设置右侧按钮的图片
     * 仅在 Title 模式下有效
     */
    public void setRImage(@DrawableRes int rimage) {
        if (ui_toolbar_rimage == null) return;

        if (rimage == 0) ui_toolbar_rimage.setVisibility(INVISIBLE);
        else ui_toolbar_rimage.setVisibility(VISIBLE);

        ui_toolbar_rimage.setImageResource(rimage);
    }

    public void setReturnImage(@DrawableRes int rimage) {
        if (ui_toolbar_return == null) return;

        ui_toolbar_return.setImageResource(rimage);
    }

    /**
     * 是否显示返回按钮
     */
    public void setHaveReturn(boolean haveReturn) {
        if (ui_toolbar_return == null) return;

        if (haveReturn) ui_toolbar_return.setVisibility(VISIBLE);
        else ui_toolbar_return.setVisibility(GONE);
    }

    private AppCompatEditText ui_toolbar_content;
    private View ui_toolbar_voice;
    private void initMode1() {
        ui_toolbar_return = findViewById(R.id.ui_toolbar_return);
        ui_toolbar_rtext = findViewById(R.id.ui_toolbar_rtext);
        ui_toolbar_content = findViewById(R.id.ui_toolbar_content);
        ui_toolbar_voice = findViewById(R.id.ui_toolbar_voice);
        ui_toolbar_parent = findViewById(R.id.ui_toolbar_parent);

        ui_toolbar_return.setOnClickListener(this);
        ui_toolbar_rtext.setOnClickListener(this);
        ui_toolbar_voice.setOnClickListener(this);
        ui_toolbar_content.addTextChangedListener(textWatcher);
        ui_toolbar_content.setOnEditorActionListener(this);

        setHaveReturn(have_return);
        setRText(rtext);
        setContent("");
        setHint(hint);
        setContentEditable(search_editable);
        setHaveVoice(have_voice);
        setBackgroundColor(background_color);
        setSearchMaxLength(search_maxLength);
    }

    /**
     * 设置提示文本
     * @param hint 提示文本
     */
    public void setHint(String hint) {
        if (ui_toolbar_content == null) return;
        if (hint == null) return;

        this.hint = hint;
        ui_toolbar_content.setHint(hint);
    }

    /**
     * 设置搜索框的本文内容
     */
    public void setContent(CharSequence content) {
        if (ui_toolbar_content == null) return;

        ui_toolbar_content.setText(content);
    }

    /**
     * 获取搜索框的文本内容
     */
    public String getContent() {
        return ui_toolbar_content.getText().toString().trim();
    }

    /**
     * 设置是否含有语音识别功能
     * 仅显示语音识别的按钮, 不提供实质的功能
     */
    public void setHaveVoice(boolean haveVoice) {
        if (ui_toolbar_voice == null) return;

        if (haveVoice) ui_toolbar_voice.setVisibility(VISIBLE);
        else ui_toolbar_voice.setVisibility(GONE);
    }

    public void setContentEditable(boolean editable) {
        if (ui_toolbar_content == null) return;

        if (editable) {
            ui_toolbar_content.setFocusableInTouchMode(true);
            ui_toolbar_content.setCursorVisible(true);
        } else {
            ui_toolbar_content.setFocusable(false);
            ui_toolbar_content.setCursorVisible(false);
            // 设置点击事件, 会让搜索事件的键盘无法回弹, 所以放在不需要输入的这个地方
            ui_toolbar_content.setOnClickListener(this);
        }
    }

    /**
     * 设置回调监听
     */
    public void setOnToolBarCallback(OnToolBarCallback callback) {
        this.callback = callback;
    }

    /**
     * 设置输入内容的观察
     */
    public void setOnTextWatcher(OnTextWatcher onTextWatcher) {
        this.onTextWatcher = onTextWatcher;
    }

    /**
     * 设置背景颜色
     * @param color
     */
    @Override
    public void setBackgroundColor(int color) {
        ui_toolbar_parent.setBackgroundColor(color);
    }

    private void setSearchMaxLength(int search_maxLength) {
        ui_toolbar_content.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(search_maxLength) });
    }

    @Override
    public void onClick(View v) {
        if (callback == null && v == ui_toolbar_return) ((FragmentActivity) getContext()).finish();
        if (callback == null) return;

        if (v == ui_toolbar_return) {
            final boolean isUserClose = callback.onReturn();
            if (!isUserClose) ((FragmentActivity) getContext()).finish();
        }
        else if (v == ui_toolbar_rtext || v == ui_toolbar_rimage) callback.onRightButton();
        else if (v == ui_toolbar_voice) callback.onVoice();
        else if (v == ui_toolbar_content) callback.onContentClick();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            KeyBoardManager.getInstance(getContext()).hide(ui_toolbar_content);
            if (callback != null) callback.onSearch(v.getText());
            return true;
        }
        return false;
    }

    private final TextWatcher textWatcher = new TextWatcherImpl(){
        @Override
        public void afterTextChanged(Editable s) {
            if (onTextWatcher != null) onTextWatcher.onTextWatcher(s.subSequence(0, s.length()));
        }
    };
}
