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
package me.luzhuo.lib_common_ui.check_button;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import me.luzhuo.lib_common_ui.R;

/**
 * Description: 圆形的勾选按钮
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/11/11 10:32
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class CheckButton extends RelativeLayout {
    private View check_button_view;
    private ImageView checked_icon;
    private onClickCallback callback;

    // parameter
    private Boolean isCheck = false;
    private Boolean checkable = true;

    public CheckButton(Context context) {
        this(context, null);
    }

    public CheckButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParameter(attrs, defStyleAttr);
        initView();
    }

    private void initParameter(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CheckButton, defStyleAttr, 0);

        try {
            isCheck = typedArray.getBoolean(R.styleable.CheckButton_check_check, false);
            checkable = typedArray.getBoolean(R.styleable.CheckButton_check_checkable, true);
        }
        finally { typedArray.recycle(); }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.ui_check_button, this, true);
        check_button_view = findViewById(R.id.check_button_view);
        checked_icon = findViewById(R.id.checked_icon);

        check_button_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkable) return;

                isCheck = !isCheck;
                updateView();

                if (callback != null) callback.onClick(isCheck);
            }
        });
    }

    /**
     * 进行选择
     */
    public void check(boolean check) {
        this.isCheck = check;
        updateView();
    }

    /**
     * 是否选择
     */
    public boolean check() {
        return isCheck;
    }

    /**
     * 更新界面
     */
    private void updateView() {
        if (isCheck) {
            check_button_view.setBackgroundResource(R.drawable.ui_check_button_checked);
            checked_icon.setVisibility(View.VISIBLE);
        } else {
            check_button_view.setBackgroundResource(R.drawable.ui_check_button_default);
            checked_icon.setVisibility(View.INVISIBLE);
        }
    }

    public interface onClickCallback {
        public void onClick(boolean isCheck);
    }
    public void setOnClickCallback(onClickCallback callback) {
        this.callback = callback;
    }
}
