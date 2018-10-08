package com.senon.leanstoragedemo.util;

import android.content.Context;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.senon.leanstoragedemo.base.BaseApplication;
import com.senon.leanstoragedemo.util.ToastUtil;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 查询公共类
 */
public class AVUtil<T extends AVQuery<AVObject>>{

    private T t;
    private Context context;
    private OnAVUtilListener listener;
    private SweetAlertDialog sad;

    public AVUtil(Context context) {
        this.context = context;
    }

    public void setOnAVUtilListener(AVQuery t, boolean showDialog,final OnAVUtilListener listener) {
        this.listener = listener;
        this.t = (T) t;
        showDialog(showDialog);
        t.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                dismissDialog();
                if(e == null){
                    listener.onSuccess(list);
//                    if(list != null || list.size() > 0){
//                        listener.onSuccess(list);
//                    }else{
//                        listener.onZero();
//                    }
                }else{
//                    listener.onFailer();
                    ToastUtil.showShortToast("连接错误，请稍后！");
                }
            }
        });
    }

    public void setOnAVUtilListener(AVQuery t, final OnAVUtilListener listener){
        setOnAVUtilListener(t,false,listener);
    }

    private void showDialog(boolean showDialog){
        if(!showDialog){
            return;
        }
        if(sad == null){
            sad = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("正在加载");
        }
        sad.show();
    }

    private void dismissDialog(){
        if(sad != null){
            sad.dismiss();
        }
    }

    public interface OnAVUtilListener{
        void onSuccess(List<AVObject> list);
//        void onFailer();
//        void onZero();
    }
}
