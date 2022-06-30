package me.luzhuo.lib_common_ui.index_bar.style;

import android.graphics.Canvas;
import android.graphics.Paint;

import me.luzhuo.lib_common_ui.index_bar.IndexBarView;

public class DefaultStyle implements IndexBarView.IndexBarStyle {
    private final Paint paint = new Paint();

    public DefaultStyle(float textSp, int textColor) {
        paint.setAntiAlias(true);
        paint.setTextSize(textSp);
        paint.setColor(textColor);
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    @Override
    public void onPressed(IndexBarView view, Canvas canvas, int index, String indexStr, int rectStartX, int rectStartY, int rectEndX, int rectEndY) {
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();

        int with = view.getMeasureWidth() - view.getPaddingLeft() - view.getPaddingRight();
        final float textX = with / 2f + view.getPaddingLeft() - getPaint().measureText(indexStr) / 2;
        final int baseLine = (int) ((view.getMeasureTextSize() - fontMetrics.bottom - fontMetrics.top) / 2);
        final float textY = view.getPaddingTop() + view.getMeasureTextSize() * index + view.getTextSpace() * index + baseLine;

        canvas.drawText(indexStr, textX, textY, getPaint());
    }
}
