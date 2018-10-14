package com.senon.leanstoragedemo.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 公共util
 */
public class ComUtil {

    // 屏幕宽度（像素）
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static String getLevelStr(int level){
        switch (level){
            case 1:
                return "非常好";
            case 2:
                return "很好";
            case 3:
                return "良好";
            case 4:
                return "一般";
            case 5:
                return "较差";
            default:
                return "很好";
        }
    }


}
