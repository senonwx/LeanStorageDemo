package com.senon.leanstoragedemo.greendaoutil;


import com.senon.leanstoragedemo.base.BaseApplication;
import com.senon.leanstoragedemo.greendao.DaoMaster;
import com.senon.leanstoragedemo.greendao.DaoSession;
import com.senon.leanstoragedemo.util.AppConfig;

public enum SqlEnu {
    Local,
    Sales;
    private static MSQLiteOpenHelper helper_local;
    private static MSQLiteOpenHelper helper_sales;
    private static DaoMaster mDaoMaster_local;
    private static DaoMaster mDaoMaster_sales;
    private static DaoSession mDaoSession_local;
    private static DaoSession mDaoSession_sales;


    // 返回连接对象
    public DaoSession cn() {
        switch (this) {
            case Local:
                if (helper_local == null) {
                    helper_local = new MSQLiteOpenHelper(BaseApplication.getContext(),
                            AppConfig.TABLE_NAME, null);
                    mDaoMaster_local = new DaoMaster(helper_local.getWritableDatabase());
                    mDaoSession_local = mDaoMaster_local.newSession();
                }
                return mDaoSession_local;
            case Sales:
                if (helper_sales == null) {
                    helper_sales = new MSQLiteOpenHelper(BaseApplication.getContext(),
                            "my_test.db", null);
                    mDaoMaster_sales = new DaoMaster(helper_sales.getWritableDatabase());
                    mDaoSession_sales = mDaoMaster_sales.newSession();
                }
                return mDaoSession_sales;
            default:
                break;
        }
        return null;
    }
}
