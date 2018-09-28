package com.senon.leanstoragedemo.base;

import android.app.Application;
import android.content.Context;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.senon.leanstoragedemo.Config;
import com.senon.leanstoragedemo.entity.Student;


public class BaseApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        //    AVOSCloud.setNetworkTimeout(20 * 1000);
    AVObject.registerSubclass(Student.class);
//    AVObject.registerSubclass(Post.class);
        AVOSCloud.setDebugLogEnabled(true);

        AVOSCloud.initialize(getContext(), Config.APP_ID, Config.APP_KEY);
    }

    public static Context getContext(){
        return mContext;
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        //  初始化多dex配置，放置三方包dex文件生成超过65536
//        MultiDex.install(base);
//    }
}
