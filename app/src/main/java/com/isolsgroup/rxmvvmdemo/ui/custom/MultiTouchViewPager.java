package com.isolsgroup.rxmvvmdemo.ui.custom;

/**
 * Created by Ashutosh Verma on 08-Jul-17.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import io.reactivex.annotations.NonNull;

public class MultiTouchViewPager extends ViewPager {

    public MultiTouchViewPager(Context context) {
        super(context);
    }

    public MultiTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean mIsDisallowIntercept = false;

    @Override public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false);
            boolean handled = super.dispatchTouchEvent(ev);
            requestDisallowInterceptTouchEvent(true);
            return handled;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }
}