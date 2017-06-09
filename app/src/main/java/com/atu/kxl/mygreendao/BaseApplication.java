package com.atu.kxl.mygreendao;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.atu.kxl.mygreendao.greendao.DaoMaster;
import com.atu.kxl.mygreendao.greendao.DaoSession;

/**
 * Created by atu on 2017/6/9.
 */

public class BaseApplication extends Application {
    private SQLiteDatabase db;
    private DaoMaster master;
    private DaoSession session;
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        //官方推荐将获取到的daomaster对象放进application层，这样避免多次创建生产daosession对象
        setUpDatabase();
    }

    private void setUpDatabase() {

        /**
         * 通过daomaster的内部类DevOpenHelper得到一个SQLiteDatabase对象
         * 不需要手动的去编写[CREATE TABLE]这样的语句去创建数据库，因为greendao已经帮我们做了
         * attention:默认的DaoMaster.DevOpenHelper在升级的时候，会删除所有的表格，意味着这将导致所有的数据丢失
         * 所以在使用的时候，还要做一层封装，来实现数据库的安全升级
         */
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constants.DB_NAME, null);
        db = helper.getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();

    }

    public DaoSession getSession() {
        return session;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public static BaseApplication getInstance() {
        if (null == instance) {
            synchronized (BaseApplication.class) {
                instance = new BaseApplication();
            }
        }
        return instance;
    }
}
