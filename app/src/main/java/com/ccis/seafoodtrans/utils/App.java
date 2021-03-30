package com.ccis.seafoodtrans.utils;

import android.app.Application;
import android.content.Context;


import static com.ccis.seafoodtrans.utils.Constants.ENGLISH;

public class App extends Application {
    private static App mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
//        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
//        Realm.setDefaultConfiguration(config);

        PreferenceController.getInstance(this).persist(PreferenceController.LANGUAGE, ENGLISH);
//            Lingver.init(mContext, Locale.ENGLISH);

    }

    public static Context getContext() {
        return mContext;
    }
}
