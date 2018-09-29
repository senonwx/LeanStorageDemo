package com.senon.leanstoragedemo.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import java.io.Serializable;
import java.util.List;

/**
 * 实体--学生
 */
@AVClassName("Student")
public class Student extends AVObject implements Serializable {
  public static final Creator CREATOR = AVObjectCreator.instance;//可序列化

  public static final String NAME = "student_name";//姓名
  public static final String TOTAL_COUNT = "total_count";//总数
  public static final String LAST_COUNT = "last_count";//剩余数
  public static final String T0TAL_MONEY = "total_money";//总金额
  public static final String LAST_MONEY = "last_money";//剩余金额
  public static final String SIGN_TIME = "sign_time";//最近签到时间
  public static final String DES = "des";//描述
  public static final String STUDENT_DETAILS = "student_details";//学生的详情
  public static final String AVATAR = "avatar";//头像
  public static final String CREATE_AT = "createAt";//创建时间
  public static final String UPDATE_AT = "updateAt";//更新时间


  public Student() {
  }

  public String getName() {
    return getString(NAME);
  }
  public void setName(String name) {
    put(NAME, name);
  }

  public int getTotalCount() {
    return getInt(TOTAL_COUNT);
  }
  public void setTotalCount(int total_count) {
    put(TOTAL_COUNT, total_count);
  }

  public int getLastCount() {
    return getInt(LAST_COUNT);
  }
  public void setLastCount(int last_count) {
    put(LAST_COUNT, last_count);
  }

  public int getTotalMoney() {
    return getInt(T0TAL_MONEY);
  }
  public void setTotalMoney(int total_money) {
    put(T0TAL_MONEY, total_money);
  }

  public int getLastMoney() {
    return getInt(LAST_MONEY);
  }
  public void setLastMoney(int last_money) {
    put(LAST_MONEY, last_money);
  }

  public String getSignTime() {
    return getString(SIGN_TIME);
  }
  public void setSignTime(String sign_time) {
    put(SIGN_TIME, sign_time);
  }

  public String getDes() {
    return getString(DES);
  }
  public void setDes(String des) {
    put(DES, des);
  }

  public List<String> getStudentDetails() {
    return getList(STUDENT_DETAILS);
  }
  public void setStudentDetails(List<String> student_details) {
    put(STUDENT_DETAILS, student_details);
  }

  public AVFile getAvatar() {
    return getAVFile(AVATAR);
  }
  public void setAvatar(AVFile avatar) {
    put(AVATAR, avatar);
  }

//  public Date getCreateTime() {
//    return getDate(CREATE_AT);
//  }
//  public Date getUpdateTime() {
//    return getDate(UPDATE_AT);
//  }

}
