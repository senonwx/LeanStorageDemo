package com.senon.leanstoragedemo.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

import java.io.Serializable;
import java.util.List;

/**
 * 实体--登录
 */
@AVClassName("LoginVerify")
public class Login extends AVObject implements Serializable {
  public static final Creator CREATOR = AVObjectCreator.instance;//可序列化

  public static final String ACCOUNT = "account";//帐号
  public static final String PASSWORD = "password";//密码


  public Login() {
  }

  public String getAccount() {
    return getString(ACCOUNT);
  }
  public void setAccount(String account) {
    put(ACCOUNT, account);
  }

  public int getPassword() {
    return getInt(PASSWORD);
  }
  public void setPassword(String password) {
    put(PASSWORD, password);
  }

}
