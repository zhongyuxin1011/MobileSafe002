package com.zyx1011.mobilesafe002.view;

import com.zyx1011.mobilesafe002.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SegmentView extends LinearLayout implements OnClickListener {

	private TextView mTvUnlock;
	private TextView mTvLock;
	private boolean mIsLeft;
	private onSelectedListener mListener;

	public SegmentView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		View.inflate(context, R.layout.view_segmentview, this);

		mTvUnlock = (TextView) findViewById(R.id.tv_segment_unlock);
		mTvLock = (TextView) findViewById(R.id.tv_segment_lock);

		mTvUnlock.setOnClickListener(this);
		mTvLock.setOnClickListener(this);

		mIsLeft = true;
		mTvUnlock.setSelected(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_segment_unlock:
			if (!mIsLeft) {
				mIsLeft = true;
				if (mListener != null) {
					mListener.OnSelected(mIsLeft);
				}
				mTvUnlock.setSelected(true);
				mTvLock.setSelected(false);
			}
			break;

		case R.id.tv_segment_lock:
			if (mIsLeft) {
				mIsLeft = false;
				if (mListener != null) {
					mListener.OnSelected(mIsLeft);
				}
				mTvUnlock.setSelected(false);
				mTvLock.setSelected(true);
			}
			break;

		default:
			break;
		}
	}

	public interface onSelectedListener {
		void OnSelected(boolean isLeft);
	}

	public void setOnSelectedListener(onSelectedListener listener) {
		this.mListener = listener;
	}

	public SegmentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SegmentView(Context context) {
		this(context, null);
	}

}
