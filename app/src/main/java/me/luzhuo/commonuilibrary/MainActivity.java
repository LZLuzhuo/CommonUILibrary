package me.luzhuo.commonuilibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import me.luzhuo.commonuilibrary.emoji.EmojiActivity;
import me.luzhuo.commonuilibrary.toolbar.ToolBarActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//         startActivity(new Intent(this, EmojiActivity.class));
        startActivity(new Intent(this, ToolBarActivity.class));
    }
}
