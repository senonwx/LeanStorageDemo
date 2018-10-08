package com.senon.leanstoragedemo.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.senon.leanstoragedemo.base.BaseApplication;

/**
 * 共享参数
 */

public class ShareUtil {

    private static final String TABLE_NAME = "nancy_class";
    private static final String LOGIN_ACCOUNT = "login_account";
    private static final String LOGIN_PASSWORD = "login_password";


    public static String getLoginAccount(){
        SharedPreferences preferences = BaseApplication.getContext().
                getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE);
        String parm= preferences.getString(LOGIN_ACCOUNT, "");
        return parm;
    }

    public static String getLoginPassword(){
        SharedPreferences preferences = BaseApplication.getContext().
                getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE);
        String parm= preferences.getString(LOGIN_PASSWORD, "");
        return parm;
    }


    public static void setLoginAccount(String account){
        SharedPreferences preferences = BaseApplication.getContext().
                getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOGIN_ACCOUNT, account);
        editor.commit();
    }

    public static void setLoginPassword(String password){
        SharedPreferences preferences = BaseApplication.getContext().
                getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOGIN_PASSWORD, password);
        editor.commit();
    }

}
