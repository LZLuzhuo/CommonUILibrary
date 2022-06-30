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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import me.luzhuo.lib_common_ui.R;

import static me.luzhuo.lib_common_ui.order_buttons.OrderButtonState.Did;
import static me.luzhuo.lib_common_ui.order_buttons.OrderButtonState.Doing;
import static me.luzhuo.lib_common_ui.order_buttons.OrderButtonState.Ready;

/**
 * Description: 有序菜单适配器
 * @Author: Luzhuo
 * @Creation Date: 2020/11/25 22:13
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
class OrderButtonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mDatas;
    private Context context;
    private OnMenuClick callback;
    private int currentIndex = 0;
    private static final int TYPE_Header = 1, TYPE_Middle = 2, Type_Bottom = 3; // 头部, 中间, 尾部
    private final int[] startRes = {R.mipmap.ui_order_button_icon_horizontal_buttons_undo_start, R.mipmap.ui_order_button_icon_horizontal_buttons_doing_start, R.mipmap.ui_order_button_icon_horizontal_buttons_did_start};
    private final int[] middleRes = {R.mipmap.ui_order_button_icon_horizontal_buttons_undo, R.mipmap.ui_order_button_icon_horizontal_buttons_doing, R.mipmap.ui_order_button_icon_horizontal_buttons_did};
    private final int[] endRes = {R.mipmap.ui_order_button_icon_horizontal_buttons_undo_end, R.mipmap.ui_order_button_icon_horizontal_buttons_doing_end, R.mipmap.ui_order_button_icon_horizontal_buttons_did_end};

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }

    public void setCurrentIndex(String content) {
        try {
            for (int i = 0; i < mDatas.size(); i++) {
                if (content.equals(mDatas.get(i))) {
                    setCurrentIndex(i);
                    return;
                }
            }
        } catch (Exception e) {
            setCurrentIndex(0);
        }
    }

    public OrderButtonsAdapter(List<String> datas){
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        switch (viewType) {
            case Type_Bottom:
                return new RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.ui_order_buttons_item_bottom, parent, false));
            default:
                return new RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.ui_order_buttons_item_header, parent, false));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_Header;
        else if (position == mDatas.size() - 1) return Type_Bottom;
        else return TYPE_Middle;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecyclerHolder) holder).bindData(mDatas.get(position));
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public ConstraintLayout parent;

        public RecyclerHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderButtonState state = currentIndex == getLayoutPosition() ? Doing : currentIndex < getLayoutPosition() ? Ready : Did;
                    if (callback != null) callback.onMenu(getLayoutPosition(), mDatas.get(getLayoutPosition()), state);
                }
            });
        }

        public void bindData(String data) {
            int index = getLayoutPosition();
            int maxIndex = getItemCount() - 1;

            int imageRes = 0;
            if (index == currentIndex) { // 进行中
                if (index == 0) imageRes = startRes[1];
                else if (index == maxIndex) imageRes = endRes[1];
                else imageRes = middleRes[1];
            } else if (index < currentIndex) { // 全部是已完成
                if (index == 0) imageRes = startRes[2];
                else if (index == maxIndex) imageRes = endRes[2];
                else imageRes = middleRes[2];
            } else if (index > currentIndex) { // 全部是未完成
                if (index == 0) imageRes = startRes[0];
                else if (index == maxIndex) imageRes = endRes[0];
                else imageRes = middleRes[0];
            }

            imageView.setImageResource(imageRes);
            textView.setText(data);
        }
    }

    public void setOnMenuClick(OnMenuClick callback) {
        this.callback = callback;
    }
}
