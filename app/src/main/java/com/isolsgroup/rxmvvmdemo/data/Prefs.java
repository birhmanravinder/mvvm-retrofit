package com.isolsgroup.rxmvvmdemo.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.isolsgroup.rxmvvmdemo.BuildConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Prefs {

    private Context context;
    private final SharedPreferences sharedPreferences;

    @Inject
    public Prefs(Context context) {
        this.context=context;
        String KEY_PREFERENCE_NAME = BuildConfig.APPLICATION_ID + ".PREFERENCE_FILE_KEY";
        sharedPreferences=context.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void clearPreferences(){
        sharedPreferences.edit().clear().apply();
    }

    public void setUserID(int userid) {
        sharedPreferences.edit().putInt("userid", userid).apply();
    }

    public int getUserID() {
        return sharedPreferences.getInt("userid", 0);
    }





}

