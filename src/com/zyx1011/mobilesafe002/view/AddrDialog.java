package com.zyx1011.mobilesafe002.view;

import com.zyx1011.mobilesafe002.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AddrDialog extends Dialog {

	private ListView mLvList;

	private OnItemClickListener mOnItemClickListener;
	private ListAdapter mListAdapter;

	public AddrDialog(Context context) {
		super(context, R.style.Address_Style);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_addrdialog_content);
		mLvList = (ListView) findViewById(R.id.lv_addrdialog_list);

		if (mListAdapter != null) {
			mLvList.setAdapter(mListAdapter);
		}
		if (mOnItemClickListener != null) {
			mLvList.setOnItemClickListener(mOnItemClickListener);
		}

		Window window = getWindow();
		LayoutParams params = window.getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		window.setAttributes(params);
	}

	/**
	 * 设置item点击事件
	 * 
	 * @param onItemClickListener
	 */
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}

	/**
	 * 设置适配器
	 * 
	 * @param listAdapter
	 */
	public void setAdapter(ListAdapter listAdapter) {
		mListAdapter = listAdapter;
	}

}
