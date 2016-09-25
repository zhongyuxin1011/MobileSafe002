package com.zyx1011.mobilesafe002;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * 手机防盗页面设置的基类
 * 
 * @author zhongyuxin
 */
public abstract class BaseProtectActivity extends Activity {

	private GestureDetector mDetector;

	public abstract boolean performPre(); // true返回,false不返回

	public abstract boolean performNext();

	private class MyGestureDetector extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (e1.getX() - e2.getX() > 50) {
				nextClick(null);
				return true;
			}

			if (e2.getX() - e1.getX() > 50) {
				preClick(null);
				return true;
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDetector = new GestureDetector(this, new MyGestureDetector());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event); // 把触摸事件转给手势识别器
		return super.onTouchEvent(event);
	}

	public void preClick(View v) { // 上一步
		if (performPre()) {
			return; // 返回而不跳转
		}
		finish(); // 结束当前的Activity
		overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
	}

	public void nextClick(View v) { // 下一步
		if (performNext()) {
			return;
		}
		finish();
		overridePendingTransition(R.anim.next_in, R.anim.next_out);
	}
}
