package com.isolsgroup.rxmvvmdemo.di;
import android.content.Context;

import com.isolsgroup.rxmvvmdemo.data.Prefs;
import com.isolsgroup.rxmvvmdemo.data.local.LocalRealmDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;


@Module
public class PersistenceModule {

    @Provides
    @Singleton
    Prefs providesPrefs(@AppContext Context context){
        return new Prefs(context);
    }

    @Provides
    public LocalRealmDB providesLocalRealmDB(Realm realm) {
        return new LocalRealmDB(realm);
    }

    @Provides
    public Realm providesRealm(RealmConfiguration realmConfiguration) {
        return Realm.getInstance(realmConfiguration);
    }

    @Provides
    @Singleton
    public RealmConfiguration providesRealmConfiguration() {
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder();
        builder.deleteRealmIfMigrationNeeded();
        // builder.inMemory();
        return builder.build();
    }

}
