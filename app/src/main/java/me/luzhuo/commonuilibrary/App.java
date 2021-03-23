package me.luzhuo.commonuilibrary;

import android.app.Application;

import me.luzhuo.commonuilibrary.emoji.MyEmoticons;
import me.luzhuo.emoji_chongjia.ChongjiaEmoticons;
import me.luzhuo.lib_common_ui.emoji.EmojiManager;
import me.luzhuo.lib_common_ui.emoji.filter.EmojiFilter;
import me.luzhuo.lib_common_ui.emoji.filter.HashMapEmoticonFilter;
import me.luzhuo.lib_common_ui.emoji.filter.HashMapAutoEmoticonFilter;

/**
 * Description:
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/7/7 0:10
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.getInstance()
                .addFilter(new EmojiFilter())
                .addFilter(new HashMapEmoticonFilter(ChongjiaEmoticons.ChongjiaHashMap))
                .addFilter(new HashMapAutoEmoticonFilter(MyEmoticons.MyEmojiHashMap));
    }
}
