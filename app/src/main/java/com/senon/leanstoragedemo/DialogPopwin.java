package com.senon.leanstoragedemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.senon.leanstoragedemo.adapter.CommonAdapter;
import com.senon.leanstoragedemo.adapter.ViewHolder;

import java.util.List;


/**
 * 长按对话框，保护复制和删除
 */
public class DialogPopwin extends Dialog {

	private Context mContext;
	private ListView listView;
	private List<String> list;
	private OnItemClickListener listener;

	public DialogPopwin(Context context, List<String> list, OnItemClickListener listener) {
		super(context, R.style.comment_dialog);
		this.mContext = context;
		this.list = list;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_popups);
		initWindowParams();
		initView();
	}

	private void initWindowParams() {
		Window dialogWindow = getWindow();
		// 获取屏幕宽、高用
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (display.getWidth() * 0.65); // 宽度设置为屏幕的0.65

		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow.setAttributes(lp);
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new CommonAdapter<String>(mContext,list,R.layout.dialog_popups_item) {
			@Override
			public void convert(ViewHolder helper, String item, final int position) {
				helper.setText(R.id.content_tv,item);
				helper.setVisible(R.id.divider_view, View.VISIBLE);
				if(position == list.size() - 1){
					helper.setVisible(R.id.divider_view, View.GONE);
				}
				helper.setOnClickListener(R.id.content_tv, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						listener.onItemClick(position);
						dismiss();
					}
				});
			}
		});
	}

	public interface OnItemClickListener{
		void onItemClick(int position);
	}
	public void setOnItemClickListener(OnItemClickListener listener){
		this.listener = listener;
	}

}
