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

/**
 * Description:
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/11/25 22:14
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public interface OnMenuClick {
    /**
     * 菜单按钮的点击
     * @param index 索引
     * @param content 菜单文本
     * @param state 状态, 0:未开始, 1:进行中, 2:已完成
     */
    public void onMenu(int index, String content, OrderButtonState state);
}