package com.truyayong.floatwindowstory.floatwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.truyayong.floatwindowstory.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/7/4.
 */

public class FloatWindowExpand extends LinearLayout {
    private final static String TAG = FloatWindowExpand.class.getSimpleName();

    /**
     * 记录扩展悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录扩展悬浮窗的高度
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

    private CircleImageView fci_room;

    public FloatWindowExpand(final Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_expand, this);
        View view = findViewById(R.id.float_window_expand);
        fci_room = (CircleImageView)view.findViewById(R.id.fci_room);
        fci_room.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               FloatWindowService.enterMainListener.enterMain();
            }
        });
        viewHeight = view.getLayoutParams().height;
        viewWidth = view.getLayoutParams().width;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                FloatWindowManager.removeExpandWindow(getContext());
                FloatWindowManager.showShrinkWindow(getContext());
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }
}
