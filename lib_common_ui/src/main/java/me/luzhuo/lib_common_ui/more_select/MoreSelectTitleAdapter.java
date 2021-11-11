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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_common_ui.more_select.bean.MoreSelectData;
import me.luzhuo.lib_common_ui.more_select.callback.MoreSelectListener;

/**
 * Description: 标题数据适配器
 * @Author: Luzhuo
 * @Creation Date: 2021/8/25 23:50
 * @Copyright: Copyright 2021 Luzhuo. All rights reserved.
 **/
class MoreSelectTitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MoreSelectData> mDatas;
    private static final int TYPE_UnSelect = 1, TYPE_Select = 2, TYPE_None = 3;
    private int currentIndex = 0;
    private MoreSelectListener listener;

    public MoreSelectTitleAdapter(List<MoreSelectData> datas) {
        this.mDatas = datas;
    }

    public void setMoreSelectListener(MoreSelectListener listener) {
        this.listener = listener;
    }

    public void setTitleIndex(int titleIndex) {
        this.currentIndex = titleIndex + 1;
        notifyDataSetChanged();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_UnSelect:
                return new UnSelectHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_more_select_item_title_unselect, parent, false));
            case TYPE_Select:
                return new SelectHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_more_select_item_title_select, parent, false));
            default:
                return new NoneHolder(new ImageView(parent.getContext()));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == currentIndex) {
            return TYPE_UnSelect;
        } else if (position < currentIndex) {
            return TYPE_Select;
        } else {
            return TYPE_None;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_UnSelect:
                ((UnSelectHolder) holder).bindData(mDatas.get(position));
                break;
            case TYPE_Select:
                ((SelectHolder) holder).bindData(mDatas.get(position));
                break;
            default:
                ((NoneHolder) holder).bindData();
                break;
        }
    }

    public class UnSelectHolder extends RecyclerView.ViewHolder {
        public TextView more_select_unselect;

        public UnSelectHolder(View itemView) {
            super(itemView);
            more_select_unselect = itemView.findViewById(R.id.more_select_unselect);
        }

        public void bindData(MoreSelectData data) {
            more_select_unselect.setText(data.hintTitle);

            if (listener != null) {
                final MoreSelectData preData = getLayoutPosition() > 0 ? mDatas.get(getLayoutPosition() - 1) : null;
                listener.getChildList(getLayoutPosition(), mDatas.get(getLayoutPosition()), preData);
            }
        }
    }

    public class SelectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView more_select_select;

        public SelectHolder(View itemView) {
            super(itemView);
            more_select_select = itemView.findViewById(R.id.more_select_select);

            more_select_select.setOnClickListener(this);
        }

        public void bindData(MoreSelectData data) {
            more_select_select.setText(data.currentSelect.data);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.more_select_select) {
                currentIndex = getLayoutPosition();
                notifyDataSetChanged();
            }
        }
    }

    public class NoneHolder extends RecyclerView.ViewHolder {
        public NoneHolder(View itemView) {
            super(itemView);
        }
        public void bindData() { }
    }
}
