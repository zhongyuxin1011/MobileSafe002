package com.zyx1011.mobilesafe002.service;

import java.util.ArrayList;
import java.util.List;

import com.zyx1011.mobilesafe002.LockScreenActivity;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.db.AppLockDao;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;

public class WatchDogService2 extends AccessibilityService {

	private WatchDogReceiver mReceiver;
	private AppLockDao mDao;
	private List<String> mAll;
	private List<String> mFree;
	private String mPackageName;
	private WatchDogObserver mObserver;

	@Override
	protected void onServiceConnected() {
		super.onServiceConnected();

		mFree = new ArrayList<>();

		// 注册广播
		mReceiver = new WatchDogReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Constants.BROADCAST_ACTION);
		registerReceiver(mReceiver, filter);

		mDao = new AppLockDao(getApplicationContext());
		mAll = mDao.getAll();

		mObserver = new WatchDogObserver(new Handler());
		getContentResolver().registerContentObserver(Constants.APPLOCK_URI, true, mObserver);
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		String packageName = event.getPackageName().toString();
		if (mFree.contains(packageName)) {
			return; // 放行
		}

		if (mPackageName != null) {
			if (!mPackageName.equals(packageName) && !packageName.equals(getPackageName())) {
				mFree.remove(mPackageName); // 打开了其他应用,重新上锁之前的应用
			}
		}

		if (mAll.contains(packageName)) {
			// 启动程序锁界面
			Intent intent = new Intent(getApplicationContext(), LockScreenActivity.class);
			intent.putExtra("PACKAGE_NAME", packageName);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 设置标记,避免报错
			startActivity(intent);
		}
	}

	@Override
	public void onInterrupt() {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}

		if (mObserver != null) {
			getContentResolver().unregisterContentObserver(mObserver);
			mObserver = null;
		}
	}

	private class WatchDogObserver extends ContentObserver {

		public WatchDogObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			if (mDao != null) {
				mAll = mDao.getAll();
			}
		}

	}

	private class WatchDogReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String packageName = intent.getStringExtra("PACKAGE_NAME");
			if (action.equals(Intent.ACTION_SCREEN_OFF)) {
				mFree.clear();
			} else if (action.equals(Constants.BROADCAST_ACTION)) {
				if (!TextUtils.isEmpty(packageName)) {
					mPackageName = packageName; // 保存当前的包名
					mFree.add(packageName);
				}
			}
		}
	}

}
