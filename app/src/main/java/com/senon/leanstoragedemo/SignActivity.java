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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SaveCallback;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.senon.leanstoragedemo.adapter.RecycleHolder;
import com.senon.leanstoragedemo.adapter.RecyclerAdapter;
import com.senon.leanstoragedemo.base.BaseActivity;
import com.senon.leanstoragedemo.base.BasePresenter;
import com.senon.leanstoragedemo.base.BaseView;
import com.senon.leanstoragedemo.entity.ClassLevel;
import com.senon.leanstoragedemo.entity.Student;
import com.senon.leanstoragedemo.entity.StudentDetails;
import com.senon.leanstoragedemo.util.AppConfig;
import com.senon.leanstoragedemo.util.BaseEvent;
import com.senon.leanstoragedemo.util.SelectorTimeUtil;
import com.senon.leanstoragedemo.util.ToastUtil;
import org.greenrobot.eventbus.EventBus;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @BindView(R.id.homework_edt)
    EditText homework_edt;
    @BindView(R.id.time_lay)
    RelativeLayout time_lay;
    private List<ClassLevel> levels;
    private RecyclerAdapter<ClassLevel> adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private String name;//当前学员名字
    private int state;//以什么状态进入该页面  0查询   1新增   2修改
    private StudentDetails details;
    private Student student;


    @Override
    public int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    public void init() {
        name = getIntent().getStringExtra("name");
        details = (StudentDetails) getIntent().getSerializableExtra("details");
        student = (Student) getIntent().getSerializableExtra("student");
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
        time_tv.setText(details.getTime());
        content_edt.setText(details.getContent());
        comments_edt.setText(details.getComments());
        homework_edt.setText(details.getHomework());
        levels.get(details.getLevel()-1).setCheck(true);
        content_edt.setSelection(content_edt.getText().toString().length());

    }

    private void setEnable(boolean enable){
        time_lay.setEnabled(enable);
        content_edt.setEnabled(enable);
        comments_edt.setEnabled(enable);
        homework_edt.setEnabled(enable);
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
                final String time = time_tv.getText().toString().trim();
                int level = 1;
                String content = content_edt.getText().toString().trim();
                String comments = comments_edt.getText().toString().trim();
                String homework = homework_edt.getText().toString().trim();
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
                    final StudentDetails stde = new StudentDetails();
                    stde.setName(name);
                    stde.setTime(time);
                    stde.setMoney(AppConfig.PRICE);
                    stde.setCount(1);
                    stde.setFlag(1);
                    stde.setLevel(level);
                    stde.setContent(content);
                    stde.setComments(comments);
                    stde.setHomework(homework);
                    stde.setOwner(student);

                    AVQuery<StudentDetails> query = AVQuery.getQuery(StudentDetails.class);
                    query.whereEqualTo(StudentDetails.NAME,student.getName());
                    query.whereEqualTo(StudentDetails.TIME,time);
                    query.whereEqualTo(StudentDetails.FLAG,1);
                    getAVManager().setOnAVUtilListener(query, true, new AVUtil.OnAVUtilListener() {
                        @Override
                        public void onSuccess(List<AVObject> list) {
                            if(list == null || list.size() == 0){
                                //减少学员概述中的剩余次数
                                student.setSignTime(time);
                                student.setLastCount(student.getLastCount() - 1);
                                student.setLastMoney(student.getLastMoney() - AppConfig.PRICE);

                                //学员签到历史记录增加
                                stde.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if(e == null){
                                            BaseEvent event = new BaseEvent();
                                            event.setCode(1);
                                            EventBus.getDefault().post(event);

                                            ToastUtil.showShortToast("签到成功！");
                                            finish();
                                        }
                                    }
                                });

                            }else{
                                ToastUtil.showShortToast("当天已经签过到啦");
                            }
                        }
                    });
                }else if(state == 2){//修改
                    details.setTime(time);
                    details.setLevel(level);
                    details.setContent(content);
                    details.setComments(comments);
                    details.setHomework(homework);
                    details.saveInBackground();
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
