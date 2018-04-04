package com.cc.android.zrouter;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.cc.android.zrouter.multiprocess.BaseApplicationLogic;
import com.cc.android.zrouter.multiprocess.PriorityLogicWrapper;
import com.cc.android.zrouter.router.LocalRouter;
import com.cc.android.zrouter.router.WideRouter;
import com.cc.android.zrouter.router.WideRouterApplicationLogic;
import com.cc.android.zrouter.router.WideRouterConnectService;
import com.cc.android.zrouter.tools.LogUtil;
import com.cc.android.zrouter.tools.ProcessUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * 继承自系统的Application，在创建Application的时候做一些事情
 */
public abstract class ZRouterApplication extends Application {
    private static final String TAG = "ZRouterApplication";
    /**当前类的实例*/
    private static ZRouterApplication sInstance;
    /**保存逻辑Application的ArrayList*/
    private ArrayList<PriorityLogicWrapper> mLogicList;

    private HashMap<String, ArrayList<PriorityLogicWrapper>> mLogicClassMap;

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        LogUtil.d(TAG,"Application onCreate start: "+System.currentTimeMillis());
        init();
        startWideRouter();
        initializeLogic();
        dispatchLogic();
        instantiateLogic();

        // Traverse the application logic.
        if (null != mLogicList && mLogicList.size() > 0) {
            for (PriorityLogicWrapper priorityLogicWrapper : mLogicList) {
                if (null != priorityLogicWrapper && null != priorityLogicWrapper.instance) {
                    priorityLogicWrapper.instance.onCreate();
                }
            }
        }
        LogUtil.d(TAG,"Application onCreate end: "+System.currentTimeMillis());
    }

    private void init() {
        LocalRouter.getInstance(this);
        mLogicClassMap = new HashMap<>();
    }

    protected void startWideRouter() {
        if (needMultipleProcess()) {//如果需要多进程通信
            registerApplicationLogic(WideRouter.PROCESS_NAME, 1000, WideRouterApplicationLogic.class);
            Intent intent = new Intent(this, WideRouterConnectService.class);
            startService(intent);
        }
    }

    public abstract void initializeAllProcessRouter();

    protected abstract void initializeLogic();

    public abstract boolean needMultipleProcess();

    protected boolean registerApplicationLogic(
            String processName,
            int priority,
            @NonNull Class<? extends BaseApplicationLogic> logicClass) {
        boolean result = false;
        if (null != mLogicClassMap) {
            ArrayList<PriorityLogicWrapper> tempList = mLogicClassMap.get(processName);
            if (null == tempList) {
                tempList = new ArrayList<>();
                mLogicClassMap.put(processName, tempList);
            }
            if (tempList.size() > 0) {
                for (PriorityLogicWrapper priorityLogicWrapper : tempList) {
                    if (logicClass.getName().equals(priorityLogicWrapper.logicClass.getName())) {
                        throw new RuntimeException(logicClass.getName() + " has registered.");
                    }
                }
            }
            PriorityLogicWrapper priorityLogicWrapper = new PriorityLogicWrapper(priority, logicClass);
            tempList.add(priorityLogicWrapper);
        }
        return result;
    }

    private void dispatchLogic() {
        if (null != mLogicClassMap) {
            mLogicList = mLogicClassMap.get(ProcessUtil.getProcessName(this, ProcessUtil.getMyProcessId()));
        }
    }

    private void instantiateLogic() {
        if (null != mLogicList && mLogicList.size() > 0) {
            if (null != mLogicList && mLogicList.size() > 0) {
                Collections.sort(mLogicList);
                for (PriorityLogicWrapper priorityLogicWrapper : mLogicList) {
                    if (null != priorityLogicWrapper) {
                        try {
                            priorityLogicWrapper.instance = priorityLogicWrapper.logicClass.newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        if (null != priorityLogicWrapper.instance) {
                            priorityLogicWrapper.instance.setApplication(this);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (null != mLogicList && mLogicList.size() > 0) {
            for (PriorityLogicWrapper priorityLogicWrapper : mLogicList) {
                if (null != priorityLogicWrapper && null != priorityLogicWrapper.instance) {
                    priorityLogicWrapper.instance.onTerminate();
                }
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (null != mLogicList && mLogicList.size() > 0) {
            for (PriorityLogicWrapper priorityLogicWrapper : mLogicList) {
                if (null != priorityLogicWrapper && null != priorityLogicWrapper.instance) {
                    priorityLogicWrapper.instance.onLowMemory();
                }
            }
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (null != mLogicList && mLogicList.size() > 0) {
            for (PriorityLogicWrapper priorityLogicWrapper : mLogicList) {
                if (null != priorityLogicWrapper && null != priorityLogicWrapper.instance) {
                    priorityLogicWrapper.instance.onTrimMemory(level);
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null != mLogicList && mLogicList.size() > 0) {
            for (PriorityLogicWrapper priorityLogicWrapper : mLogicList) {
                if (null != priorityLogicWrapper && null != priorityLogicWrapper.instance) {
                    priorityLogicWrapper.instance.onConfigurationChanged(newConfig);
                }
            }
        }
    }

    public static ZRouterApplication getCoreApplication(){
        return sInstance;
    }
}
