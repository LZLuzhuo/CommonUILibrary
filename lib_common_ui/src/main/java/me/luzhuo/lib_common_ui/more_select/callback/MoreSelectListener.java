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
package me.luzhuo.lib_common_ui.more_select.callback;

import me.luzhuo.lib_common_ui.more_select.bean.MoreSelectData;

/**
 * Description: 供外部使用的 Listener
 * @Author: Luzhuo
 * @Creation Date: 2021/8/25 23:44
 * @Copyright: Copyright 2021 Luzhuo. All rights reserved.
 **/
public interface MoreSelectListener {
    public void getChildList(int position, MoreSelectData title, MoreSelectData preTitle);

    public void onChildClick(int position, MoreSelectData.ChildData data, int titlePosition, MoreSelectData title, boolean isCompleted);
}
