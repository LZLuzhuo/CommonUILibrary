package me.luzhuo.commonuilibrary.emoji;

import java.util.HashMap;

/**
 * Description:
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/7/7 0:12
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class MyEmoticons {
    public static final String coverName = "wansuo_video_icon_advertising";
    public static final HashMap<String, Integer> MyEmojiHashMap = new HashMap<>();

    static {
        MyEmojiHashMap.put("[默认地址]", me.luzhuo.commonuilibrary.R.mipmap.wansuo_defulat_address);
        MyEmojiHashMap.put("[广告]", me.luzhuo.commonuilibrary.R.mipmap.wansuo_video_icon_advertising);
    }
}
