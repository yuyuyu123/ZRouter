package com.cc.android.zrouter.router;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.cc.android.zrouter.ILocalRouterAIDL;
import com.cc.android.zrouter.ZRouterActionResult;
import com.cc.android.zrouter.ZRouterApplication;

public class LocalRouterConnectService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    ILocalRouterAIDL.Stub stub = new ILocalRouterAIDL.Stub() {

        @Override
        public boolean checkResponseAsync(String routerRequest) throws RemoteException {
            return LocalRouter.getInstance(ZRouterApplication.getCoreApplication()).
                    answerWiderAsync(new RouterRequest
                            .Builder(getApplicationContext())
                            .json(routerRequest)
                            .build());
        }

        @Override
        public String route(String routerRequest) {
            try {
                LocalRouter localRouter = LocalRouter.getInstance(ZRouterApplication.getCoreApplication());
                RouterRequest routerRequest1 = new RouterRequest
                        .Builder(getApplicationContext())
                        .json(routerRequest)
                        .build();
                RouterResponse routerResponse = localRouter.route(LocalRouterConnectService.this,routerRequest1);
                return routerResponse.get();
            } catch (Exception e) {
                e.printStackTrace();
                return new ZRouterActionResult.Builder().msg(e.getMessage()).build().toString();
            }
        }

        @Override
        public boolean stopWideRouter() throws RemoteException {
            LocalRouter
                    .getInstance(ZRouterApplication.getCoreApplication())
                    .disconnectWideRouter();
            return true;
        }

        @Override
        public void connectWideRouter() throws RemoteException {
            LocalRouter
                    .getInstance(ZRouterApplication.getCoreApplication())
                    .connectWideRouter();
        }
    };
}
