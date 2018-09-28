package com.senon.leanstoragedemo.greendaoutil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.senon.leanstoragedemo.greendao.DaoMaster;

public class MSQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MSQLiteOpenHelper(Context context, String tableName) {
        super(context, tableName);
    }

    public MSQLiteOpenHelper(Context context, String tableName, SQLiteDatabase.CursorFactory factory) {
        super(context, tableName, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("SQLite", "数据库从" + oldVersion + "升级到" + newVersion);
        //MigrationHelper.migrate(db, UserDao.class, StudentDao.class);
    }
}