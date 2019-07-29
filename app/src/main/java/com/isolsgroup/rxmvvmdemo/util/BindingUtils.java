package com.isolsgroup.rxmvvmdemo.util;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isolsgroup.rxmvvmdemo.R;


public class BindingUtils {

    @BindingAdapter("bind:src")
    public static void bindImageSrc(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
    @BindingAdapter("bind:drawableTop")
    public static void bindDrawableTop(TextView textView, int resource) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, resource, 0, 0);
    }

    @BindingAdapter("bind:visible")
    public static void bindVisible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("bind:imgUrl")
    public static void bindImageUrl(SimpleDraweeView draweeView, String imgUrl) {
        Uri imgUri = Uri.parse(imgUrl);
        draweeView.setImageURI(imgUri);
    }

    @BindingAdapter("bind:cartFab")
    public static void bindCartFab(FloatingActionButton fab, boolean inCart) {
        if (!inCart)fab.setImageResource(R.drawable.ic_add_to_cart);
        else fab.setImageResource(R.drawable.ic_remove_cart);
    }

}
