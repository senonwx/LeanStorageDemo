package com.senon.leanstoragedemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.senon.leanstoragedemo.R;
import com.senon.leanstoragedemo.adapter.CommonAdapter;
import com.senon.leanstoragedemo.adapter.ViewHolder;
import com.senon.leanstoragedemo.entity.Student;
import com.senon.leanstoragedemo.util.AVUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 学生列表 多选
 */
public class DialogSelectStu extends Dialog {

	private Context mContext;
	private ListView listView;
	private List<AVObject> totalStu = new ArrayList<>();
	private List<Student> selectStu = new ArrayList<>();
	private TextView sure_tv;
	private OnItemClickListener listener;
	private CommonAdapter adapter;

	public DialogSelectStu(Context context, List<Student> list, OnItemClickListener listener) {
		super(context, R.style.comment_dialog);
		this.mContext = context;
		this.selectStu.addAll(list);
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_popups);
		initWindowParams();
		initView();
		getOrderList();
	}

	private void initWindowParams() {
		Window dialogWindow = getWindow();
		// 获取屏幕宽、高用
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (display.getWidth() * 0.75); // 宽度设置为屏幕的0.65

		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow.setAttributes(lp);
	}

	private void initView() {
		sure_tv = (TextView) findViewById(R.id.sure_tv);
		sure_tv.setVisibility(View.VISIBLE);
		listView = (ListView) findViewById(R.id.listView);
		adapter = new CommonAdapter<AVObject>(mContext,totalStu,R.layout.dialog_selectstu_item) {
			@Override
			public void convert(final ViewHolder helper, AVObject item, final int position) {
				final Student student = (Student) item;
				CheckBox checkBox = helper.getView(R.id.checkbox);
				helper.setText(R.id.content_tv,student.getName());

				checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
						if(b){
							if(!selectStu.contains(student)){
								selectStu.add(student);
							}
						}else{
							selectStu.remove(student);
						}
					}
				});
				checkBox.setChecked(selectStu.contains(student) ? true : false);

			}
		};
		listView.setAdapter(adapter);

		sure_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(listener != null){
					listener.onItemClick(selectStu);
					dismiss();
				}
			}
		});
	}

	private void getOrderList() {
		AVQuery<Student> student = AVObject.getQuery(Student.class);
		student.orderByAscending("createdAt");//按照创建时间升序排列
		student.limit(100);// 最多返回 10 条结果
		student.skip(0);// 跳过 limit * 当前页数 条结果
		new AVUtil<>(mContext).setOnAVUtilListener(student, new AVUtil.OnAVUtilListener() {
			@Override
			public void onSuccess(List<AVObject> list) {
				totalStu.clear();
				totalStu.addAll(list);
				adapter.notifyDataSetChanged();
			}
		});
	}

	public interface OnItemClickListener{
		void onItemClick(List<Student> students);
	}
	public void setOnItemClickListener(OnItemClickListener listener){
		this.listener = listener;
	}

}
