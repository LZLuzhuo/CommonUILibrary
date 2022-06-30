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

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_core.ui.dialog.callback.OnMenuCallback;

/**
 * Description: 右侧弹出的菜单列表
 * <pre>
 * protected void onCreate(Bundle savedInstanceState) {
 *     super.onCreate(savedInstanceState);
 *     setContentView(R.layout.ui_right_menu);
 *
 *     FrameLayout base_content = findViewById(R.id.ui_content);
 *     LayoutInflater.from(this).inflate(R.layout.activity_test_content, base_content, true);
 *
 *     List<String> menus = new ArrayList<>();
 *     menus.add("激活");
 *     menus.add("删除");
 *     final RightMenuUtils menuUtils = new RightMenuUtils(this);
 *     menuUtils.setData(menus, new RightMenuAdapter.OnMenuCallback() {
 *         @Override
 *         public void onMenu(int position, String menu) {
 *             menuUtils.close();
 *             Toast.makeText(MainActivity.this, "" + menu, Toast.LENGTH_SHORT).show();
 *         }
 *     });
 * }
 * </pre>
 * @Author: Luzhuo
 * @Creation Date: 2020/11/11 10:32
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class RightMenuUtils {
    private Context context;
    private View base_right_menu_open;
    private View base_right_menu_close;
    private DrawerLayout base_right_menu_drawer;
    private RecyclerView base_right_menu_rec;
    private RightMenuAdapter adapter;
    private OnOpenOrCloseCallback openOrCloseCallback;

    public RightMenuUtils(@NonNull Activity activity) {
        this.context = activity;
        findView(activity);
        initView();
    }

    public RightMenuUtils(@NonNull Fragment fragment) {
        this.context = fragment.getContext();
        findView(fragment.getView());
        initView();
    }

    private void findView(@NonNull Activity activity) {
        base_right_menu_open = activity.findViewById(R.id.ui_right_menu_open);
        base_right_menu_drawer = activity.findViewById(R.id.ui_right_menu_drawer);
        base_right_menu_close = activity.findViewById(R.id.ui_right_menu_close);
        base_right_menu_rec = activity.findViewById(R.id.ui_right_menu_rec);
    }

    private void findView(@NonNull View activity) {
        base_right_menu_open = activity.findViewById(R.id.ui_right_menu_open);
        base_right_menu_drawer = activity.findViewById(R.id.ui_right_menu_drawer);
        base_right_menu_close = activity.findViewById(R.id.ui_right_menu_close);
        base_right_menu_rec = activity.findViewById(R.id.ui_right_menu_rec);
    }

    private void initView() {
        if (base_right_menu_open != null) {
            base_right_menu_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isOpen = true;
                    if (openOrCloseCallback != null) isOpen = openOrCloseCallback.onOpen();
                    if (base_right_menu_drawer != null && isOpen) base_right_menu_drawer.openDrawer(GravityCompat.END);
                }
            });
        }

        if (base_right_menu_close != null) {
            base_right_menu_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (openOrCloseCallback != null) openOrCloseCallback.onClose();
                    if (base_right_menu_drawer != null) base_right_menu_drawer.closeDrawer(GravityCompat.END);
                }
            });
        }

        if (base_right_menu_rec != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            base_right_menu_rec.setLayoutManager(layoutManager);
        }
    }

    /**
     * 设置数据
     */
    public void setData(@NonNull List<String> menus, @Nullable OnMenuCallback callback) {
        adapter = new RightMenuAdapter(menus, callback);
        base_right_menu_rec.setAdapter(adapter);
    }

    /**
     * 设置新的菜案数据
     */
    public void setNewData(@NonNull List<String> menus) {
        if (adapter == null) return;
        adapter.mDatas = menus;
        adapter.notifyDataSetChanged();
    }

    /**
     * 关闭
     */
    public void close() {
        if (base_right_menu_drawer != null) base_right_menu_drawer.closeDrawer(GravityCompat.END);
    }

    /**
     * 设置点击了 打开 或 关闭 时的监听
     */
    public void setOpenOrCloseCallback(@Nullable OnOpenOrCloseCallback openOrCloseCallback) {
        this.openOrCloseCallback = openOrCloseCallback;
    }
}
