package com.senon.leanstoragedemo;

import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.senon.leanstoragedemo.util.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/9/28.
 */
public class AVUtil<T extends AVQuery<AVObject>>{

    private T t;
    private OnAVUtilListener listener;


    public void setOnAVUtilListener(AVQuery t,final OnAVUtilListener listener){
        this.listener = listener;
        this.t = (T) t;
        t.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
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



    public interface OnAVUtilListener{
        void onSuccess(List<AVObject> list);
//        void onFailer();
//        void onZero();
    }
}
