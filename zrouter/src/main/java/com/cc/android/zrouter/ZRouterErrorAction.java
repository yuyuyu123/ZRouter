package com.cc.android.zrouter;

import android.content.Context;

import java.util.HashMap;

public class ZRouterErrorAction extends ZRouterAction {

    private static final String DEFAULT_MESSAGE = "Something was really wrong. Ha ha!";
    private int mCode;
    private String mMessage;
    private boolean mAsync;
    public ZRouterErrorAction() {
        mCode = ZRouterActionResult.CODE_ERROR;
        mMessage = DEFAULT_MESSAGE;
        mAsync = false;
    }

    public ZRouterErrorAction(boolean isAsync, int code, String message) {
        this.mCode = code;
        this.mMessage = message;
        this.mAsync = isAsync;
    }

    @Override
    public boolean isAsync(Context context, HashMap<String, String> requestData) {
        return mAsync;
    }

    @Override
    public ZRouterActionResult invoke(Context context, HashMap<String, String> requestData) {
        ZRouterActionResult result = new ZRouterActionResult.Builder()
                .code(mCode)
                .msg(mMessage)
                .data(null)
                .object(null)
                .build();
        return result;
    }

}
