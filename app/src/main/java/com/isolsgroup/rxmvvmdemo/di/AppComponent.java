package com.isolsgroup.rxmvvmdemo.di;


import android.app.Application;

import com.isolsgroup.rxmvvmdemo.App;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ashutosh Verma.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(App app);

    ActivityComponent plusActivityModule(ActivityModule activityModule);

    public static AppComponent component(Application app){
        return DaggerAppComponent.builder().appModule(new AppModule(app)).networkModule(new NetworkModule()).persistenceModule(new PersistenceModule()).build();
    }

}
