package com.cc.android.zrouter;

import java.util.HashMap;

public abstract class ZRouterProvider {
    //TODO this field is used for control the provider on and off
    private boolean mValid = true;
    private HashMap<String,ZRouterAction> mActions;
    public ZRouterProvider(){
        mActions = new HashMap<>();
        registerActions();
    }
    protected void registerAction(String actionName,ZRouterAction action){
        mActions.put(actionName,action);
    }

    public ZRouterAction findAction(String actionName){
        return mActions.get(actionName);
    }

    public boolean isValid(){
        return mValid;
    }

    protected abstract void registerActions();
}
