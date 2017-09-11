package com.holmeslei.greendaodemo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.holmeslei.greendaodemo.database.CompanyDao;
import com.holmeslei.greendaodemo.database.DaoMaster;
import com.holmeslei.greendaodemo.database.DaoSession;
import com.holmeslei.greendaodemo.database.EmployeeDao;
import com.holmeslei.greendaodemo.entity.Company;
import com.holmeslei.greendaodemo.entity.Employee;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Description:
 * author         xulei
 * Date           2017/7/5
 */

public class GreenDaoUtil {
    private static DaoSession daoSession;
    private static SQLiteDatabase database;

    /**
     * 初始化数据库
     * 建议放在Application中执行
     */
    public static void initDataBase(Context context) {
        //通过DaoMaster的内部类DevOpenHelper，可得到一个SQLiteOpenHelper对象。
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "greendaoutil.db", null); //数据库名称
        database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static SQLiteDatabase getDatabase() {
        return database;
    }

    public static CompanyDao getCompanyDao() {
        return daoSession.getCompanyDao();
    }

    public static QueryBuilder<Company> getCompanyQuery() {
        return daoSession.getCompanyDao().queryBuilder();
    }

    public static EmployeeDao getEmployeeDao() {
        return daoSession.getEmployeeDao();
    }

    public static QueryBuilder<Employee> getEmployeeQuery() {
        return daoSession.getEmployeeDao().queryBuilder();
    }

}
