package com.cc.android.zrouter.router;


import com.cc.android.zrouter.multiprocess.BaseApplicationLogic;

public final class WideRouterApplicationLogic extends BaseApplicationLogic {
    @Override
    public void onCreate() {
        super.onCreate();
        initRouter();
    }

    protected void initRouter() {
        WideRouter.getInstance(mApplication);
        mApplication.initializeAllProcessRouter();
    }
}
