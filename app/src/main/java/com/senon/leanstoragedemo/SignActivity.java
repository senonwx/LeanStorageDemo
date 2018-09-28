package com.senon.leanstoragedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.senon.leanstoragedemo.adapter.RecycleHolder;
import com.senon.leanstoragedemo.adapter.RecyclerAdapter;
import com.senon.leanstoragedemo.base.BaseActivity;
import com.senon.leanstoragedemo.base.BasePresenter;
import com.senon.leanstoragedemo.base.BaseView;
import com.senon.leanstoragedemo.entity.ClassLevel;
import com.senon.leanstoragedemo.greendaoentity.UserDetails;
import com.senon.leanstoragedemo.greendaoentity.UserReview;
import com.senon.leanstoragedemo.greendaoutil.UserDetailsDt;
import com.senon.leanstoragedemo.greendaoutil.UserReviewDt;
import com.senon.leanstoragedemo.util.AppConfig;
import com.senon.leanstoragedemo.util.BaseEvent;
import com.senon.leanstoragedemo.util.SelectorTimeUtil;
import com.senon.leanstoragedemo.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 学员签到页面
 */
public class SignActivity extends BaseActivity<BaseView, BasePresenter<BaseView>> implements BaseView {

    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.commit_tv)
    TextView commit_tv;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.level_lrv)
    LRecyclerView lrv;
    @BindView(R.id.content_edt)
    EditText content_edt;
    @BindView(R.id.comments_edt)
    EditText comments_edt;
    @BindView(R.id.time_lay)
    RelativeLayout time_lay;
    private List<ClassLevel> levels;
    private RecyclerAdapter<ClassLevel> adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private UserDetailsDt userDetailsDt = new UserDetailsDt();//学员详细信息操作类
    private UserReviewDt userReviewDt = new UserReviewDt();//学员概述操作类
    private String name;//当前学员名字
    private String time;//当前日期
    private int state;//以什么状态进入该页面  0查询   1新增   2修改
    private UserDetails userDetails;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    public void init() {
        name = getIntent().getStringExtra("name");
        time = getIntent().getStringExtra("time");
        state = getIntent().getIntExtra("state",0);

        initState();
        initLevelLrv();
    }

    private void initState() {
        levels = new ClassLevel().getTypeTop();

        if(state == 0){
            name_tv.setText(name+"的签到");
            commit_tv.setText("截屏");

            initData();
            setEnable(false);
        }else if(state == 1){
            name_tv.setText("签到:"+name);
            commit_tv.setText("签到");
        }else if(state == 2){
            name_tv.setText("签到修改:"+name);
            commit_tv.setText("修改");
            initData();

        }
    }

    private void initData(){
        userDetails = userDetailsDt.findByName$Time$Flag(name,time,1);
        time_tv.setText(userDetails.getTime());
        content_edt.setText(userDetails.getContent());
        comments_edt.setText(userDetails.getComments());
        levels.get(userDetails.getLevel()-1).setCheck(true);
        content_edt.setSelection(content_edt.getText().toString().length());

    }

    private void setEnable(boolean enable){
        time_lay.setEnabled(enable);
        content_edt.setEnabled(enable);
        comments_edt.setEnabled(enable);
    }

    //初始化类型Recyclerview
    private void initLevelLrv(){
        lrv.setLayoutManager(new GridLayoutManager(this,5));
        adapter = new RecyclerAdapter<ClassLevel>(this, levels, R.layout.item_sign_level) {
            @Override
            public void convert(final RecycleHolder helper, final ClassLevel item, final int position) {
                helper.setBackgroundResource(R.id.item_igv,item.isCheck() ? item.getImageCheck() : item.getImageNoCheck());
                helper.setTextColor(R.id.item_tv,item.isCheck() ? R.color.smile_yellow : R.color.tablayout_tv_gray);
                helper.setText(R.id.item_tv,item.getDes());

                helper.setEnabled(R.id.item_lay,state != 0);
                helper.setOnClickListener(R.id.item_lay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < levels.size(); i++) {
                            levels.get(i).setCheck( i == position ? true : false);
                        }

                        lrv.refreshComplete(0);
                        mLRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lrv.setAdapter(mLRecyclerViewAdapter);
        lrv.setLoadMoreEnabled(false);
        lrv.setPullRefreshEnabled(false);
    }
    
    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public BaseView createView() {
        return null;
    }
    

    @OnClick({R.id.back_igv, R.id.commit_tv, R.id.time_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.commit_tv:
                if(state == 0){
                    saveBitmap();
                    return;
                }
                String time = time_tv.getText().toString().trim();
                int level = 1;
                String content = content_edt.getText().toString().trim();
                String comments = comments_edt.getText().toString().trim();
                if(time.isEmpty()){
                    ToastUtil.showShortToast("请选择签到时间");
                    return;
                }else if(content.isEmpty()){
                    ToastUtil.showShortToast("请填写上课内容");
                    return;
                }else if(comments.isEmpty()){
                    ToastUtil.showShortToast("请对学生该堂课表现进行评价");
                    return;
                }
                for (int i = 0; i < levels.size(); i++) {
                    if(levels.get(i).isCheck()){
                        level = levels.get(i).getLevel();
                        break;
                    }
                    if(i == levels.size() - 1){
                        ToastUtil.showShortToast("请选择听课等级");
                        return;
                    }
                }

                if(state == 1){//新增
                    UserDetails details = new UserDetails();
                    details.setName(name);
                    details.setTime(time);
                    details.setMoney(AppConfig.PRICE);
                    details.setCount(1);
                    details.setFlag(1);
                    details.setLevel(level);
                    details.setContent(content);
                    details.setComments(comments);
                    if(userDetailsDt.findByName$Time$Flag(name,time,1) != null){
                        ToastUtil.showShortToast("当天已经签过到啦");
                    }else{
                        //减少学员概述中的剩余次数
                        UserReview userReview = userReviewDt.findByName(name);
                        userReview.setSignTime(time);
                        userReview.setLast_count(userReview.getLast_count()-1);
                        userReview.setLast_money(userReview.getLast_money() - AppConfig.PRICE);
                        userReviewDt.update(userReview);

                        //学员签到历史记录增加
                        userDetailsDt.insert(details);
                        ToastUtil.showShortToast("签到成功");

                        BaseEvent event = new BaseEvent();
                        event.setCode(1);
                        EventBus.getDefault().post(event);

                        finish();
                    }
                }else if(state == 2){//修改
                    userDetails.setTime(time);
                    userDetails.setLevel(level);
                    userDetails.setContent(content);
                    userDetails.setComments(comments);

                    userDetailsDt.update(userDetails);
                    ToastUtil.showShortToast("签到修改成功");

                    BaseEvent event = new BaseEvent();
                    event.setCode(2);
                    EventBus.getDefault().post(event);

                    finish();
                }

                break;
            case R.id.time_lay:
                SelectorTimeUtil.choseDateTime(time_tv, null, SignActivity.this);
                break;
        }
    }

    private void saveBitmap(){
        // 获取屏幕
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null){
            revitionImageSize(bmp);
        }
    }

    public void revitionImageSize(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        while (true) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            if (baos.toByteArray().length / 1024 > 512) {
                quality -= 10;
            } else {
                break;
            }
        }
        Bitmap bm = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);

        // 得到图片的宽，高
        int w = bm.getWidth();
        int h = bm.getHeight();

        bm = Bitmap.createBitmap(bitmap, 0, 40, w, h-40, null, false);
        try {
            // 获取内置SD卡路径
            String sdCardPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
            // 图片文件路径
            String filePath = sdCardPath + "/" +System.currentTimeMillis()+name+".jpg";
            File file = new File(filePath);
            judeDirExists(file);
            FileOutputStream os = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
            ToastUtil.showShortToast("截屏成功，在相册中可以查看！");
            //刷新系统相册
            MediaScannerConnection.scanFile(this, new String[]{
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + filePath},
                    null, null);
        } catch (Exception e) {
            ToastUtil.showShortToast("截屏失败，请重试！");
        }
    }
    // 判断文件是否存在
    public void judeDirExists(File file) {
        if (file.exists()) {
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
