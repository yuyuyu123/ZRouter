package com.cc.android.zrouter;

import android.content.Context;

import java.util.HashMap;


public abstract class ZRouterAction {
    public abstract boolean isAsync(Context context, HashMap<String,String> requestData);
    public abstract ZRouterActionResult invoke(Context context, HashMap<String,String> requestData);
    public boolean isAsync(Context context, HashMap<String,String> requestData,Object object){
        return false;
    }
    public ZRouterActionResult invoke(Context context, HashMap<String,String> requestData, Object object){
        return new ZRouterActionResult.Builder().code(ZRouterActionResult.CODE_NOT_IMPLEMENT).msg("This method has not yet been implemented.").build();
    }
}
