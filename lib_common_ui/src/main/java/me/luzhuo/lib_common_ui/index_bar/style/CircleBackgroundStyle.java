package me.luzhuo.lib_common_ui.index_bar.style;

import android.graphics.Canvas;
import android.graphics.Paint;

import me.luzhuo.lib_common_ui.index_bar.IndexBarView;

/**
 * 有圆形背景的样式
 */
public class CircleBackgroundStyle extends DefaultStyle {
    private final Paint circleBackgroundPaint = new Paint();
    public CircleBackgroundStyle(float textSp, int textColor, int circleBackgroundColor) {
        super(textSp, textColor);

        circleBackgroundPaint.setColor(circleBackgroundColor);
        circleBackgroundPaint.setAntiAlias(true);
    }

    @Override
    public void onPressed(IndexBarView view, Canvas canvas, int index, String indexStr, int rectStartX, int rectStartY, int rectEndX, int rectEndY) {

        int with = view.getMeasureWidth() - view.getPaddingLeft() - view.getPaddingRight();
        final float centerX = with / 2f + view.getPaddingLeft();
        final float centerY = rectStartY + (rectEndY - rectStartY) / 2f;
        canvas.drawCircle(centerX, centerY, view.getMeasureTextSize() / 1.3f /*30f*/, circleBackgroundPaint);

        super.onPressed(view, canvas, index, indexStr, rectStartX, rectStartY, rectEndX, rectEndY);
    }
}
