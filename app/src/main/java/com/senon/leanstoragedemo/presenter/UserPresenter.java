package com.senon.leanstoragedemo.presenter;

import android.content.Context;
import com.senon.leanstoragedemo.contract.UserContract;
import java.util.HashMap;


public class UserPresenter extends UserContract.Presenter{

    private Context context;

    public UserPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void login(HashMap<String, String> map, boolean isDialog, boolean cancelable) {

    }
}
