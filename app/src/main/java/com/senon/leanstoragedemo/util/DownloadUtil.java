package com.senon.leanstoragedemo.util;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.senon.leanstoragedemo.R;
import com.senon.leanstoragedemo.entity.DownloadAPK;
import com.senon.leanstoragedemo.entity.Login;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * app下载util
 */
public class DownloadUtil {

    private DownloadBuilder builder;
    private Context context;
    private DownloadAPK apk;
    private AVUtil avUtil;
    private int versionCode;
    private boolean isInitiativeCheck;

    public DownloadUtil(Context context,boolean isInitiativeCheck) {
        this.context = context;
        this.isInitiativeCheck = isInitiativeCheck;
        this.avUtil = new AVUtil(context);
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setRequest(){
        builder = AllenVersionChecker
                .getInstance()
                .downloadOnly(crateUIData(apk.getTitle(),apk.getContent(),apk.getAvFile().getUrl()));
        /**
         * 是否为强制更新
         */
        if(!isInitiativeCheck && apk.getForcedUpdate()){
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    forceUpdate();
                }
            });
        }
        builder.setForceRedownload(true);
        builder.setShowDownloadingDialog(true);
        builder.setShowNotification(true);
        builder.setShowDownloadFailDialog(true);
        builder.setCustomDownloadingDialogListener(createCustomDownloadingDialog());
//        builder.setDownloadAPKPath(address);

        builder.excuteMission(context);

    }

    public void checkVersion()  {
        AVQuery<DownloadAPK> query = AVObject.getQuery(DownloadAPK.class);
//        query.whereEqualTo(DownloadAPK.NAME,"app-release.apk");
        avUtil.setOnAVUtilListener(query,new AVUtil.OnAVUtilListener() {
            @Override
            public void onSuccess(List<AVObject> list) {
                if(list == null || list.size() == 0){
                    ToastUtil.showShortToast("请求错误！");
                }else{
                    apk = (DownloadAPK) list.get(0);
                    if(apk.getVersion() == versionCode){
                        if(isInitiativeCheck)
                            ToastUtil.showShortToast("已是最新版！");
                    }else{
                        setRequest();
                    }
                }
            }
        });
    }


    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     */
    private UIData crateUIData(String title, String content, String url) {
        UIData uiData = UIData.create();
        uiData.setTitle(title);
        uiData.setDownloadUrl(url);
        uiData.setContent(content);
        return uiData;
    }


    /**
     * 强制更新操作
     * 通常关闭整个activity所有界面，这里方便测试直接关闭当前activity
     */
    private void forceUpdate() {
    }

    /**
     * 自定义下载中对话框，下载中会连续回调此方法 updateUI
     * 务必用库传回来的context 实例化你的dialog
     *
     * @return
     */
    private CustomDownloadingDialogListener createCustomDownloadingDialog() {
        return new CustomDownloadingDialogListener() {
            @Override
            public Dialog getCustomDownloadingDialog(Context context, int progress, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_download_layout);
                return baseDialog;
            }

            @Override
            public void updateUI(Dialog dialog, int progress, UIData versionBundle) {
                TextView tvProgress = dialog.findViewById(R.id.tv_progress);
                ProgressBar progressBar = dialog.findViewById(R.id.pb);
                progressBar.setProgress(progress);
                tvProgress.setText(context.getString(R.string.versionchecklib_progress, progress));
            }
        };
    }
    public class BaseDialog extends Dialog {
        public BaseDialog(Context context, int theme, int res) {
            super(context, theme);
            setContentView(res);
            setCanceledOnTouchOutside(false);
        }

    }
}
