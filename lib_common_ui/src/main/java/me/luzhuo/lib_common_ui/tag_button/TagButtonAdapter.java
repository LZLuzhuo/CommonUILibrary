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
package me.luzhuo.lib_common_ui.tag_button;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_core.app.color.ColorManager;
import me.luzhuo.lib_core.ui.calculation.UICalculation;

/**
 * Description:
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/11/11 10:32
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
class TagButtonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TagButton> mDatas = new ArrayList<>();
    private Context context;
    private static final int TYPE_Normal = 1, TYPE_Checked = 2, TYPE_Invalid = 3;
    private int NormalBackgroundColor;
    private int NormalTextColor;
    private int CheckedBackgroundColor;
    private int CheckedTextColor;
    private int InvalidBackgroundColor;
    private int InvalidTextColor;
    private ColorManager color;
    private UICalculation ui;
    // 默认为单选
    private boolean singleSelect = true;
    private boolean cancelable = false;
    private TagButtonListener listener = null;

    public TagButtonAdapter(Context context) {
        this.context = context;
        this.color = new ColorManager(context);
        this.ui = new UICalculation(context);
        this.NormalBackgroundColor = 0xFFF5F5F5;
        this.NormalTextColor = 0xFF333333;
        this.CheckedBackgroundColor = (0x4D << 24) | (color.getColorAccent() & 0x00FFFFFF);
        this.CheckedTextColor = color.getColorAccent();
        this.InvalidBackgroundColor = 0xFFF6F6F6;
        this.InvalidTextColor = 0xFFA8A8A8;
    }

    public TagButtonAdapter(Context context, boolean singleSelect, boolean cancelable) {
        this(context);
        this.singleSelect = singleSelect;
        this.cancelable = cancelable;
    }

    public void setData(List<TagButton> data) {
        this.mDatas.clear();
        this.mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public List<TagButton> getData() {
        return mDatas;
    }

    public void setCurrent(int current) {
        if (current > mDatas.size() - 1) return;
        setCurrent(mDatas.get(current), current, false);
    }

    private void setCurrent(TagButton data, int position, boolean isClick) {
        if (!singleSelect) data.isChecked = !isClick || !data.isChecked; // isClick ? !data.isChecked : true
        else {
            if (cancelable && data.isChecked) data.isChecked = false;
            else {
                for (TagButton mData : mDatas) { mData.isChecked = false; }
                data.isChecked = true;
            }
        }

        if (!singleSelect) notifyItemChanged(position);
        else notifyDataSetChanged();
    }

    public void setTagButtonListener(TagButtonListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_Normal: return new NormalHolder(LayoutInflater.from(context).inflate(R.layout.ui_tag_button_item_normal, parent, false));
            case TYPE_Checked: return new CheckedHolder(LayoutInflater.from(context).inflate(R.layout.ui_tag_button_item_normal/*core_item_checked*/, parent, false));
            default: return new InvalidHolder(LayoutInflater.from(context).inflate(R.layout.ui_tag_button_item_normal/*core_item_invalid*/, parent, false));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        TagButton tagButton = mDatas.get(position);
        boolean isChecked = tagButton.isChecked;
        boolean isEnable = tagButton.isEnable;

        if (isEnable) {
            if (isChecked) return TYPE_Checked;
            else return TYPE_Normal;
        } else {
            return TYPE_Invalid;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_Normal:
                ((NormalHolder) holder).bindData(mDatas.get(position));
                break;
            case TYPE_Checked:
                ((CheckedHolder) holder).bindData(mDatas.get(position));
                break;
            default:
                ((InvalidHolder) holder).bindData(mDatas.get(position));
                break;
        }
    }

    public class NormalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ConstraintLayout core_tag_button_parent;
        public TextView core_tag_button;

        public NormalHolder(View itemView) {
            super(itemView);
            core_tag_button_parent = itemView.findViewById(R.id.ui_tag_button_parent);
            core_tag_button = itemView.findViewById(R.id.ui_tag_button);
            core_tag_button.setTextColor(NormalTextColor);
            setBackgroundShape(core_tag_button, NormalBackgroundColor);

            core_tag_button_parent.setOnClickListener(this);
        }

        public void bindData(TagButton data) {
            core_tag_button.setText(data.title);
        }

        @Override
        public void onClick(View v) {
            onClickListener(getLayoutPosition());
        }
    }

    private void onClickListener(int position) {
        final TagButton data = mDatas.get(position);
        setCurrent(data, position, true);

        if (listener != null) listener.onSelect(position, data);
    }

    public class CheckedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ConstraintLayout core_tag_button_parent;
        public TextView core_tag_button;

        public CheckedHolder(View itemView) {
            super(itemView);
            core_tag_button_parent = itemView.findViewById(R.id.ui_tag_button_parent);
            core_tag_button = itemView.findViewById(R.id.ui_tag_button);
            core_tag_button.setTextColor(CheckedTextColor);
            setBackgroundShape(core_tag_button, CheckedBackgroundColor);

            core_tag_button_parent.setOnClickListener(this);
        }

        public void bindData(TagButton data) {
            core_tag_button.setText(data.title);
        }

        @Override
        public void onClick(View v) {
            onClickListener(getLayoutPosition());
        }
    }

    public class InvalidHolder extends RecyclerView.ViewHolder {
        public TextView core_tag_button;

        public InvalidHolder(View itemView) {
            super(itemView);
            core_tag_button = itemView.findViewById(R.id.ui_tag_button);
            core_tag_button.setTextColor(InvalidTextColor);
            setBackgroundShape(core_tag_button, InvalidBackgroundColor);
        }

        public void bindData(TagButton data) {
            core_tag_button.setText(data.title);
        }
    }

    private void setBackgroundShape(View view, int color) {
        ShapeAppearanceModel roundShape = ShapeAppearanceModel.builder()
                .setAllCorners(CornerFamily.ROUNDED, ui.dp2px(10))
                .build();
        MaterialShapeDrawable backgroundDrawable = new MaterialShapeDrawable(roundShape);
        backgroundDrawable.setTint(color);
        backgroundDrawable.setPaintStyle(Paint.Style.FILL_AND_STROKE);

        view.setBackground(backgroundDrawable);
    }
}

