package com.truyayong.floatwindowstory.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/7/4.
 */

public class FloatWindowManager {

    private static final String TAG = FloatWindowManager.class.getSimpleName();

    public static FloatWindowShrink floatWindowShrink;

    public static FloatWindowExpand floatWindowExpand;

    private static WindowManager.LayoutParams shrinkWindowParams;

    private static WindowManager.LayoutParams expandWindowParams;

    private static WindowManager mWindowManager;

    public static void showShrinkWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        shrinkWindowParams = FloatWindowShrink.lastPara;
        floatWindowShrink = new FloatWindowShrink(context);
        if (shrinkWindowParams == null) {
            shrinkWindowParams = new WindowManager.LayoutParams();
            if (false) {//PermissionUtil.checkFloatWindowPermission(context)
                shrinkWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                shrinkWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
            shrinkWindowParams.format = PixelFormat.RGBA_8888;
            shrinkWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            shrinkWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
            shrinkWindowParams.width = FloatWindowShrink.viewWidth;
            shrinkWindowParams.height = FloatWindowShrink.viewHeight;
            shrinkWindowParams.x = screenWidth;
            shrinkWindowParams.y = screenHeight / 2;
        }
        floatWindowShrink.setParams(shrinkWindowParams);
        windowManager.addView(floatWindowShrink, shrinkWindowParams);
    }

    public static void removeShrinkWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        try {
            if (floatWindowShrink != null) {
                windowManager.removeView(floatWindowShrink);
                floatWindowShrink = null;
                shrinkWindowParams = null;
            }
        } catch (Exception e) {
            floatWindowShrink = null;
            shrinkWindowParams = null;
            Log.e(TAG, "removeSmallWindow exception",e);
        }
    }

    public static void showExpandWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        floatWindowExpand = new FloatWindowExpand(context);
        if (expandWindowParams == null) {
            expandWindowParams = new WindowManager.LayoutParams();
            expandWindowParams.x = screenWidth / 2 - FloatWindowExpand.viewWidth / 2;
            expandWindowParams.y = screenHeight / 2 - FloatWindowExpand.viewHeight / 2;
            if (false) { //PermissionUtil.checkFloatWindowPermission(context)
                expandWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                expandWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
            expandWindowParams.format = PixelFormat.RGBA_8888;
            expandWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
            expandWindowParams.width = FloatWindowExpand.viewWidth;
            expandWindowParams.height = FloatWindowExpand.viewHeight;
        }
        floatWindowExpand.setParams(expandWindowParams);
        windowManager.addView(floatWindowExpand, expandWindowParams);
    }

    public static void removeExpandWindow(Context context) {
        try {
            if (floatWindowExpand != null) {
                WindowManager windowManager = getWindowManager(context);
                windowManager.removeView(floatWindowExpand);
                floatWindowExpand = null;
                expandWindowParams = null;
            }
        } catch (Exception e) {
            floatWindowExpand = null;
            expandWindowParams = null;
            Log.e(TAG, " removeBigWindow exception : ", e);
        }
    }

    public static boolean isWindowShowing() {
        return floatWindowShrink != null || expandWindowParams != null;
    }

    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}

