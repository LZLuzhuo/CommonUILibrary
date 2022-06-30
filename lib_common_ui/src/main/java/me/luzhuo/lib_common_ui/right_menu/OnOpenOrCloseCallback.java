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
package me.luzhuo.lib_common_ui.right_menu;

/**
 * Description: 点击 打开 或者 关闭 时的监听
 *
 * @Author: Luzhuo
 * @Creation Date: 2021/1/24 16:52
 * @Copyright: Copyright 2021 Luzhuo. All rights reserved.
 **/
public interface OnOpenOrCloseCallback {
    /**
     * 打开
     * @return boolean 返回true, 表示打开侧边栏, 返回false, 表示不打开侧边栏
     */
    public boolean onOpen();

    /**
     * 关闭
     */
    public void onClose();
}
