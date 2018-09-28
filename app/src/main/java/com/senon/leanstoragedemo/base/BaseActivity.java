package com.senon.leanstoragedemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.senon.leanstoragedemo.AVUtil;

import butterknife.ButterKnife;

/**
 * 基类
 */
public abstract class BaseActivity<V extends BaseView,P extends BasePresenter<V>> extends AppCompatActivity {

    //引用V层和P层
    private P presenter;
    private V view;
    private AVUtil a;
    public P getPresenter(){
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtils.with(this).init();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        if(presenter == null){
            presenter = createPresenter();
        }
        if(view == null){
            view = createView();
        }
        if(presenter != null && view != null){
            presenter.attachView(view);
        }
        init();
    }

    //由子类指定具体类型
    public abstract int getLayoutId();
    public abstract void init();
    public abstract P createPresenter();
    public abstract V createView();

    public AVUtil getAVManager(){
        if(a == null){
            return new AVUtil();
        }
        return a;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter != null){
            presenter.detachView();
        }
    }
}
