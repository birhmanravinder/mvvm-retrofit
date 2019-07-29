package com.isolsgroup.rxmvvmdemo.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

/**
 * Created by Ashutosh Verma.
 */
public class RoundedBackgroundSpan extends ReplacementSpan {

    private static int CORNER_RADIUS = 8;
    private int backgroundColor = 0;
    private int textColor = 0;

    public RoundedBackgroundSpan(Context context, int textColor, int backgroundColor, int radius) {
        super();
        this.textColor = context.getResources().getColor(textColor);
        this.backgroundColor = context.getResources().getColor(backgroundColor);
        CORNER_RADIUS= radius;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        RectF rect = new RectF(x, top, x + measureText(paint, text, start, end), bottom);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);
        paint.setColor(textColor);
        canvas.drawText(text, start, end, x, y, paint);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}