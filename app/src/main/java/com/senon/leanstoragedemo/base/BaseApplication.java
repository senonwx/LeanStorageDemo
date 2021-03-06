package com.senon.leanstoragedemo.base;

import android.app.Application;
import android.content.Context;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.mob.MobSDK;
import com.senon.leanstoragedemo.util.AVCloudConfig;
import com.senon.leanstoragedemo.entity.DownloadAPK;
import com.senon.leanstoragedemo.entity.Login;
import com.senon.leanstoragedemo.entity.Student;
import com.senon.leanstoragedemo.entity.StudentDetails;


public class BaseApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        //初始化shareSdk
        MobSDK.init(this);

        //初始化AVOSCloud
        initAVOSCloud();
    }

    private void initAVOSCloud(){
        //AVOSCloud.setNetworkTimeout(20 * 1000);
        AVObject.registerSubclass(Student.class);
        AVObject.registerSubclass(StudentDetails.class);
        AVObject.registerSubclass(Login.class);
        AVObject.registerSubclass(DownloadAPK.class);
        AVOSCloud.setDebugLogEnabled(true);

        AVOSCloud.initialize(getContext(), AVCloudConfig.APP_ID, AVCloudConfig.APP_KEY);
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
