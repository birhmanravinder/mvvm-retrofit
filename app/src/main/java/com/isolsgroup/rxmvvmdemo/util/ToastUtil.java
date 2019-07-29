package com.isolsgroup.rxmvvmdemo.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Ashutosh Verma.
 */

@Singleton
public class ToastUtil {
    private Context context;

    @Inject
    public ToastUtil(Context context) {
        this.context=context;
    }

    public void showLongMessage(String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showShortMessage(String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
