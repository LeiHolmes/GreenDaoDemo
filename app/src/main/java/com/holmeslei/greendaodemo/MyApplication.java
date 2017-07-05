package com.holmeslei.greendaodemo;

import android.app.Application;

import com.holmeslei.greendaodemo.util.GreenDaoUtil;

/**
 * Description:
 * author         xulei
 * Date           2017/7/5
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoUtil.initDataBase(getApplicationContext());
    }
}
