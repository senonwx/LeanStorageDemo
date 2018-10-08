package com.senon.leanstoragedemo.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.senon.leanstoragedemo.R;
import com.senon.leanstoragedemo.util.ComUtil;
import com.senon.leanstoragedemo.util.SelectorTimeUtil;
import com.senon.leanstoragedemo.util.ToastUtil;

/**
 * 充值dialog
 */
public class DialogRecharge extends AlertDialog implements View.OnClickListener {
    private TextView name_tv, time_tv,money_tv,count_tv,des_tv;
    private Button cancel_btn, confirm_btn;
    private RelativeLayout time_lay;
    private Context context;
    private String name;

    public DialogRecharge(Context context,String name) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.name = name;
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_recharge, null);
        name_tv = (TextView) v.findViewById(R.id.name_tv);//姓名
        time_lay = (RelativeLayout) v.findViewById(R.id.time_lay);//时间lay
        time_tv = (TextView) v.findViewById(R.id.time_tv);//时间
        money_tv = (EditText) v.findViewById(R.id.money_tv);//金额
        count_tv = (EditText) v.findViewById(R.id.count_tv);//次数
        des_tv = (EditText) v.findViewById(R.id.des_tv);//备注信息
        cancel_btn = (Button) v.findViewById(R.id.cancel_btn);//取消按钮
        confirm_btn = (Button) v.findViewById(R.id.confirm_btn);//确定按钮

        cancel_btn.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);
        time_lay.setOnClickListener(this);

        name_tv.setText(name);
        this.setView(v);
    }
    @Override
    public void show() {
        super.show();
        //设置宽度全屏，要设置在show的后面
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT*71/75;
        layoutParams.width = (int) (ComUtil.getScreenWidth(context) * 0.9);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        layoutParams.height = (int) (ComUtil.getScreenHeight(context) * 0.25);
//        layoutParams.alpha = 8f; // 透明度
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.confirm_btn:
                if(time_tv.getText().toString().isEmpty()){
                    ToastUtil.showShortToast("请选择时间");
                    return;
                }else if(money_tv.getText().toString().isEmpty()){
                    ToastUtil.showShortToast("请填写金额");
                    return;
                }else if(count_tv.getText().toString().isEmpty()){
                    ToastUtil.showShortToast("请填写次数");
                    return;
                }
                if(onClickListener != null){
                    onClickListener.setConfirmClickListener(
                            time_tv.getText().toString().trim(),
                            money_tv.getText().toString().trim(),
                            count_tv.getText().toString().trim(),
                            des_tv.getText().toString().trim());
                }
                break;
            case R.id.time_lay:
                SelectorTimeUtil.choseDateTime(time_tv, null, context);
                break;
        }
    }

    public void setTitle(String title) {
        this.name_tv.setText(title);
    }

    public void setConfirmText(String text) {
        confirm_btn.setText(text);
    }

    public void setCancelText(String text) {
        cancel_btn.setText(text);
    }

    private OnClickListener onClickListener = null;
    public interface OnClickListener {
        void setConfirmClickListener(String time, String money, String count, String des);
    }
    public void setConfirmClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public void setCancelButtonVisibility() {
        cancel_btn.setVisibility(View.GONE);
    }
}
