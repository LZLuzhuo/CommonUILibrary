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
package me.luzhuo.lib_common_ui.toolbar;

/**
 * ToolBar 的回调
 */
public class OnToolBarCallback {
    /**
     * 返回
     * 不重写, 默认关闭
     */
    public boolean onReturn(){ return true; }

    /**
     * 右侧按钮的点击
     */
    public void onRightButton(){}

    /**
     * 搜索的内容
     */
    public void onSearch(CharSequence content){}

    /**
     * 语音识别
     */
    public void onVoice(){}

    /**
     * 搜索框被点击
     */
    public void onContentClick(){}
}
