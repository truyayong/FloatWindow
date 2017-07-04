package com.truyayong.floatwindowstory.floatwindow;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.truyayong.floatwindowstory.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/7/4.
 */

public class FloatWindowShrink extends LinearLayout {

    private static final String TAG = FloatWindowShrink.class.getSimpleName();

    /**
     * 记录收缩悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录收缩悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 用于更新收缩悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 收缩悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xTouchScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yTouchScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownTouchScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownTouchScreen;

    /**
     * 记录手指按下时在收缩悬浮窗的View上的横坐标的值
     */
    private float xTouchView;

    /**
     * 记录手指按下时在收缩悬浮窗的View上的纵坐标的值
     */
    private float yTouchView;

    public static WindowManager.LayoutParams lastPara;


    public FloatWindowShrink(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_shrink, this);
        View view = findViewById(R.id.float_window_shrink);
        viewHeight = view.getLayoutParams().height;
        viewWidth = view.getLayoutParams().width;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xTouchView = event.getX();
                yTouchView = event.getY();
                xDownTouchScreen = event.getRawX();
                yDownTouchScreen = event.getRawY() - getStatusBarHeight();
                xTouchScreen = event.getRawX();
                yTouchScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xTouchScreen = event.getRawX();
                yTouchScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                if (xDownTouchScreen == xTouchScreen && yDownTouchScreen == yTouchScreen) {
                    openExpandWindow();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void openExpandWindow() {
        FloatWindowManager.removeShrinkWindow(getContext());
        FloatWindowManager.showExpandWindow(getContext());
    }


    /**
     * 更新收缩悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xTouchScreen - xTouchView);
        mParams.y = (int) (yTouchScreen - yTouchView);
        lastPara = mParams;
        try {
            if (true) {//FloatWindowService.getInRoom() && MyWindowManager.floatChatRoomBigView == null && MyWindowManager.floatChatRoomSmallView == this
                windowManager.updateViewLayout(this, mParams);
            }

            if (FloatWindowManager.floatWindowExpand != null) {
                windowManager.removeView(FloatWindowManager.floatWindowExpand);
            }
        } catch (Exception e) {
            Log.e(TAG, "updateViewPosition exception",e);
        }
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }
}