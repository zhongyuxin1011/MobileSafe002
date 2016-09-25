package com.zyx1011.mobilesafe002;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.engine.SmsProvider;
import com.zyx1011.mobilesafe002.engine.SmsProvider.SmsListener;
import com.zyx1011.mobilesafe002.service.WatchDogService1;
import com.zyx1011.mobilesafe002.service.WatchDogService2;
import com.zyx1011.mobilesafe002.utils.PasswordUtils;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;
import com.zyx1011.mobilesafe002.utils.ServiceStatusUtils;
import com.zyx1011.mobilesafe002.view.SettingItemView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

@ContentView(R.layout.activity_toolsmanager)
public class ToolsManagerActivity extends Activity {

	private ProgressDialog mDialog;

	@ViewInject(R.id.siv_toolsmanager_watchdog1)
	private SettingItemView mSivDog1;

	@ViewInject(R.id.siv_toolsmanager_watchdog2)
	private SettingItemView mSivDog2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		initView();
		initEvent();
	}

	/**
	 * 初始化电子狗服务状态显示
	 */
	private void initView() {
		mSivDog1.check(ServiceStatusUtils.isRunningService(this, WatchDogService1.class));
		mSivDog2.check(ServiceStatusUtils.isRunningService(this, WatchDogService2.class));
	}

	private void initEvent() {
		mDialog = new ProgressDialog(ToolsManagerActivity.this);
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 点击跳转后的回显
		boolean runningService = ServiceStatusUtils.isRunningService(this, WatchDogService2.class);
		mSivDog2.check(runningService);

		Intent intent = new Intent(this, WatchDogService1.class);
		if (runningService) { // 如果电子狗2开启了句关闭电子狗1
			stopService(intent);
			PreferenceUtils.putBoolean(getApplicationContext(), Constants.WATCH_DOG1, false);
		}
		// 更新电子狗1的显示
		mSivDog1.check(ServiceStatusUtils.isRunningService(getApplicationContext(), WatchDogService1.class));
	}

	/**
	 * 按钮点击响应事件
	 * 
	 * @param v
	 */
	public void onToolsClick(View v) {
		switch (v.getId()) {
		case R.id.siv_toolsmanager_phonearea: // 归属地查询
			Intent area = new Intent(this, PhoneAreaActivity.class);
			startActivity(area);
			break;

		case R.id.siv_toolsmanager_phonenum: // 常用号码查询
			Intent num = new Intent(this, PublicPhoneActivity.class);
			startActivity(num);
			break;

		case R.id.siv_toolsmanager_applock: // 程序锁管理
			PasswordUtils.checkPass(this, AppLockActivity.class);
			break;

		case R.id.siv_toolsmanager_smsbackup: // 短信备份
			mDialog.show();

			SmsProvider.smsBackup(getApplicationContext(), new SmsListener() {

				@Override
				public void onSuccess() {
					Toast.makeText(getApplicationContext(), "备份成功!", Toast.LENGTH_SHORT).show();
					mDialog.dismiss();
				}

				@Override
				public void onProgress(int progress) {
					mDialog.setProgress(progress);
				}

				@Override
				public void onMax(int max) {
					mDialog.setMax(max);
				}

				@Override
				public void onFail(String errorDes) {
					Toast.makeText(getApplicationContext(), "备份失败:" + errorDes, Toast.LENGTH_SHORT).show();
					mDialog.dismiss();
				}
			});
			break;

		case R.id.siv_toolsmanager_smsrestore: // 短信还原
			mDialog.show();

			SmsProvider.smsRestore(getApplicationContext(), new SmsListener() {

				@Override
				public void onSuccess() {
					Toast.makeText(getApplicationContext(), "还原成功!", Toast.LENGTH_SHORT).show();
					mDialog.dismiss();
				}

				@Override
				public void onProgress(int progress) {
					mDialog.setProgress(progress);
				}

				@Override
				public void onMax(int max) {
					mDialog.setMax(max);
				}

				@Override
				public void onFail(String errorDes) {
					Toast.makeText(getApplicationContext(), "还原失败:" + errorDes, Toast.LENGTH_SHORT).show();
					mDialog.dismiss();
				}
			});
			break;

		case R.id.siv_toolsmanager_watchdog1: // 电子狗服务1
			Intent watchDog1 = new Intent(this, WatchDogService1.class);
			if (ServiceStatusUtils.isRunningService(getApplicationContext(), WatchDogService1.class)) {
				stopService(watchDog1);

				PreferenceUtils.putBoolean(getApplicationContext(), Constants.WATCH_DOG1, false);
			} else {
				startService(watchDog1);

				PreferenceUtils.putBoolean(getApplicationContext(), Constants.WATCH_DOG1, true);
			}

			mSivDog1.check(PreferenceUtils.getBoolean(getApplicationContext(), Constants.WATCH_DOG1));
			break;

		case R.id.siv_toolsmanager_watchdog2: // 电子狗服务2
			Intent watchDog2 = new Intent();
			watchDog2.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
			startActivity(watchDog2);
			break;

		default:
			break;
		}
	}

}
