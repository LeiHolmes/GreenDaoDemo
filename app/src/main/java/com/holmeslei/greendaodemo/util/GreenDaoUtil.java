package com.holmeslei.greendaodemo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.holmeslei.greendaodemo.database.DaoMaster;
import com.holmeslei.greendaodemo.database.DaoSession;

/**
 * Description:
 * author         xulei
 * Date           2017/7/5
 */

public class GreenDaoUtil {
    private static DaoSession daoSession;

    /**
     * 初始化数据库
     * 建议放在Application中执行
     */
    public static void initDataBase(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "magicalpen.db", null);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
