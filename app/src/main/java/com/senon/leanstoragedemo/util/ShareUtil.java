package com.senon.leanstoragedemo.util;

import android.content.Context;

import com.senon.leanstoragedemo.R;

import cn.sharesdk.onekeyshare.OnekeyShare;

import static com.mob.tools.utils.Strings.getString;

/**
 * 分享util
 */

public class ShareUtil {

    public static void showShare(Context context,String userName,String picturePath) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("看看"+userName+"近期的表现吧");
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
//        oks.setText("详情请查看图片");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(picturePath);//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(context);
    }

    public static void downloadShare(Context context,String picturePath) {
        String downloadUrl = "https://www.pgyer.com/nancyclass";

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("我分享了 Nancy课堂 App下载地址");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(downloadUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("老师上课内容，学生学习内容、表现等信息均可记录");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(picturePath);//确保SDcard下面存在此张图片
        // url仅在微信（包括秀友和朋友圈）中使用
        oks.setUrl(downloadUrl);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("我分享了 "+ getString(R.string.app_name) + "App 下载地址");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(downloadUrl);

        // 启动分享GUI
        oks.show(context);
    }
}
