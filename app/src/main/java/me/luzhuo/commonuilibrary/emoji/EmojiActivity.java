package me.luzhuo.commonuilibrary.emoji;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.luzhuo.commonuilibrary.R;
import me.luzhuo.lib_common_ui.emoji.EmojiManager;

/**
 * Description:
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/7/6 23:48
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class EmojiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji);

        EmojiManager manager = EmojiManager.getInstance();

        final EditText tv1 = findViewById(R.id.tv1);
        TextView tv2 = findViewById(R.id.tv2);
        TextView tv3 = findViewById(R.id.tv3);

        tv1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                EmojiManager.getInstance().EditTextFilter(tv1, charSequence, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        manager.TextViewFilter(tv2, "牛逼哟[偷笑]牛逼哟");
        manager.TextViewFilter(tv3, "牛逼哟[广告]牛逼哟[默认地址]牛逼哟");
    }
}
