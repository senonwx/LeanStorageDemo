package com.senon.leanstoragedemo;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.senon.leanstoragedemo.adapter.RecycleHolder;
import com.senon.leanstoragedemo.adapter.RecyclerAdapter;
import com.senon.leanstoragedemo.base.BaseActivity;
import com.senon.leanstoragedemo.base.BaseResponse;
import com.senon.leanstoragedemo.contract.MainContract;
import com.senon.leanstoragedemo.entity.Student;
import com.senon.leanstoragedemo.greendaoentity.UserReview;
import com.senon.leanstoragedemo.greendaoutil.UserDetailsDt;
import com.senon.leanstoragedemo.greendaoutil.UserReviewDt;
import com.senon.leanstoragedemo.presenter.MainPresenter;
import com.senon.leanstoragedemo.util.BaseEvent;
import com.senon.leanstoragedemo.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * 主页
 */
public class MainActivity extends BaseActivity<MainContract.View, MainContract.Presenter> implements MainContract.View {

    @BindView(R.id.lrv)
    LRecyclerView lrv;

    private RecyclerAdapter<AVObject> adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private boolean isLoadMore = false;//是否加载更多
    private boolean isDownRefesh = false;//是否下拉刷新
    private int currentPage = 0;//当前页数
    private List<AVObject> mData = new ArrayList<>();//原始数据
    private List<AVObject> tempData = new ArrayList<>();//间接数据
    private UserReviewDt userBeanDt = new UserReviewDt();
    private UserDetailsDt userDetailsDt = new UserDetailsDt();
    private DialogAdd$Del dialogAdd$Del;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);

        initData();
        initLrv();
    }

    private void initData() {
        AVQuery<Student> student = AVObject.getQuery(Student.class);
        getAVManager().setOnAVUtilListener(student, new AVUtil.OnAVUtilListener() {
            @Override
            public void onSuccess(List<AVObject> list) {
                if(list == null || list.size() == 0){
                    ToastUtil.showShortToast("没有数据！");
                }else{
                    mData.clear();
                    mData.addAll(list);
                }
                mLRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initLrv() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lrv.setLayoutManager(manager);
        lrv.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader); //设置下拉刷新Progress的样式
//        lrv.setArrowImageView(R.mipmap.news_renovate);  //设置下拉刷新箭头
        lrv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        adapter = new RecyclerAdapter<AVObject>(this, mData, R.layout.item_main_lrv) {
            @Override
            public void convert(final RecycleHolder helper, final AVObject item, final int position) {
                final Student student = (Student) item;
                helper.setText(R.id.name_tv, student.getName());
                helper.setText(R.id.count_tv, student.getLastCount());
                helper.setText(R.id.time_tv, student.getSignTime());

                helper.setOnClickListener(R.id.lay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, UserActivity.class)
                                .putExtra("name", student.getName()));
                    }
                });
            }
        };
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lrv.setAdapter(mLRecyclerViewAdapter);
        lrv.setLoadMoreEnabled(false);
        lrv.setPullRefreshEnabled(false);
        //设置底部加载颜色
        lrv.setFooterViewColor(R.color.color_blue, R.color.text_gray, R.color.elegant_bg);
        lrv.setHeaderViewColor(R.color.color_blue, R.color.text_gray, R.color.elegant_bg);
        lrv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getFirstPageData();
            }
        });
        lrv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                isLoadMore = true;
//                currentPage++;
//                getOrderList();
            }
        });

    }

    @OnClick({R.id.add_igv, R.id.detele_igv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_igv:
                dialogAdd$Del = new DialogAdd$Del(MainActivity.this, "请输入新增学员的名字");
                dialogAdd$Del.setConfirmClickListener(new DialogAdd$Del.OnClickListener() {
                    @Override
                    public void setConfirmClickListener(final String name) {
                        AVQuery<Student> student = AVObject.getQuery(Student.class);
                        student.whereEqualTo(Student.NAME, name);
                        getAVManager().setOnAVUtilListener(student,true,new AVUtil.OnAVUtilListener() {
                            @Override
                            public void onSuccess(List<AVObject> list) {
                                if(list == null || list.size() == 0){
                                    Student stu = new Student();
                                    stu.setName(name);
                                    stu.saveInBackground();

                                    initData();
                                }else{
                                    ToastUtil.showShortToast("姓名重复，请重新输入！");
                                }
                            }
                        });
                        dialogAdd$Del.dismiss();
                    }
                });
                dialogAdd$Del.show();
                break;
            case R.id.detele_igv:
                dialogAdd$Del = new DialogAdd$Del(MainActivity.this, "请输入要删除的学员名字");
                dialogAdd$Del.setConfirmClickListener(new DialogAdd$Del.OnClickListener() {
                    @Override
                    public void setConfirmClickListener(final String name) {
                        setSweetDialog(name,"确认删除?", "删除之后将不能恢复!");
                        dialogAdd$Del.dismiss();
                    }
                });
                dialogAdd$Del.show();
                break;
        }
    }

    private void setSweetDialog(final String name, String title, String tip) {
        SweetAlertDialog sad = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(tip)
                .setCancelText("取消")
                .setConfirmText("确认")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        final AVQuery<Student> student = AVObject.getQuery(Student.class);
                        student.whereEqualTo(Student.NAME, name);
                        getAVManager().setOnAVUtilListener(student,true,new AVUtil.OnAVUtilListener() {
                            @Override
                            public void onSuccess(List<AVObject> list) {
                                if(list == null || list.size() == 0){
                                    ToastUtil.showShortToast("姓名输入错误，未找到学员！");
                                }else{
                                    AVQuery<Student> q = AVObject.getQuery(Student.class);
                                    q.getFirstInBackground(new GetCallback<Student>() {
                                        @Override
                                        public void done(Student student, AVException e) {
                                            student.deleteInBackground();

                                            AVQuery<Student> studentAVQuery = AVQuery.getQuery(Student.class);
                                            studentAVQuery.findInBackground(new FindCallback<Student>() {
                                                @Override
                                                public void done(List<Student> list, AVException e) {
                                                    list.toString();
                                                }
                                            });
                                        }
                                    });
//                                    ((Student)list.get(0)).deleteInBackground();
                                    initData();
                                }
                            }
                        });
                    }
                });
        sad.show();
    }

    @Override
    public MainContract.Presenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public MainContract.View createView() {
        return this;
    }

    @Override
    public void result(BaseResponse data) {
    }

    @Override
    public void setMsg(String msg) {
        ToastUtil.showShortToast(msg);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//在ui线程执行
    public void onDataSynEvent(BaseEvent event) {
        int code = event.getCode();
        if (code == 1 || code == 2 || code == 3 || code == 4) {//1签到  2签到修改 3充值  4删除了历史记录
            initData();
            mLRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
