package com.zyx1011.mobilesafe002.service;

import com.zyx1011.mobilesafe002.MainActivity;
import com.zyx1011.mobilesafe002.R;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

/**
 * 保护服务,常驻状态栏
 * 
 * @author zhongyuxin
 */
public class ProtectService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Notification notification = new Notification();
		notification.icon = R.drawable.main_icon;
		notification.tickerText = "黑马手机卫士正在运行...";
		Intent intent = new Intent(this, MainActivity.class);
		notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentView = new RemoteViews(getPackageName(), R.layout.service_protectservice_notification);
		startForeground(11, notification); // 启动为前台进程
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(true); // 结束前台进程
	}

}
