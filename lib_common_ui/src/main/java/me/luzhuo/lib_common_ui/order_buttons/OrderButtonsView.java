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
package me.luzhuo.lib_common_ui.order_buttons;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description: 从左向右, 有进度的按钮
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/11/25 22:09
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class OrderButtonsView extends RecyclerView {
    private List<String> menus = new ArrayList<>();
    private OrderButtonsAdapter adapter = new OrderButtonsAdapter(menus);;

    public OrderButtonsView(@NonNull Context context) {
        this(context, null);
    }

    public OrderButtonsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderButtonsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        this.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.setAdapter(adapter);
    }

    /**
     * 设置当前进度位置
     */
    public void setCurrentIndex(int index) {
        adapter.setCurrentIndex(index);
    }

    /**
     * 设置当前进度位置
     */
    public void setCurrentIndex(String content) {
        adapter.setCurrentIndex(content);
    }

    /**
     * 菜单点击事件
     */
    public void setOnMenuClick(OnMenuClick onMenuClick) {
        adapter.setOnMenuClick(onMenuClick);
    }

    /**
     * 设置菜单数据
     * @param menus
     */
    public void setMenus(List<String> menus) {
        this.menus.clear();
        this.menus.addAll(menus);
        this.adapter.notifyDataSetChanged();
    }
}
