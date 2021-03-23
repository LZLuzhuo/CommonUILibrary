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
package me.luzhuo.lib_common_ui.emoji;

import android.text.SpannableStringBuilder;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.luzhuo.lib_common_ui.emoji.filter.EmoticonFilter;

/**
 * Description:
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/6/4 23:23
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class EmojiManager {
    private List<EmoticonFilter> filters = new ArrayList<>();

    private EmojiManager(){}

    public static EmojiManager getInstance(){
        return Instance.instance;
    }

    private static class Instance{
        private static final EmojiManager instance = new EmojiManager();
    }

    /**
     * Please add filter in Application
     *
     * Example:
     * <pre>
     * EmojiManager.getInstance()
     *         .addFilter(new HashMapEmoticonFilter(ChongjiaEmoticons.ChongjiaHashMap))
     *         .addFilter(new EmojiFilter());
     * </pre>
     * @param filter
     */
    public EmojiManager addFilter(EmoticonFilter filter){
        filters.add(filter);
        return this;
    }

    /**
     * Example:
     * <pre>
     * editText.addTextChangedListener(new TextWatcher() {
     *     @Override
     *     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
     *
     *     @Override
     *     public void onTextChanged(CharSequence charSequence, int start, int lengthBefore, int after) {
     *         EmojiManager.getInstance().EditTextFilter(editText, charSequence, start, lengthBefore, after);
     *     }
     *
     *     @Override
     *     public void afterTextChanged(Editable editable) { }
     * });
     * </pre>
     * @param editText
     * @param text
     * @param start
     * @param lengthBefore
     * @param lengthAfter
     */
    public void EditTextFilter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter){
        for (EmoticonFilter filter : filters) {
            filter.EditTextfilter(editText, text, start, lengthBefore, lengthAfter);
        }
    }

    /**
     * Example:
     * <pre>
     * String text = "[偷笑][偷笑][偷笑][偷笑][偷笑][偷笑][偷笑][偷笑][偷笑][偷笑][偷笑]";
     * EmojiManager.getInstance().TextViewFilter(textView, text);
     * </pre>
     *
     * @param textView
     * @param content
     */
    public void TextViewFilter(TextView textView, String content){
        if(textView == null || content == null) return;

        SpannableStringBuilder spannable = new SpannableStringBuilder(content);
        for (EmoticonFilter filter : filters) {
            filter.TextViewFilter(textView, spannable, content);
        }
        textView.setText(spannable);
    }
}
