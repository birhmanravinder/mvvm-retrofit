package com.isolsgroup.rxmvvmdemo.util;

import android.app.Activity;
import android.content.Intent;

import com.isolsgroup.rxmvvmdemo.ui.productdetail.ProductDetailActivity;
import com.isolsgroup.rxmvvmdemo.ui.productlist.ProductListActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActivityRouter {
    private Activity activity;

    @Inject
    public ActivityRouter(Activity activity) {
        this.activity=activity;
    }

    public void startProductListActivity(String category){
        Intent intent=new Intent(activity, ProductListActivity.class);
        intent.putExtra("category", category);
        activity.startActivity(intent);
    }

    public void startProductDetailActivity(long productId){
        Intent intent=new Intent(activity, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.KEY_PRODUCT_ID, productId);
        activity.startActivity(intent);
    }

}
