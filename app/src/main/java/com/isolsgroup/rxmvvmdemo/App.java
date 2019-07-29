package com.isolsgroup.rxmvvmdemo;

import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.isolsgroup.rxmvvmdemo.di.AppComponent;
import com.isolsgroup.rxmvvmdemo.util.Foreground;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.OkHttpClient;

public class App extends MultiDexApplication {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this, OkHttpImagePipelineConfigFactory.newBuilder(this, new OkHttpClient()).setDownsampleEnabled(true).build());
        Realm.init(this);

        appComponent=AppComponent.component(this);
        appComponent.inject(this);

        initForeground();
    }


    public static AppComponent component(Context context) {
       return ((App)context.getApplicationContext()).appComponent;
    }


    public void initForeground(){
        Foreground.init(this).addListener(new Foreground.Listener() {
            @Override
            public void onBecameForeground() {
                Log.e("Event", "onBecameForeground");
            }

            @Override
            public void onBecameBackground() throws IOException {
                Log.e("Event", "onBecameBackground");
            }
        });
    }

}
