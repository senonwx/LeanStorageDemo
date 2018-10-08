package com.senon.leanstoragedemo.contract;

import com.avos.avoscloud.AVObject;
import com.senon.leanstoragedemo.base.BasePresenter;
import com.senon.leanstoragedemo.base.BaseView;
import java.util.List;


public interface LoginContract {

    interface View extends BaseView {

        void result(List<AVObject> data, boolean isAutoLogin);

        void setMsg(String msg);

    }

    abstract class Presenter extends BasePresenter<View> {

        //请求1
        public abstract void login(String phone,String password, boolean isDialog,boolean isAutoLogin);

    }
}
