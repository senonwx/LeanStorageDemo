package com.senon.leanstoragedemo;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
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
import com.senon.leanstoragedemo.contract.UserContract;
import com.senon.leanstoragedemo.entity.Student;
import com.senon.leanstoragedemo.entity.StudentDetails;
import com.senon.leanstoragedemo.presenter.UserPresenter;
import com.senon.leanstoragedemo.util.BaseEvent;
import com.senon.leanstoragedemo.util.ComUtil;
import com.senon.leanstoragedemo.util.ToastUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * 学员详细 页面
 */
public class UserActivity extends BaseActivity<UserContract.View, UserContract.Presenter> implements UserContract.View{

    @BindView(R.id.lrv)
    LRecyclerView lrv;
    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.total_count_tv)
    TextView total_count_tv;
    @BindView(R.id.last_count_tv)
    TextView last_count_tv;
    @BindView(R.id.total_money_tv)
    TextView total_money_tv;
    @BindView(R.id.last_money_tv)
    TextView last_money_tv;

    private RecyclerAdapter<AVObject> adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private boolean isLoadMore = false;//是否加载更多
    private boolean isDownRefesh = false;//是否下拉刷新
    private int currentPage = 0;//当前页数
    private int pageLimit = 8;//每页条数
    private List<AVObject> mData = new ArrayList<>();//原始数据
    private List<AVObject> tempData = new ArrayList<>();//间接数据
    private DialogRecharge dialogRecharge;
    private Student student;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        student = (Student) getIntent().getSerializableExtra("student");
        name_tv.setText(student.getName()+"的历史记录");

        initLrv();
        setData();
    }

    private void getOrderList() {
        AVQuery<StudentDetails> details = AVObject.getQuery(StudentDetails.class);
        details.whereEqualTo(StudentDetails.OWNER, student);
        details.addDescendingOrder("createdAt");//按照创建时间降序排列
        details.limit(pageLimit);// 最多返回 10 条结果
        details.skip(currentPage * pageLimit);// 跳过 10 * 当前页数 条结果
        getAVManager().setOnAVUtilListener(details, new AVUtil.OnAVUtilListener() {
            @Override
            public void onSuccess(List<AVObject> list) {
                result(list);
            }
        });
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

    }

    private void setData(){
        AVQuery<Student> avQuery = AVQuery.getQuery(Student.class);
        avQuery.getInBackground(student.getObjectId(), new GetCallback<Student>() {
            @Override
            public void done(Student stu, AVException e) {
                student = stu;
                total_count_tv.setText(student.getTotalCount()+"");
                last_count_tv.setText(student.getLastCount()+"");
                total_money_tv.setText(student.getTotalMoney()+"");
                last_money_tv.setText(student.getLastMoney()+"");
            }
        });

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

    private void initLrv() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lrv.setLayoutManager(manager);
        lrv.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader); //设置下拉刷新Progress的样式
