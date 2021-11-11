package me.luzhuo.commonuilibrary.toolbar;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.luzhuo.commonuilibrary.R;
import me.luzhuo.lib_common_ui.toolbar.OnTextWatcher;
import me.luzhuo.lib_common_ui.toolbar.OnToolBarCallback;
import me.luzhuo.lib_common_ui.toolbar.ToolBarView;

public class ToolBarActivity extends AppCompatActivity {
    private static final String TAG = ToolBarActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        final ToolBarView toolbar = findViewById(R.id.toolbar);
        toolbar.setOnToolBarCallback(new OnToolBarCallback(){
            @Override
            public boolean onReturn() {
                Log.e(TAG, "onReturn");
                return true;
            }

            @Override
            public void onRightButton() {
                Log.e(TAG, "onRightButton");
            }

            @Override
            public void onSearch(CharSequence content) {
                Log.e(TAG, "onSearch: " + content);
            }

            @Override
            public void onContentClick() {
                Log.e(TAG, "onContentClick");
            }

            @Override
            public void onVoice() {
                Log.e(TAG, "onVoice");
            }
        });
        toolbar.setOnTextWatcher(new OnTextWatcher() {
            @Override
            public void onTextWatcher(CharSequence content) {
                Log.e(TAG, "onTextWatcher: " + content);
            }
        });
    }
}
