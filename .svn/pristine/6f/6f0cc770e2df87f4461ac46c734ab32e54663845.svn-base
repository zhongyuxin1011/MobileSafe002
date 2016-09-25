package com.zyx1011.mobilesafe002.service;

import com.zyx1011.mobilesafe002.R;
import com.zyx1011.mobilesafe002.engine.ProcessProvider;
import com.zyx1011.mobilesafe002.receiver.WidgetReceiver;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class CleanService extends Service {

	private AppWidgetManager mManager;
	private ComponentName mName;
	private boolean mIsRunning;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mManager = AppWidgetManager.getInstance(this);
		mName = new ComponentName(this, WidgetReceiver.class);

		cleanProcess();
	}

	private void cleanProcess() {
		new Thread(new Runnable() {
			public void run() {
				mIsRunning = true;
				while (mIsRunning) {
					RemoteViews views = new RemoteViews(getPackageName(), R.layout.view_widget_process);

					int size = ProcessProvider.getAllRunningProcessInfo(getApplicationContext()).size();
					views.setTextViewText(R.id.tv_widget_process, "正在运行进程:" + size + "个");

					long availMen = ProcessProvider.getAvailMen(getApplicationContext());
					String fileSize = Formatter.formatFileSize(getApplicationContext(), availMen);
					views.setTextViewText(R.id.tv_widget_availMen, "可用内存:" + fileSize);

					Intent intent = new Intent();
					intent.setAction("com.zyx1011.mobilesafe002.provider.cleanprocess");
					PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 11, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					views.setOnClickPendingIntent(R.id.tv_widget_clean, pendingIntent);

					mManager.updateAppWidget(mName, views);

					SystemClock.sleep(1000 * 5);
				}
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mIsRunning = false;
	}

}
