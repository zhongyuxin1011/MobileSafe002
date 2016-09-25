package com.zyx1011.mobilesafe002.service;

import java.util.List;

import com.zyx1011.mobilesafe002.engine.ProcessProvider;
import com.zyx1011.mobilesafe002.entity.ProcessInfo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

public class LockScreenService extends Service {

	private LockScreenReceiver mReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mReceiver = new LockScreenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	private class LockScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			SystemClock.sleep(5000); // 延时5秒再清理进程

			List<ProcessInfo> infos = ProcessProvider.getAllRunningProcessInfo(context);
			if (infos != null && infos.size() > 0) {
				for (ProcessInfo info : infos) {
					if (!info.getProcessName().equals(context.getPackageName()) && !info.isGodProcess()) {
						ProcessProvider.killProcess(context, info.getProcessName());
					}
				}
			}
		}
	}

}
