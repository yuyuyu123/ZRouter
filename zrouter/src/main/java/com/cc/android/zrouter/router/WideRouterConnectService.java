package com.cc.android.zrouter.router;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.cc.android.zrouter.IWideRouterAIDL;
import com.cc.android.zrouter.ZRouterActionResult;
import com.cc.android.zrouter.ZRouterApplication;
import com.cc.android.zrouter.tools.LogUtil;

/**
 * 广域路由服务
 */
public final class WideRouterConnectService extends Service {
    private static final String TAG = "WideRouterConnectService";

    @Override public void onCreate() {
        super.onCreate();
        if (!(getApplication() instanceof ZRouterApplication)) {
            throw new RuntimeException("Please check your AndroidManifest.xml and make sure the application is instance of ZRouterApplication.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String domain = intent.getStringExtra("domain");
        if (WideRouter.getInstance(ZRouterApplication.getCoreApplication()).mIsStopping) {
            LogUtil.e(TAG, "Bind error: The wide router is stopping.");
            return null;
        }
        if (domain != null && domain.length() > 0) {
            boolean hasRegistered = WideRouter.getInstance(ZRouterApplication.getCoreApplication()).checkLocalRouterHasRegistered(domain);
            if (!hasRegistered) {
                LogUtil.e(TAG, "Bind error: The local router of process " + domain + " is not bidirectional." +
                        "\nPlease create a Service extend LocalRouterConnectService then register it in AndroidManifest.xml and the initializeAllProcessRouter method of ZRouterApplication." +
                        "\nFor example:" +
                        "\n<service android:name=\"XXXConnectService\" android:process=\"your process name\"/>" +
                        "\nWideRouter.registerLocalRouter(\"your process name\",XXXConnectService.class);");
                return null;
            }
            WideRouter.getInstance(ZRouterApplication.getCoreApplication()).connectLocalRouter(domain);
        } else {
            LogUtil.e(TAG, "Bind error: Intent do not have \"domain\" extra!");
            return null;
        }
        return stub;
    }

    IWideRouterAIDL.Stub stub = new IWideRouterAIDL.Stub() {

        @Override
        public boolean checkResponseAsync(String domain, String routerRequest) throws RemoteException {
            return
                    WideRouter.getInstance(ZRouterApplication.getCoreApplication())
                            .answerLocalAsync(domain, routerRequest);
        }

        @Override
        public String route(String domain, String routerRequest) {
            try {
                return WideRouter.getInstance(ZRouterApplication.getCoreApplication())
                        .route(domain, routerRequest)
                        .mResultString;
            } catch (Exception e) {
                e.printStackTrace();
                return new ZRouterActionResult.Builder()
                        .code(ZRouterActionResult.CODE_ERROR)
                        .msg(e.getMessage())
                        .build()
                        .toString();
            }
        }

        @Override
        public boolean stopRouter(String domain) throws RemoteException {
            return WideRouter.getInstance(ZRouterApplication.getCoreApplication())
                    .disconnectLocalRouter(domain);
        }

    };
}
