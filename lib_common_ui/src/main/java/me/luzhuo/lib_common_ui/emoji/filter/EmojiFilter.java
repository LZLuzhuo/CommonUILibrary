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
import android.text.SpannableStringBuilder;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.luzhuo.lib_common_ui.emoji.span.EmojiSpan;

/**
 * emoji表情过滤器
 * new EmojiFilter()
 */
public class EmojiFilter extends EmoticonFilter {

    private final Pattern EMOJI_RANGE = Pattern.compile("[\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee]");

    public Matcher getMatcher(CharSequence matchStr) {
        return EMOJI_RANGE.matcher(matchStr);
    }

    @Override
    public void EditTextfilter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter) {
        emoticonSize = emoticonSize == -1 ? getFontHeight(editText) : emoticonSize;
        clearSpan(editText.getText(), start, text.toString().length());
        Matcher m = getMatcher(text.toString().substring(start));
        if (m != null) {
            while (m.find()) {
                try {
                    String emojiHex = Integer.toHexString(Character.codePointAt(m.group(), 0));
                    emojiDisplay(editText.getContext(), editText.getText(), emojiHex, emoticonSize, start + m.start(), start + m.end());
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void TextViewFilter(TextView textView, SpannableStringBuilder spannable, CharSequence text) {
        emoticonSize = emoticonSize == -1 ? getFontHeight(textView) : emoticonSize;
        Matcher m = getMatcher(text);
        if (m != null) {
            while (m.find()) {
                try {
                    String emojiHex = Integer.toHexString(Character.codePointAt(m.group(), 0));
                    if(emojiHex == null || emojiHex.isEmpty()) continue;

                    emojiDisplay(textView.getContext(), spannable, emojiHex, emoticonSize, m.start(), m.end());
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void emojiDisplay(Context context, Spannable spannable, String emojiHex, int fontSize, int start, int end) {
        Drawable drawable = getDrawable(context, "emoji_0x" + emojiHex);
        if (drawable != null) {
            int itemHeight;
            int itemWidth;
            if (fontSize == WRAP_DRAWABLE) {
                itemHeight = drawable.getIntrinsicHeight();
                itemWidth = drawable.getIntrinsicWidth();
            } else {
                itemHeight = fontSize;
                itemWidth = fontSize;
            }

            drawable.setBounds(0, 0, itemWidth, itemHeight);
            EmojiSpan imageSpan = new EmojiSpan(drawable);
            spannable.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    private void clearSpan(Spannable spannable, int start, int end) {
        if (start == end) {
            return;
        }
        EmojiSpan[] oldSpans = spannable.getSpans(start, end, EmojiSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            spannable.removeSpan(oldSpans[i]);
        }
    }
}
