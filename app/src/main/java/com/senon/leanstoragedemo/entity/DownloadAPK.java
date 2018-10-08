package com.senon.leanstoragedemo.entity;

import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

import java.io.Serializable;

/**
 * 实体--登录
 */
@AVClassName("Apk")
public class DownloadAPK extends AVObject implements Serializable {
    public static final Parcelable.Creator CREATOR = AVObject.AVObjectCreator.instance;//可序列化

    public static final String VERSION = "version";//APK版本
    public static final String TITLE = "title";//更新标题
    public static final String CONTENT = "content";//更新内容
    public static final String FORCED_UPDATE = "forcedUpdate";//强制升级
    public static final String File = "file";//文件


    public DownloadAPK() {
    }

    public int getVersion() {
        return getInt(VERSION);
    }
    public void setVersion(int version) {
        put(VERSION, version);
    }

    public String getTitle() {
        return getString(TITLE);
    }
    public void setTitle(String title) {
        put(TITLE, title);
    }

    public String getContent() {
        return getString(CONTENT);
    }
    public void setContent(String content) {
        put(CONTENT, content);
    }

    public boolean getForcedUpdate() {
        return getBoolean(FORCED_UPDATE);
    }
    public void setForcedUpdate(boolean forcedUpdate) {
        put(FORCED_UPDATE, forcedUpdate);
    }

    public AVFile getAvFile() {
        return getAVFile(File);
    }
    public void setAvFile(AVFile avFile) {
        put(File, avFile);
    }
}
