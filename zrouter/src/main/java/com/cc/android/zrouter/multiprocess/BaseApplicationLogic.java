package com.cc.android.zrouter.multiprocess;

import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.cc.android.zrouter.ZRouterApplication;


public class BaseApplicationLogic {

    protected ZRouterApplication mApplication;

    public BaseApplicationLogic() {}

    public void setApplication(@NonNull ZRouterApplication application) {
        mApplication = application;
    }

    public void onCreate() {
    }

    public void onTerminate() {
    }

    public void onLowMemory() {
    }

    public void onTrimMemory(int level) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }
}
