package com.example.emma_baumstarck.todoapp;

import android.content.Context;

/**
 * Created by emma_baumstarck on 7/16/16.
 */
public class ApplicationContext {
    private Context appContext;

    private ApplicationContext() {
    }

    public void init(Context context) {
        if (appContext == null) {
            appContext = context;
        }
    }

    private Context getContext() {
        return appContext;
    }

    public static Context get() {
        return getInstance().getContext();
    }

    private static ApplicationContext instance;

    public static ApplicationContext getInstance() {
        return instance == null ?
                (instance = new ApplicationContext()) :
                instance;
    }
}
