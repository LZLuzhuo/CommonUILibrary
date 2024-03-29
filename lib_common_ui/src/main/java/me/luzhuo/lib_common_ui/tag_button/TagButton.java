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

/**
 * Description:
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/11/11 10:32
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class TagButton {
    public String title;
    public Object tag;
    public boolean isChecked;
    public boolean isEnable;

    public TagButton(String title, Object tag, boolean isChecked, boolean isEnable) {
        this.title = title;
        this.tag = tag;
        this.isChecked = isChecked;
        this.isEnable = isEnable;
    }

    public TagButton(String title, Object tag) {
        this(title, tag, false, true);
    }
}
