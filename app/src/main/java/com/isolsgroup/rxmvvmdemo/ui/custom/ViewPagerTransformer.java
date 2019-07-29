package com.isolsgroup.rxmvvmdemo.ui.custom;

/**
 * Created by Ashutosh Verma on 15-09-2016.
 */
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public abstract class ViewPagerTransformer implements ViewPager.PageTransformer {


    protected abstract void onTransform(View view, float position);

    @Override
    public void transformPage(View view, float position) {
        onPreTransform(view, position);
        onTransform(view, position);
        onPostTransform(view, position);
    }

    protected boolean hideOffscreenPages() {
        return true;
    }


    protected boolean isPagingEnabled() {
        return false;
    }


    protected void onPreTransform(View view, float position) {
        final float width = view.getWidth();

        view.setRotationX(0);
        view.setRotationY(0);
        view.setRotation(0);
        view.setScaleX(1);
        view.setScaleY(1);
        view.setPivotX(0);
        view.setPivotY(0);
        view.setTranslationY(0);
        view.setTranslationX(isPagingEnabled() ? 0f : -width * position);

        if (hideOffscreenPages()) {
            view.setAlpha(position <= -1f || position >= 1f ? 0f : 1f);
        } else {
            view.setAlpha(1f);
        }
    }


    protected void onPostTransform(View view, float position) {
    }




    public static class AccordionTransformer extends ViewPagerTransformer {

        @Override
        protected void onTransform(View view, float position) {
            view.setPivotX(position < 0 ? 0 : view.getWidth());
            view.setScaleX(position < 0 ? 1f + position : 1f - position);
        }

    }

//    public static class StackTransformer extends ViewPagerTransformer {
//
//        @Override
//        protected void onTransform(View view, float position) {
//            view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
//        }
//    }

}