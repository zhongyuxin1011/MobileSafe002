package com.zyx1011.mobilesafe002.view;

import com.zyx1011.mobilesafe002.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressDes extends LinearLayout {

	private TextView mTvData;
	private ProgressBar mPbShow;
	private TextView mTvUsed;
	private TextView mTvFree;

	public ProgressDes(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View.inflate(context, R.layout.view_progressdes, this);

		mTvData = (TextView) findViewById(R.id.tv_progressdes_type);
		mPbShow = (ProgressBar) findViewById(R.id.pb_progressdes_show);
		mTvUsed = (TextView) findViewById(R.id.tv_progressdes_used);
		mTvFree = (TextView) findViewById(R.id.tv_progressdes_free);
		
		enableType(true);
	}

	public void enableType(boolean isEnable) {
		if (isEnable) {
			mTvData.setVisibility(View.VISIBLE);
		} else {
			mTvData.setVisibility(View.GONE);
		}
	}

	public void setType(String type) {
		mTvData.setText(type);
	}

	public void setProgress(long used, long total) {
		mPbShow.setProgress((int) (used * 100 / total));
	}

	public void setUsed(String used) {
		mTvUsed.setText(used);
	}

	public void setFree(String free) {
		mTvFree.setText(free);
	}

	public ProgressDes(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProgressDes(Context context) {
		this(context, null);
	}

}
