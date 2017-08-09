package com.truyayong.floatwindowstory;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.truyayong.floatwindowstory.floatwindow.FloatWindowService;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btn;
    private static FloatWindowService.FloatWindowBinder floatWindowBinder;
    ServiceConnection floatWindowConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null && (service instanceof FloatWindowService.FloatWindowBinder)) {
                floatWindowBinder = (FloatWindowService.FloatWindowBinder) service;
                floatWindowBinder.updateFloatWindow();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (floatWindowBinder != null) {
                floatWindowBinder.registerEnterMain(null);
            }
            floatWindowBinder = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn_show_float_window);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (floatWindowConn != null && floatWindowBinder == null) {
            Intent floatIntent = new Intent(this, FloatWindowService.class);
            bindService(floatIntent, floatWindowConn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (floatWindowBinder != null) {
            floatWindowBinder.updateFloatWindow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (floatWindowBinder != null) {
            floatWindowBinder.updateFloatWindow();
        }
        if(floatWindowBinder != null) {
            unbindService(floatWindowConn);
            floatWindowBinder = null;
            floatWindowConn = null;
        }
    }
}

