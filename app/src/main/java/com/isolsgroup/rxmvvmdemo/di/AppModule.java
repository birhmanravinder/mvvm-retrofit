package com.isolsgroup.rxmvvmdemo.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ashutosh Verma.
 */

@Singleton
@Module(includes ={NetworkModule.class, PersistenceModule.class, UtilModule.class})
public class AppModule {
    private Application app;

    public AppModule(Application app) {
        this.app=app;
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return app;
    }

    @Provides
    @Singleton
    @AppContext
    Context providesContext(){
        return app;
    }



}