//        lrv.setArrowImageView(R.mipmap.news_renovate);  //设置下拉刷新箭头
        lrv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        adapter = new RecyclerAdapter<AVObject>(this, mData, R.layout.item_user_lrv) {
            @Override
            public void convert(final RecycleHolder helper, final AVObject data, final int position) {
                final StudentDetails item = (StudentDetails) data;
                helper.setVisible(R.id.title_tv,position == 0);
                helper.setText(R.id.time_tv,item.getTime());

                if(item.getFlag() == 1){//签到
                    helper.setText(R.id.text2,"表现");
                    helper.setText(R.id.des_tv,"备注:"+item.getComments());
                    helper.setText(R.id.money_tv, ComUtil.getLevelStr(item.getLevel()));
                    helper.setText(R.id.type_tv,"签到");
                    helper.setTextColor(R.id.type_tv,R.color.txt_blue_color);
                }else if(item.getFlag() == 2){//充值
                    helper.setText(R.id.text2,"充值");
                    helper.setText(R.id.des_tv,"备注:"+item.getContent());
                    helper.setText(R.id.money_tv,item.getMoney());
                    helper.setText(R.id.type_tv,"充值");
                    helper.setTextColor(R.id.type_tv,R.color.txt_green_color);
                }

                helper.setOnClickListener(R.id.lay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.getFlag() == 1) {
                            startActivity(new Intent(UserActivity.this,SignActivity.class)
                                    .putExtra("name",item.getName())
                                    .putExtra("state",0)
                                    .putExtra("details", (Serializable) item));
                        }

                    }
                });

                helper.setOnLongClickListener(R.id.lay, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ArrayList<String> list = new ArrayList<>();
                        if(item.getFlag() == 1){
                            list.add("删除该次签到");
                            list.add("更改该次签到");
                        }else if(item.getFlag() == 2){
                            list.add("删除该次充值");
                        }
                        new DialogPopwin(UserActivity.this, list, new DialogPopwin.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                if(position == 0){
                                    setSweetDialog(item, "确认删除?","删除记录之后将不能恢复!");
                                }else if(position == 1){
                                    startActivity(new Intent(UserActivity.this,SignActivity.class)
                                            .putExtra("name",item.getName())
                                            .putExtra("state",2)
                                            .putExtra("details",(Serializable)item));
                                }
                            }
                        }).show();
                        return false;
                    }
                });
            }
        };
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lrv.setAdapter(mLRecyclerViewAdapter);
//        lrv.setLoadMoreEnabled(false);
//        lrv.setPullRefreshEnabled(false);
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

    private void setSweetDialog(final StudentDetails item, String title, String tip){
        SweetAlertDialog sad = new SweetAlertDialog(UserActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                        if(item.getFlag() == 1){//签到
                            int money = item.getMoney();
                            int count = item.getCount();
                            student.setLastMoney(student.getLastMoney() + money);
                            student.setLastCount(student.getLastCount() + count);
                        }else if(item.getFlag() == 2){//充值
                            int money = item.getMoney();
                            int count = item.getCount();
                            student.setTotalMoney(student.getTotalMoney() - money);
                            student.setLastMoney(student.getLastMoney() - money);
                            student.setTotalCount(student.getTotalCount() - count);
                            student.setLastCount(student.getLastCount() - count);
                        }
                        //学员概述
                        student.saveInBackground();
                        //历史记录
                        item.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(AVException e) {
                                if(e == null){
                                    //通知MainActivity刷新页面
                                    BaseEvent event = new BaseEvent();
                                    event.setCode(4);
                                    EventBus.getDefault().post(event);

                                    setData();
                                    getForceToRefresh();
                                }
                            }
                        });
                    }
                });
        sad.show();
    }

    @OnClick({R.id.back_igv,R.id.recharge_btn,R.id.sign_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.recharge_btn:
                if(dialogRecharge == null){
                    dialogRecharge = new DialogRecharge(UserActivity.this,student.getName());
                    dialogRecharge.setConfirmClickListener(new DialogRecharge.OnClickListener() {
                        @Override
                        public void setConfirmClickListener(final String time, final String money, final String count, final String des) {
                            final AVQuery<StudentDetails> details = AVObject.getQuery(StudentDetails.class);
                            details.whereEqualTo(StudentDetails.OWNER, student);
                            details.whereEqualTo(StudentDetails.TIME, time);
                            details.whereEqualTo(StudentDetails.FLAG, 2);//每天只能充值一次
                            getAVManager().setOnAVUtilListener(details, new AVUtil.OnAVUtilListener() {
                                @Override
                                public void onSuccess(List<AVObject> list) {
                                    if(list == null || list.size() == 0){
                                        //更新学员概述次数与金额等
                                        student.setTotalCount(student.getTotalCount()+Integer.parseInt(count));
                                        student.setLastCount(student.getLastCount()+Integer.parseInt(count));
                                        student.setTotalMoney(student.getTotalMoney()+Integer.parseInt(money));
                                        student.setLastMoney(student.getLastMoney()+Integer.parseInt(money));
//                                        student.saveInBackground();


                                        //生成当前充值记录，并插入数据库中
                                        StudentDetails studentDetails = new StudentDetails();
                                        studentDetails.setName(student.getName());
                                        studentDetails.setFlag(2);
                                        studentDetails.setTime(time);
                                        studentDetails.setMoney(Integer.parseInt(money));
                                        studentDetails.setCount(Integer.parseInt(count));
                                        studentDetails.setContent(des);
                                        studentDetails.setOwner(student);
                                        studentDetails.saveInBackground();


                                        BaseEvent event = new BaseEvent();
                                        event.setCode(3);
                                        EventBus.getDefault().post(event);

                                        setData();
                                        getForceToRefresh();
                                        dialogRecharge.dismiss();
                                        ToastUtil.showShortToast("充值成功！");
                                    }else{
                                        ToastUtil.showShortToast("每天只能充值一次哦！");
                                    }
                                }
                            });
                        }
                    });
                }
                dialogRecharge.show();
                break;
            case R.id.sign_btn:
                startActivity(new Intent(UserActivity.this,SignActivity.class)
                                .putExtra("name",student.getName())
                                .putExtra("state",1)
                                .putExtra("student", (Serializable) student));
                break;
        }
    }


    @Override
    public UserContract.Presenter createPresenter() {
        return new UserPresenter(this);
    }

    @Override
    public UserContract.View createView() {
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
        if (code == 1 || code == 2) {//1签到  2签到修改
            setData();
            getForceToRefresh();
        }
    }
}
