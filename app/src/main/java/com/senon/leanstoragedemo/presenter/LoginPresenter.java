package com.senon.leanstoragedemo.presenter;

import android.content.Context;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.senon.leanstoragedemo.contract.LoginContract;
import com.senon.leanstoragedemo.entity.Login;
import com.senon.leanstoragedemo.util.AVUtil;
import java.util.List;


public class LoginPresenter extends LoginContract.Presenter{

    private Context context;
    private AVUtil avUtil;
    public LoginPresenter(Context context) {
        this.context = context;
        avUtil = new AVUtil(context);
    }

    @Override
    public void login(String phone, String password, boolean isDialog, final boolean isAutoLogin) {
        AVQuery<Login> account = AVObject.getQuery(Login.class);
        account.whereEqualTo(Login.ACCOUNT,phone);
        account.whereEqualTo(Login.PASSWORD,password);
        avUtil.setOnAVUtilListener(account,isDialog, new AVUtil.OnAVUtilListener() {
            @Override
            public void onSuccess(List<AVObject> list) {
                getView().result(list,isAutoLogin);
            }
        });
    }
}
