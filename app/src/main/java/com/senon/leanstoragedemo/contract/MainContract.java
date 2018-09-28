package com.senon.leanstoragedemo.contract;

import com.senon.leanstoragedemo.base.BasePresenter;
import com.senon.leanstoragedemo.base.BaseResponse;
import com.senon.leanstoragedemo.base.BaseView;

import java.util.HashMap;


public interface MainContract {

    interface View extends BaseView {

        void result(BaseResponse data);

        void setMsg(String msg);

    }

    abstract class Presenter extends BasePresenter<View> {

        //请求1
        public abstract void login(HashMap<String, String> map, boolean isDialog, boolean cancelable);

    }
}
