package com.senon.leanstoragedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.senon.leanstoragedemo.util.ComUtil;
import com.senon.leanstoragedemo.util.ToastUtil;


/**
 * 学员新增或者删除
 */
public class DialogAdd$Del extends AlertDialog implements View.OnClickListener {
    private TextView title_tv;
    private EditText name_tv;
    private Button cancel_btn, confirm_btn;
    private Context context;


    public DialogAdd$Del(Context context,String title) {
        super(context, R.style.MyDialog);
        this.context = context;
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_add, null);
        name_tv = (EditText) v.findViewById(R.id.name_tv);//姓名
        title_tv = (TextView) v.findViewById(R.id.title_tv);//标题
        cancel_btn = (Button) v.findViewById(R.id.cancel_btn);//取消按钮
        confirm_btn = (Button) v.findViewById(R.id.confirm_btn);//确定按钮

        cancel_btn.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);

        title_tv.setText(title);
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
                String name = name_tv.getText().toString().trim();
                if(name.isEmpty()){
                    ToastUtil.showShortToast("请输入学员的名字");
                    return;
                }
                if(onClickListener != null){
                    onClickListener.setConfirmClickListener(name);
                }
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
        void setConfirmClickListener(String name);
    }
    public void setConfirmClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public void setCancelButtonVisibility() {
        cancel_btn.setVisibility(View.GONE);
    }
}
