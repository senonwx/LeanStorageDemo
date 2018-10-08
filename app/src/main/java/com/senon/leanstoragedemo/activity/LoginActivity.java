package com.senon.leanstoragedemo.activity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.avos.avoscloud.AVObject;
import com.senon.leanstoragedemo.R;
import com.senon.leanstoragedemo.base.BaseActivity;
import com.senon.leanstoragedemo.contract.LoginContract;
import com.senon.leanstoragedemo.presenter.LoginPresenter;
import com.senon.leanstoragedemo.util.ShareUtil;
import com.senon.leanstoragedemo.util.ToastUtil;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity<LoginContract.View, LoginContract.Presenter>
        implements LoginContract.View{

    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.login_btn)
    Button login_btn;


    @Override
    public int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    public LoginContract.Presenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public LoginContract.View createView() {
        return this;
    }

    @Override
    public void init() {
        String account = ShareUtil.getLoginAccount();
        String password = ShareUtil.getLoginPassword();
        if(!account.isEmpty() && !password.isEmpty()){
            getPresenter().login(account,password, true, true);
        }
    }

    @Override
    public void result(List<AVObject>  data,boolean isAutoLogin) {
        if(data != null && data.size() != 0){
            if(!isAutoLogin){
                ShareUtil.setLoginAccount(account.getText().toString());
                ShareUtil.setLoginPassword(pwd.getText().toString());
            }else{
                setMsg("自动登录成功！");
            }

            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else {
            setMsg("帐号或密码错误！");
        }
    }

    @Override
    public void setMsg(String msg) {
        ToastUtil.showLongToast(msg);
    }

    @OnClick({R.id.login_btn,R.id.register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                String phone = account.getText().toString();
                String password = pwd.getText().toString();
                if (phone.isEmpty()) {
                    setMsg("请输入帐号");
                    return;
                }
                if (password.isEmpty()) {
                    setMsg("请输入密码");
                    return;
                }
                getPresenter().login(phone,password, true, false);

                break;
            case R.id.register_btn:
                break;
        }
    }

}
