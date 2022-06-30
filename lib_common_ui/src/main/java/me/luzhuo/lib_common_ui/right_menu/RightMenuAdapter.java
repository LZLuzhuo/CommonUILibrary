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
package me.luzhuo.lib_common_ui.right_menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_core.ui.dialog.callback.OnMenuCallback;

/**
 * Description:
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/11/11 10:32
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
class RightMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<String> mDatas;
    private OnMenuCallback callback;

    public RightMenuAdapter(List<String> menus, OnMenuCallback callback) {
        this.mDatas = menus;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_right_menu_item_right_menu, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder) holder).bindData(mDatas.get(position));
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView base_right_menu;

        public ItemHolder(View itemView) {
            super(itemView);
            base_right_menu = itemView.findViewById(R.id.base_right_menu);
            base_right_menu.setOnClickListener(this);
        }

        public void bindData(String data) {
            base_right_menu.setText(data);
        }

        @Override
        public void onClick(View v) {
            if (callback != null) callback.onMenu(getLayoutPosition(), mDatas.get(getLayoutPosition()));
        }
    }
}

