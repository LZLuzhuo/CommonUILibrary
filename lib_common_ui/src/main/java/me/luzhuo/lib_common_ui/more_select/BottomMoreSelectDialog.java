/* Copyright 2021 Luzhuo. All rights reserved.
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
package me.luzhuo.lib_common_ui.more_select;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_common_ui.more_select.bean.MoreSelectData;
import me.luzhuo.lib_common_ui.more_select.callback.MoreSelectListener;

/**
 * Description: 支持N级选择的选择器
 * @Author: Luzhuo
 * @Creation Date: 2021/8/25 23:45
 * @Copyright: Copyright 2021 Luzhuo. All rights reserved.
 **/
public class BottomMoreSelectDialog implements View.OnClickListener, MoreSelectListener {
    private View layout;
    private Dialog dialog;
    private Context context;
    private String title;
    private MoreSelectTitleAdapter titleAdapter;
    private MoreSelectDataAdapter dataAdapter;
    private final List<MoreSelectData> datas = new ArrayList<>();
    private MoreSelectListener listener;

    public BottomMoreSelectDialog(Context context) {
        this.context = context;
        this.layout = LayoutInflater.from(context).inflate(R.layout.ui_more_select_layout, null, false);
        this.dialog = new Dialog(context, R.style.Core_Bottom_Dialog);
        this.layout.findViewById(R.id.more_select_close).setOnClickListener(this);
        this.dialog.setCanceledOnTouchOutside(true);
        this.dialog.setContentView(this.layout);

        // recyclerView
        RecyclerView titleRecyclerView = this.layout.findViewById(R.id.more_select_horizontal_rec);
        RecyclerView dataRecyclerView = this.layout.findViewById(R.id.more_select_vertical_rec);
        titleRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        titleAdapter = new MoreSelectTitleAdapter(datas);
        dataAdapter = new MoreSelectDataAdapter();
        titleRecyclerView.setAdapter(titleAdapter);
        dataRecyclerView.setAdapter(dataAdapter);

        titleAdapter.setMoreSelectListener(this);
        dataAdapter.setMoreSelectListener(this);

        Window window = this.dialog.getWindow();
        window.setGravity(80);
        window.setWindowAnimations(R.style.Core_Bottom_Dialog_UpOrDownAnimation);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.more_select_close) dialog.dismiss();
    }

    /**
     * 显示
     */
    public void show() {
        dialog.show();
    }

    /**
     * 显示
     * 宽度占满, 高度占0.8倍
     */
    public void showAllWith() {
        dialog.show();
        setAllWith();
    }

    /**
     * 关闭显示
     */
    public void dismiss() {
        dialog.dismiss();
    }

    public void setAllWith() {
        // 高度是宽度的0.8倍, 宽度是全局宽
        setAllWidth(0.8f);
    }

    public void setAllWidth(float f2) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        WindowManager.LayoutParams attributes = this.dialog.getWindow().getAttributes();
        attributes.width = defaultDisplay.getWidth();
        attributes.height = (int) (((float) defaultDisplay.getHeight()) * f2);
        this.dialog.getWindow().setAttributes(attributes);
    }

    /**
     * 设置标题
     */
    public BottomMoreSelectDialog setTitle(String title) {
        this.title = title;
        TextView titleTv = this.layout.findViewById(R.id.more_select_title);
        titleTv.setText(title);
        return this;
    }

    /**
     * 设置选择器的标题列表数据
     */
    public BottomMoreSelectDialog setTitleData(List<MoreSelectData> titles) {
        this.datas.clear();
        this.datas.addAll(titles);
        this.titleAdapter.notifyDataSetChanged();
        return this;
    }

    public void setChildData(int position, List<MoreSelectData.ChildData> datas) {
        try {
            final MoreSelectData data = this.datas.get(position);
            data.datas = datas;
            dataAdapter.setData(position, data);
            dataAdapter.notifyDataSetChanged();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public BottomMoreSelectDialog setMoreSelectListener(MoreSelectListener listener) {
        this.listener = listener;
        return this;
    }

    public List<MoreSelectData.ChildData> currentSelect() {
        int currentTitleIndex = titleAdapter.getCurrentIndex();
        final List<MoreSelectData.ChildData> tempDatas = new ArrayList<>();
        for (int i = 0; i < currentTitleIndex; i++) {
            tempDatas.add(datas.get(i).currentSelect);
        }
        return tempDatas;
    }

    public List<MoreSelectData.ChildData> completed() {
        final List<MoreSelectData.ChildData> currentSelect = currentSelect();
        dismiss();
        return currentSelect;
    }

    // ========================================== 回调 ==========================================

    @Override
    public void getChildList(int position, MoreSelectData title, MoreSelectData preTitle) {
        if (listener != null) listener.getChildList(position, title, preTitle);
    }

    @Override
    public void onChildClick(int position, MoreSelectData.ChildData data, int titlePosition, MoreSelectData title, boolean isCompleted) {
        titleAdapter.setTitleIndex(titlePosition);
        if (listener != null) listener.onChildClick(position, data, titlePosition, title, this.datas.size() == titlePosition + 1);
    }
}
