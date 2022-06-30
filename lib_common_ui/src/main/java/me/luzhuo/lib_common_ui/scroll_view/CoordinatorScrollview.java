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
package me.luzhuo.lib_common_ui.scroll_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.widget.NestedScrollView;
import me.luzhuo.lib_core.ui.calculation.UICalculation;

/**
 * Description: 嵌套滚动的 NestedScrollView
 * 只支持嵌套 topView, centerView, bottomView
 * @Author: Luzhuo
 * @Creation Date: 2022/6/14 20:25
 * @Copyright: Copyright 2022 Luzhuo. All rights reserved.
 **/
public class CoordinatorScrollview extends NestedScrollView implements NestedScrollingParent2 {
    private final UICalculation ui;
    public CoordinatorScrollview(@NonNull Context context) {
        this(context, null);
    }

    public CoordinatorScrollview(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoordinatorScrollview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ui = new UICalculation(getContext());
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;
    }

    private int maxScrollY;

    /**
     * 设置最大滑动距离
     */
    public void setMaxScrollY(int maxScrollY) {
        this.maxScrollY = maxScrollY;
    }

    /**
     * @param target   触发嵌套滑动的View
     * @param dx       表示 View 本次 x 方向的滚动的总距离
     * @param dy       表示 View 本次 y 方向的滚动的总距离
     * @param consumed 表示父布局消费的水平和垂直距离
     * @param type     触发滑动事件的类型
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy > 0 && getScrollY() < maxScrollY) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ViewGroup linearLayout = (ViewGroup) getChildAt(0);
        if (linearLayout.getChildCount() != 3) throw new IllegalArgumentException("请按要求编写布局(3个布局) topView centerView bottomView");

        final View topView = linearLayout.getChildAt(0);
        final View centerView = linearLayout.getChildAt(1);
        final View bottomView = linearLayout.getChildAt(2);

        // 设置ViewPager的高度
        centerView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams bottomLayoutParams = bottomView.getLayoutParams();
                bottomLayoutParams.width = ui.getDisplay()[0];
                bottomLayoutParams.height = getHeight() - centerView.getHeight();
                bottomView.setLayoutParams(bottomLayoutParams);
            }
        });

        topView.post(new Runnable() {
            @Override
            public void run() {
                maxScrollY = topView.getHeight();
            }
        });
    }
}
