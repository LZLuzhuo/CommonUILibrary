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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.FragmentActivity;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_core.app.keyboard.KeyBoardManager;

public class ToolBarView extends ConstraintLayout implements View.OnClickListener, TextView.OnEditorActionListener {
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
    private String hint;
    private String rtext;
    private int rimage;
    private boolean search_editable;
    private boolean have_return;
    private boolean have_voice;
    private OnToolBarCallback callback;
    private OnTextWatcher onTextWatcher;

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ToolBarView, defStyleAttr, defStyleRes);
        try {
            mode = typedArray.getInt(R.styleable.ToolBarView_toolbar_mode, 0);
            title = typedArray.getString(R.styleable.ToolBarView_toolbar_title);
            hint = typedArray.getString(R.styleable.ToolBarView_toolbar_hint);
            rtext = typedArray.getString(R.styleable.ToolBarView_toolbar_rtext);
            rimage = typedArray.getResourceId(R.styleable.ToolBarView_toolbar_rimage, 0);
            search_editable = typedArray.getBoolean(R.styleable.ToolBarView_toolbar_search_editable, true);
            have_return = typedArray.getBoolean(R.styleable.ToolBarView_toolbar_have_return, true);
            have_voice = typedArray.getBoolean(R.styleable.ToolBarView_toolbar_have_voice, false);
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

    private View ui_toolbar_return;
    private TextView ui_toolbar_title;
    private TextView ui_toolbar_rtext;
    private ImageView ui_toolbar_rimage;
    private void initMode0() {
        ui_toolbar_return = findViewById(R.id.ui_toolbar_return);
        ui_toolbar_title = findViewById(R.id.ui_toolbar_title);
        ui_toolbar_rtext = findViewById(R.id.ui_toolbar_rtext);
        ui_toolbar_rimage = findViewById(R.id.ui_toolbar_rimage);

        ui_toolbar_return.setOnClickListener(this);
        ui_toolbar_rtext.setOnClickListener(this);
        ui_toolbar_rimage.setOnClickListener(this);

        setHaveReturn(have_return);
        setTitle(title);
        setRText(rtext);
        setRImage(rimage);
    }

    /**
     * ????????????
     * ?????? Title ???????????????
     */
    public void setTitle(CharSequence title) {
        if (ui_toolbar_title == null) return;

        ui_toolbar_title.setText(title);
    }

    /**
     * ???????????????????????????
     */
    public void setRText(CharSequence rtext) {
        if (ui_toolbar_rtext == null) return;

        if (TextUtils.isEmpty(rtext)) ui_toolbar_rtext.setVisibility(GONE);
        else ui_toolbar_rtext.setVisibility(VISIBLE);

        ui_toolbar_rtext.setText(rtext);
    }

    /**
     * ???????????????????????????
     * ?????? Title ???????????????
     */
    public void setRImage(@DrawableRes int rimage) {
        if (ui_toolbar_rimage == null) return;

        if (rimage == 0) ui_toolbar_rimage.setVisibility(INVISIBLE);
        else ui_toolbar_rimage.setVisibility(VISIBLE);

        ui_toolbar_rimage.setImageResource(rimage);
    }

    /**
     * ????????????????????????
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

        ui_toolbar_return.setOnClickListener(this);
        ui_toolbar_rtext.setOnClickListener(this);
        ui_toolbar_voice.setOnClickListener(this);
        ui_toolbar_content.addTextChangedListener(textWatcher);
        ui_toolbar_content.setOnEditorActionListener(this);

        setHaveReturn(have_return);
        setRText(rtext);
        setContent("");
        setContentEditable(search_editable);
        setHaveVoice(have_voice);
    }

    /**
     * ??????????????????
     * @param hint ????????????
     */
    public void setHint(String hint) {
        if (ui_toolbar_content == null) return;

        this.hint = hint;
        ui_toolbar_content.setHint(hint);
    }

    /**
     * ??????????????????????????????
     * ??????????????????????????????, ?????????????????????????????????
     */
    public void setContent(CharSequence content) {
        if (ui_toolbar_content == null) return;

        if (TextUtils.isEmpty(content)) ui_toolbar_content.setHint(hint);
        else ui_toolbar_content.setText(content);
    }

    /**
     * ??????????????????????????????
     */
    public String getContent() {
        return ui_toolbar_content.getText().toString().trim();
    }

    /**
     * ????????????????????????????????????
     * ??????????????????????????????, ????????????????????????
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
            // ??????????????????, ???????????????????????????????????????, ??????????????????????????????????????????
            ui_toolbar_content.setOnClickListener(this);
        }
    }

    /**
     * ??????????????????
     */
    public void setOnToolBarCallback(OnToolBarCallback callback) {
        this.callback = callback;
    }

    /**
     * ???????????????????????????
     */
    public void setOnTextWatcher(OnTextWatcher onTextWatcher) {
        this.onTextWatcher = onTextWatcher;
    }

    @Override
    public void onClick(View v) {
        if (callback == null) return;

        if (v == ui_toolbar_return) {
            final boolean isclose = callback.onReturn();
            if (isclose) ((FragmentActivity) getContext()).finish();
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

    private TextWatcher textWatcher = new TextWatcherImpl(){
        @Override
        public void afterTextChanged(Editable s) {
            if (onTextWatcher != null) onTextWatcher.onTextWatcher(s.subSequence(0, s.length()));
        }
    };
}
