package com.zyx1011.mobilesafe002.view;

import com.zyx1011.mobilesafe002.R;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class AddrToast extends Toast implements OnTouchListener {

	private Context mContext;
	private WindowManager mWm;
	private LayoutParams mParams;
	private View mView;
	private float mStartX;
	private float mStartY;

	public AddrToast(Context context) {
		super(context);
		this.mContext = context;

		mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mParams = new WindowManager.LayoutParams();

		// 设置布局参数
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.format = PixelFormat.TRANSLUCENT;
		mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	}

	/**
	 * 显示来电归属地框
	 * 
	 * @param msg
	 */
	public void show(String msg) {
		if (mView == null) {
			mView = View.inflate(mContext, R.layout.view_addrtoast, null);
		}

		// 设置背景样式
		mView.setBackgroundResource(
				PreferenceUtils.getInt(mContext, Constants.ADDRDIALOG.SELECTED, R.drawable.iv_addrtoast_normal));
		TextView tvArea = (TextView) mView.findViewById(R.id.tv_view_address_area);
		tvArea.setText(msg); // 设置显示的文本
		mView.setOnTouchListener(this); // 设置触摸监听

		mWm.addView(mView, mParams);
	}

	/**
	 * 隐藏来电归属地框
	 */
	public void hide() {
		if (mView != null) {
			if (mView.getParent() != null) {
				mWm.removeView(mView);
			}
			mView = null;
		}
	}

	/**
	 * 响应触摸事件,移动来电归属地框
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mStartX = event.getRawX();
			mStartY = event.getRawY();
			break;

		case MotionEvent.ACTION_MOVE:
			float endX = event.getRawX();
			float endY = event.getRawY();

			int dx = (int) (endX - mStartX + 0.5f);
			int dy = (int) (endY - mStartY + 0.5f);

			mParams.x += dx;
			mParams.y += dy;

			mWm.updateViewLayout(mView, mParams);

			mStartX = endX;
			mStartY = endY;
			break;

		case MotionEvent.ACTION_UP:
			break;

		default:
			break;
		}
		return true;
	}

}
