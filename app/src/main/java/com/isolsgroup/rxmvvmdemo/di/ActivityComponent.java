package com.isolsgroup.rxmvvmdemo.di;

import android.app.Activity;

import com.isolsgroup.rxmvvmdemo.App;
import com.isolsgroup.rxmvvmdemo.ui.main.MainActivity;
import com.isolsgroup.rxmvvmdemo.ui.productdetail.ProductDetailActivity;
import com.isolsgroup.rxmvvmdemo.ui.productlist.ProductListActivity;

import dagger.Subcomponent;

/**
 * Created by Ashutosh Verma.
 */

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);
    void inject(ProductListActivity activity);
    void inject(ProductDetailActivity activity);


    public static ActivityComponent component(Activity activity){
        return App.component(activity).plusActivityModule(new ActivityModule(activity));
    }
}
