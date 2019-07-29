package com.isolsgroup.rxmvvmdemo.ui.custom;

import android.graphics.Color;
import android.os.Handler;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.isolsgroup.rxmvvmdemo.util.ClickListener;

/**
 * Created by Ashutosh Verma on 17-08-2016.
 */
public class ClickSpan extends ClickableSpan {
    private boolean clicked= false;
    private ClickListener listener;
    private int color;

    public ClickSpan(int color, ClickListener listener) {
        this.color=color;
        this.listener=listener;
    }

    @Override
    public void onClick(final View view) {
        clicked=true;
        view.invalidate();
        if(listener!=null){listener.onClick();}
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clicked= false; view.invalidate();
            }
        }, 100);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if(clicked){ ds.bgColor= Color.parseColor("#c9cbcc"); }
        else { ds.bgColor= Color.TRANSPARENT; }
        ds.setColor(color);
        ds.setUnderlineText(false);
    }
}
