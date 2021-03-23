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
package me.luzhuo.lib_common_ui.emoji.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;

import java.util.HashMap;

import me.luzhuo.lib_common_ui.emoji.span.EmojiSpan;

/**
 * HashMap形式的表情过滤器
 * new HashMapEmoticonFilter(ChongjiaEmoticons.ChongjiaHashMap)
 */
public class HashMapAutoEmoticonFilter extends HashMapEmoticonFilter {

    public HashMapAutoEmoticonFilter(HashMap<String, Integer> emoticons) {
        super(emoticons);
    }

    protected void emoticonDisplay(Context context, Spannable spannable, int emoticonName, int fontSize, int start, int end) {
        Drawable drawable = getDrawable(context, emoticonName);
        if (drawable != null) {
            int itemHeight = fontSize;
            int itemWidth = (int)(drawable.getIntrinsicWidth() * ((float)fontSize / drawable.getIntrinsicHeight()));

            drawable.setBounds(0, 0, itemWidth, itemHeight);
            EmojiSpan imageSpan = new EmojiSpan(drawable);
            spannable.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }
}
