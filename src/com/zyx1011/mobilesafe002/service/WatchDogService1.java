package com.zyx1011.mobilesafe002.service;

import java.util.ArrayList;
import java.util.List;

import com.zyx1011.mobilesafe002.LockScreenActivity;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.db.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

public class WatchDogService1 extends Service {

	private ActivityManager mAm;
	private boolean mFlag;
	private AppLockDao mDao;
	private List<String> mAll;
	private List<String> mFree;
	private LockScreenReceiver mReceiver;
	private String mPackageName;
	private WatchContentOberver mObserver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mAm = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		mDao = new AppLockDao(getApplicationContext());
		mAll = mDao.getAll();

		mFree = new ArrayList<>();

		mReceiver = new LockScreenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Constants.BROADCAST_ACTION);
		registerReceiver(mReceiver, filter);

		mObserver = new WatchContentOberver(new Handler());
		getContentResolver().registerContentObserver(Constants.APPLOCK_URI, true, mObserver);

		scanProcess();
	}

	private void scanProcess() {
		mFlag = true;

		new Thread(new Runnable() {
			public void run() {
				while (mFlag) {
					@SuppressWarnings("deprecation")
					List<RunningTaskInfo> tasks = mAm.getRunningTasks(1);
					if (tasks != null && tasks.size() > 0) {
						RunningTaskInfo taskInfo = tasks.get(0);
						ComponentName componentName = taskInfo.topActivity;
						String packageName = componentName.getPackageName();

						if (mFree.contains(packageName)) {
							continue; // 放行
						}

						if (mPackageName != null) {
							if (!mPackageName.equals(packageName) && !packageName.equals(getPackageName())) {
								mFree.remove(mPackageName); // 打开了其他应用,重新上锁
							}
						}

						if (mAll.contains(packageName)) {
							Intent intent = new Intent(getApplicationContext(), LockScreenActivity.class);
							intent.putExtra("PACKAGE_NAME", packageName);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 设置标记,避免报错
							startActivity(intent);
						}
					}

					SystemClock.sleep(500); // 延迟0.5秒
				}
			}
		}).start();
	}

	private class LockScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				mFlag = false; // 停止电子狗内的循环
				mFree.clear(); // 清空放行集合
			} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
				scanProcess(); // 重新启动电子狗监听
			} else if (Constants.BROADCAST_ACTION.equals(action)) {
				mPackageName = intent.getStringExtra("PACKAGE_NAME");
				if (mPackageName != null) {
					mFree.add(mPackageName);
				}
			}
		}
	}

	private class WatchContentOberver extends ContentObserver {

		public WatchContentOberver(Handler handler) {
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		mFlag = false;

		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}

		if (mObserver != null) { // 反注册
			getContentResolver().unregisterContentObserver(mObserver);
			mObserver = null;
		}
	}

}
