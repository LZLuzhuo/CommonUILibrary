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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public abstract class EmoticonFilter {

    public abstract void EditTextfilter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter);
    public abstract void TextViewFilter(TextView textView, SpannableStringBuilder spannable, CharSequence text);

    protected final int WRAP_DRAWABLE = -1;
    protected int emoticonSize = -1;

    /**
     * get the drawable form asset
     * @param context
     * @param assetName
     * @return
     */
    protected Drawable getDrawableFromAssets(Context context, String assetName) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(assetName));
            return new BitmapDrawable(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * get the drawable for drawable name from mipmap or drawable
     * @param context
     * @param emojiName
     * @return
     */
    protected Drawable getDrawable(Context context, String emojiName) {
        if(TextUtils.isEmpty(emojiName)){
            return null;
        }

        if(emojiName.indexOf(".") >= 0){
            emojiName = emojiName.substring(0,emojiName.indexOf("."));
        }
        int resID = context.getResources().getIdentifier(emojiName, "mipmap", context.getPackageName());
        if (resID <= 0) {
            resID = context.getResources().getIdentifier(emojiName, "drawable", context.getPackageName());
        }

        try {
            return Build.VERSION.SDK_INT >= 21 ? context.getResources().getDrawable(resID, null) : context.getResources().getDrawable(resID);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    /**
     * get the drawable from drawable id
     * @param context
     * @param resourceId
     * @return
     */
    protected Drawable getDrawable(Context context, int resourceId) {
        if(resourceId <= 0) return null;

        try {
            return Build.VERSION.SDK_INT >= 21 ? context.getResources().getDrawable(resourceId, null) : context.getResources().getDrawable(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get font height by text size from textview
     * @param textView Textview
     * @return font height
     */
    protected int getFontHeight(TextView textView) {
        Paint paint = new Paint();
        paint.setTextSize(textView.getTextSize());
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top);
    }
}
