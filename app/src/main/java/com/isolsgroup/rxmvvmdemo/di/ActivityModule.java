package com.isolsgroup.rxmvvmdemo.di;

import android.app.Activity;
import android.content.Context;

import com.isolsgroup.rxmvvmdemo.util.ActivityRouter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ashutosh Verma.
 */
@PerActivity
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    ActivityRouter providesActivityRouter(){
        return new ActivityRouter(activity);
    }

}