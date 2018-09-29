package com.senon.leanstoragedemo.entity;

import android.os.Parcelable;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.io.Serializable;

/**
 * 实体--学生详情
 */
@AVClassName("StudentDetails")
public class StudentDetails extends AVObject implements Serializable {
    public static final Parcelable.Creator CREATOR = AVObject.AVObjectCreator.instance;//可序列化

    public static final String NAME = "student_name";//姓名
    public static final String FLAG = "flag";//标记是否为  签到:1  充值:2
    public static final String COUNT = "count";//次数
    public static final String MONEY = "money";//金额
    public static final String LEVEL = "level";//上课评价等级（优良中差等）
    public static final String CONTENT = "content";//补课内容
    public static final String TIME = "time";//操作时间
    public static final String HOMEWORK = "homework";//课后作业
    public static final String COMMETNS = "comments";//老师评价
    public static final String OWNER = "owner";//pointer  指向Student类型
    public static final String CREATE_AT = "createAt";//创建时间
    public static final String UPDATE_AT = "updateAt";//更新时间


    public StudentDetails() {
    }

    public String getName() {
        return getString(NAME);
    }
    public void setName(String name) {
        put(NAME, name);
    }

    public int getFlag() {
        return getInt(FLAG);
    }
    public void setFlag(int flag) {
        put(FLAG, flag);
    }

    public int getCount() {
        return getInt(COUNT);
    }
    public void setCount(int count) {
        put(COUNT, count);
    }

    public int getMoney() {
        return getInt(MONEY);
    }
    public void setMoney(int money) {
        put(MONEY, money);
    }

    public int getLevel() {
        return getInt(LEVEL);
    }
    public void setLevel(int level) {
        put(LEVEL, level);
    }

    public String getContent() {
        return getString(CONTENT);
    }
    public void setContent(String content) {
        put(CONTENT, content);
    }

    public String getTime() {
        return getString(TIME);
    }
    public void setTime(String time) {
        put(TIME, time);
    }

    public String getHomework() {
        return getString(HOMEWORK);
    }
    public void setHomework(String homework) {
        put(HOMEWORK, homework);
    }

    public String getComments() {
        return getString(COMMETNS);
    }
    public void setComments(String comments) {
        put(COMMETNS, comments);
    }

    public Student getOwner() {
        return getAVObject(OWNER);
    }
    public void setOwner(Student student) {
        put(OWNER, student);
    }

//  public Date getCreateTime() {
//    return getDate(CREATE_AT);
//  }
//  public Date getUpdateTime() {
//    return getDate(UPDATE_AT);
//  }

}