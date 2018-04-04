package com.cc.android.zrouter;


interface IWideRouterAIDL {

    boolean checkResponseAsync(String domain,String routerRequset);

    String route(String domain,String routerRequest);

    boolean stopRouter(String domain);
}
