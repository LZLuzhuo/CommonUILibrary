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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_common_ui.more_select.bean.MoreSelectData;
import me.luzhuo.lib_common_ui.more_select.callback.MoreSelectListener;

/**
 * Description: 子数据适配器
 * @Author: Luzhuo
 * @Creation Date: 2021/8/25 23:48
 * @Copyright: Copyright 2021 Luzhuo. All rights reserved.
 **/
class MoreSelectDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MoreSelectData.ChildData> mDatas = new ArrayList<>();
    private Context context;
    private MoreSelectListener listener;

    private MoreSelectData titleData;
    private int titleIndex;

    public void setData(int titleIndex, MoreSelectData titleData) {
        this.titleIndex = titleIndex;
        this.titleData = titleData;
        this.mDatas.clear();
        this.mDatas.addAll(titleData.datas);
        notifyDataSetChanged();
    }

    public void setMoreSelectListener(MoreSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new RecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_more_select_item_data, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecyclerHolder) holder).bindData(mDatas.get(position));
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView more_select_data;

        public RecyclerHolder(View itemView) {
            super(itemView);
            more_select_data = itemView.findViewById(R.id.more_select_data);

            more_select_data.setOnClickListener(this);
        }

        public void bindData(MoreSelectData.ChildData data) {
            more_select_data.setText(data.data);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.more_select_data) {
                titleData.currentSelect = mDatas.get(getLayoutPosition());
                if (listener != null) listener.onChildClick(getLayoutPosition(), mDatas.get(getLayoutPosition()), titleIndex, titleData, false);
            }
        }
    }
}
