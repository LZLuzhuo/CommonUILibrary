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
package me.luzhuo.lib_common_ui.more_select.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * @Author: Luzhuo
 * @Creation Date: 2021/8/25 23:44
 * @Copyright: Copyright 2021 Luzhuo. All rights reserved.
 **/
public class MoreSelectData implements Serializable {
    /**
     * 提示的标题
     */
    public String hintTitle;
    /**
     * 当前的选择
     */
    public ChildData currentSelect;
    /**
     * 数据集
     */
    public List<ChildData> datas;
    /**
     * 附带数据
     */
    public Object tag;

    /**
     * 外部传进来的标题数据
     * @param hintTitle 提示的标题信息
     * @param tag 附加数据
     */
    public MoreSelectData(String hintTitle, Object tag) {
        this.hintTitle = hintTitle;
        this.tag = tag;
    }

    public static class ChildData implements Serializable {
        /**
         * 数据名
         */
        public String data;
        /**
         * 附带数据
         */
        public Object tag;

        public ChildData(String data, Object tag) {
            this.data = data;
            this.tag = tag;
        }
    }
}
