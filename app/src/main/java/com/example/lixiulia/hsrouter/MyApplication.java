package com.example.lixiulia.hsrouter;

import android.app.Application;

import com.example.hsrouter.HsRouter;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HsRouter.getInstance().init(this);
    }
}
