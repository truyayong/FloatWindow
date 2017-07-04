package com.truyayong.floatwindowstory.floatwindow;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class FloatWindowService extends Service {

    private static final String TAG = FloatWindowService.class.getSimpleName();

    public static EnterMainListener enterMainListener;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new FloatWindowBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class FloatWindowBinder extends Binder {

        private long lastUpdateWindowTime = System.currentTimeMillis();

        public void updateFloatWindow() {
            Log.e(TAG, "[truyayong1] updateFloatWindow");
            long curTime = System.currentTimeMillis();
            //刷新频率过快，易导致Binder android.os.TransactionTooLargeException
//            if (curTime - lastUpdateWindowTime < 100) {
//                return;
//            }
            Log.e(TAG, "[truyayong1] isInApp() : " + isInApp() +
                    " FloatWindowManager.isWindowShowing() + " + FloatWindowManager.isWindowShowing());
            if (!isInApp() && !FloatWindowManager.isWindowShowing()) {
                FloatWindowManager.showShrinkWindow(getApplicationContext());
                FloatWindowManager.removeExpandWindow(getApplicationContext());
            } else {
                FloatWindowManager.removeExpandWindow(getApplicationContext());
                FloatWindowManager.removeShrinkWindow(getApplicationContext());
            }

            lastUpdateWindowTime = curTime;
        }

        public void registerEnterMain(@Nullable EnterMainListener listener) {
            enterMainListener = listener;
        }
    }

    public interface EnterMainListener {
        void enterMain();
    }

    /**
     * 判断当前界面是否是Hello
     */
    private boolean isInApp() {
        return getApplicationContext().getPackageName()
                .equals(getTopPackageName());
    }

    private String getTopPackageName() {
        ActivityManager manager = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE));
        if (Build.VERSION.SDK_INT >= 21) {
            List<ActivityManager.RunningAppProcessInfo> pis = manager.getRunningAppProcesses();
            if (pis != null && !pis.isEmpty()) {
                ActivityManager.RunningAppProcessInfo topAppProcess = pis.get(0);
                if (topAppProcess != null && topAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return topAppProcess.processName;
                }
            }
        } else {
            //getRunningTasks() is deprecated since API Level 21 (Android 5.0)
            List localList = manager.getRunningTasks(1);
            if (localList != null && !localList.isEmpty()) {
                ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo)localList.get(0);
                if (localRunningTaskInfo != null && localRunningTaskInfo.topActivity != null) {
                    return localRunningTaskInfo.topActivity.getPackageName();
                }
            }
        }
        return "";
    }
}