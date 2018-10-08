package com.senon.leanstoragedemo.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SaveCallback;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.senon.leanstoragedemo.util.AVUtil;
import com.senon.leanstoragedemo.dialog.DialogAdd$Del;
import com.senon.leanstoragedemo.R;
import com.senon.leanstoragedemo.adapter.RecycleHolder;
import com.senon.leanstoragedemo.adapter.RecyclerAdapter;
import com.senon.leanstoragedemo.base.BaseActivity;
import com.senon.leanstoragedemo.base.BaseResponse;
import com.senon.leanstoragedemo.contract.MainContract;
import com.senon.leanstoragedemo.entity.Student;
import com.senon.leanstoragedemo.entity.StudentDetails;
import com.senon.leanstoragedemo.presenter.MainPresenter;
import com.senon.leanstoragedemo.util.BaseEvent;
import com.senon.leanstoragedemo.util.ToastUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
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
    private int pageLimit = 10;//每页条数
    private List<AVObject> mData = new ArrayList<>();//原始数据
    private List<AVObject> tempData = new ArrayList<>();//间接数据
    private DialogAdd$Del dialogAdd$Del;
    private long mExitTime;//点击退出时间差


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);

        initLrv();
    }

    private void initLrv() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lrv.setLayoutManager(manager);
        lrv.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader); //设置下拉刷新Progress的样式
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
                                .putExtra("student", item));
                    }
                });
            }
        };
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lrv.setAdapter(mLRecyclerViewAdapter);
        //设置底部加载颜色
        lrv.setFooterViewColor(R.color.color_blue, R.color.text_gray, R.color.elegant_bg);
        lrv.setHeaderViewColor(R.color.color_blue, R.color.text_gray, R.color.elegant_bg);
        lrv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
        lrv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isLoadMore = true;
                currentPage++;
                getOrderList();
            }
        });
        lrv.forceToRefresh();
    }

    /**
     * 获取第一页数据
     */
    private void getFirstPageData() {
        isDownRefesh = true;
        currentPage = 0;
        getOrderList();
    }

    private void getForceToRefresh(){
        lrv.scrollToPosition(0);
        currentPage = 0;
        isLoadMore = false;
        isDownRefesh = false;
        lrv.forceToRefresh();
    }

    private void getOrderList() {
        AVQuery<Student> student = AVObject.getQuery(Student.class);
        student.orderByAscending("createdAt");//按照创建时间升序排列
        student.limit(pageLimit);// 最多返回 10 条结果
        student.skip(currentPage * pageLimit);// 跳过 10 * 当前页数 条结果
        getAVManager().setOnAVUtilListener(student, new AVUtil.OnAVUtilListener() {
            @Override
            public void onSuccess(List<AVObject> list) {
                result(list);
            }
        });
    }

    private void result(List<AVObject> data){
        tempData.clear();
        tempData.addAll(data);
        if (tempData.size() == 0 && mData.size() > 0 && isLoadMore) {//最后一页时
            lrv.setNoMore(true);
            isLoadMore = false;
        } else if (isDownRefesh) {//下拉刷新时
            mData.clear();
            mData.addAll(tempData);
            refreshData();
        } else {//加载更多时
            mData.addAll(tempData);
            refreshData();
        }
//        empty_img.setVisibility(mData.size() > 0 ? View.GONE:View.VISIBLE);
    }

    private void refreshData() {
        if (lrv == null) {
            return;
        }
        lrv.refreshComplete(currentPage);
        mLRecyclerViewAdapter.notifyDataSetChanged();
        isDownRefesh = false;
        isLoadMore = false;
    }

    @OnClick({R.id.add_igv, R.id.detele_igv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_igv:
                dialogAdd$Del = new DialogAdd$Del(MainActivity.this, "请输入新增学员的名字");
                dialogAdd$Del.setConfirmClickListener(new DialogAdd$Del.OnClickListener() {
                    @Override
                    public void setConfirmClickListener(final String name) {
                        AVQuery<Student> student = AVQuery.getQuery(Student.class);
                        student.whereEqualTo(Student.NAME, name);
                        getAVManager().setOnAVUtilListener(student, true, new AVUtil.OnAVUtilListener() {
                            @Override
                            public void onSuccess(List<AVObject> list) {
                                if (list == null || list.size() == 0) {
                                    Student stu = new Student();
                                    stu.put(Student.NAME,name);
                                    stu.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            getForceToRefresh();
                                        }
                                    });

                                } else {
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
                        setSweetDialog(name, "确认删除?", "删除之后将不能恢复!");
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
                        AVQuery<Student> student = AVQuery.getQuery(Student.class);
                        student.whereEqualTo(Student.NAME, name);
                        getAVManager().setOnAVUtilListener(student,true,new AVUtil.OnAVUtilListener() {
                            @Override
                            public void onSuccess(List<AVObject> list) {
                                if(list == null || list.size() == 0){
                                    ToastUtil.showShortToast("姓名输入错误，未找到学员！");
                                }else{
                                    //删除学员详细的信息列表
                                    AVQuery<StudentDetails> details = AVQuery.getQuery(StudentDetails.class);
                                    details.whereEqualTo(StudentDetails.OWNER, list.get(0));
                                    getAVManager().setOnAVUtilListener(details,true,new AVUtil.OnAVUtilListener() {
                                        @Override
                                        public void onSuccess(List<AVObject> list) {
                                            if(list == null || list.size() == 0){
//                                                ToastUtil.showShortToast("姓名输入错误，未找到学员！");
                                            }else{
                                                //循环删除学员详细的信息
                                                for (AVObject city : list) {
                                                    city.deleteInBackground();
                                                }
                                            }
                                        }
                                    });

                                    //删除学员表信息
                                    (list.get(0)).deleteInBackground();
                                    getForceToRefresh();
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
            getForceToRefresh();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                ToastUtil.showShortToast("再按一次返回键退出");
                //并记录下本次点击“返回键”的时刻，以便下次  进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                finish();
//                    moveTaskToBack(false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
