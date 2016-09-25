package com.zyx1011.mobilesafe002;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.entity.VersionInfo;
import com.zyx1011.mobilesafe002.service.AddressService;
import com.zyx1011.mobilesafe002.service.CleanService;
import com.zyx1011.mobilesafe002.service.InterceptService;
import com.zyx1011.mobilesafe002.service.LockScreenService;
import com.zyx1011.mobilesafe002.service.ProtectService;
import com.zyx1011.mobilesafe002.service.WatchDogService1;
import com.zyx1011.mobilesafe002.utils.CloseableUtils;
import com.zyx1011.mobilesafe002.utils.GZIPUtils;
import com.zyx1011.mobilesafe002.utils.JSONUtils;
import com.zyx1011.mobilesafe002.utils.PackageUtils;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;
import com.zyx1011.mobilesafe002.utils.ServiceStatusUtils;
import com.zyx1011.mobilesafe002.utils.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	@ViewInject(R.id.tv_splash_version)
	private TextView mVersionName;

	private VersionInfo mVersionInfo; // 存储新版本信息的对象
	private AlertDialog.Builder mUpdateDialog;
	private String mDescPath; // 下载新版本文件的路径
	private Message mMessage;

	// 消息处理
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.HTTP_CONN.VERSION_UPDATE_Y:
				try {
					// 比较版本号
					if (mVersionInfo.getVersionCode() > PackageUtils.getVersionCode(SplashActivity.this)) {
						// 弹出对话框
						mUpdateDialog = new Builder(SplashActivity.this).setIcon(R.drawable.heima)
								.setTitle("发现新版本" + mVersionInfo.getVersionName())
								.setMessage(mVersionInfo.getdescribe())
								.setPositiveButton("马上升级", new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										downApk();
										dialog.dismiss();
									}
								}).setNegativeButton("下次提醒", new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										loadMain();
									}
								}).setCancelable(false); // 不可返回关闭
						mUpdateDialog.show();
					} else {
						loadMain();
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
					// 获取包信息失败
				}
				break;

			case Constants.HTTP_CONN.VERSION_UPDATE_N:
				loadMain();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ViewUtils.inject(this);

		try {
			// 设置版本名显示
			mVersionName.setText(PackageUtils.getVersionName(this));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		// 根据设置里的"版本更新"值判断是否需要联网检查版本
		if (PreferenceUtils.getBoolean(this, Constants.MAIN_MENU.VERSION_UPDATE)) {
			SystemClock.sleep(1000); // 启动后延迟一秒再检查版本更新
			checkUpdate();
		} else {
			loadMain();
		}

		initDB("address.db");
		initDB("commonnum.db");
		initDB("antivirus.db");

		checkService(Constants.PROTECT_SERVICE, ProtectService.class); // 状态栏
		checkService(Constants.MAIN_MENU.INTERCEPT_HARRY, InterceptService.class); // 骚扰拦截
		checkService(Constants.MAIN_MENU.PHONE_AREA, AddressService.class); // 来电归属地
		checkService(Constants.LOCK_SCREEN_CLEAN, LockScreenService.class); // 锁屏清理
		checkService(Constants.WATCH_DOG1, WatchDogService1.class); // 电子狗服务1

		// Widget
		if (PreferenceUtils.getInt(this, Constants.WIDGET_SERVICE) == 1) {
			if (!ServiceStatusUtils.isRunningService(this, CleanService.class)) {
				Intent service = new Intent(this, CleanService.class);
				startService(service);
			}
		}
	}

	/**
	 * 检查守护服务是否开启
	 */
	private void checkService(String key, Class<? extends Service> cls) {
		if (PreferenceUtils.getBoolean(this, key)) {
			if (!ServiceStatusUtils.isRunningService(this, cls)) {
				Intent service = new Intent(this, cls);
				startService(service);
			}
		}
	}

	/**
	 * 初始化来电归属地数据库
	 */
	private void initDB(final String dbName) {
		final File file = new File(getFilesDir(), dbName); // 获取file实例/data/data<package>/files/address.db
		if (!file.exists()) { // 不存在则解压一份
			new Thread(new Runnable() { // 让子线程去做
				public void run() {
					InputStream src = null;
					OutputStream dest = null;
					try { // address.db
						if ("address.db".equals(dbName)) {
							src = getAssets().open("address.zip"); // 获取asset目录下的address.zip
							dest = new FileOutputStream(file);

							GZIPUtils.ungzip(src, dest); // 解压
						} else { // commonnum.db和antivirus.db
							src = getAssets().open(dbName);
							dest = new FileOutputStream(file);

							byte[] b = new byte[1024];
							int len;
							while ((len = src.read(b)) != -1) {
								dest.write(b, 0, len);
							}

						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							CloseableUtils.close(dest);
							CloseableUtils.close(src);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							src = null;
						}
					}
				}
			}).start();
		}
	}

	/**
	 * 检查是否有版本更新
	 */
	private void checkUpdate() {
		// 子线程访问网络
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection conn = null;
				try {
					URL url = new URL(Constants.HTTP_CONN.UPDATE_URL);
					conn = (HttpURLConnection) url.openConnection(); // 创建网络连接
					conn.setRequestMethod("GET"); // 设置请求方式
					conn.setConnectTimeout(2000); // 设置连接超时
					conn.setReadTimeout(2000); // 设置读取超时

					if (conn.getResponseCode() == 200) { // 判断响应码
						InputStream is = conn.getInputStream(); // 获取输入流
						String json = StringUtils.getString(is); // 专为字符串值
						mVersionInfo = JSONUtils.getVersionInfo(json); // 封装成Bean对象

						mMessage = Message.obtain();
						mMessage.what = Constants.HTTP_CONN.VERSION_UPDATE_Y;
						handler.sendMessage(mMessage);
					} else {
						// 无法获取数据
					}
				} catch (Exception e) {
					e.printStackTrace();
					// 网络连接异常,输入流关闭异常,JSON解析异常等
					mMessage = Message.obtain();
					mMessage.what = Constants.HTTP_CONN.VERSION_UPDATE_N;
					handler.sendMessage(mMessage);
				} finally {
					if (conn != null) {
						conn.disconnect(); // 断开连接
						conn = null;
					}
				}
			}
		}).start();
	}

	/**
	 * 加载主界面
	 */
	private void loadMain() {
		// 延时加载
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				SplashActivity.this.finish(); // 关闭当前的Activity
			}
		}, 3000); // 延时3秒
	}

	/**
	 * 下载新版本APK
	 */
	private void downApk() {
		String srcPath = Constants.HTTP_CONN.HOST + "/MobileSafe002_" + mVersionInfo.getVersionCode() + ".apk";
		mDescPath = getStoragePath(srcPath.substring(srcPath.lastIndexOf("/") + 1));

		deleteAPK();

		final ProgressDialog downDialog = new ProgressDialog(SplashActivity.this);
		downDialog.setTitle("正在下载...");
		downDialog.setIcon(android.R.drawable.stat_sys_download);
		downDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // 设置显示式样为水平进度条
		downDialog.show();

		if (mDescPath != null) {
			HttpUtils downUtils = new HttpUtils(); // xutils的下载功能
			downUtils.download(srcPath, mDescPath, new RequestCallBack<File>() {

				@Override
				public void onSuccess(ResponseInfo<File> results) {
					downDialog.dismiss();

					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction("android.intent.action.VIEW");
					intent.setDataAndType(Uri.fromFile(results.result), "application/vnd.android.package-archive");
					startActivityForResult(intent, Constants.HTTP_CONN.UPDATE_INSTALL_CODE);
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// 下载失败
					Toast.makeText(SplashActivity.this, "下载失败,请稍后重试!", Toast.LENGTH_SHORT).show();
					downDialog.dismiss();
					loadMain();
				}

				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					downDialog.setMax((int) total);
					downDialog.setProgress((int) current);
				}
			});
		}
	}

	/**
	 * 获取存储卡中要存放APK的位置
	 * 
	 * @return
	 */
	private String getStoragePath(String apkName) {
		// 判断存储卡是否挂载
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory(), "Download");

			if (!file.exists()) { // 如果不存在Download文件夹则创建
				file.mkdir();
			}
			return file.getPath() + "/" + apkName; // 返回存储路径
		} else {
			Toast.makeText(SplashActivity.this, "下载失败,存储卡不可用!", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constants.HTTP_CONN.UPDATE_INSTALL_CODE:
			if (resultCode == Activity.RESULT_CANCELED) {
				loadMain();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 如果下载存放路径下已存在相同APK,则删除原来的
	 */
	private void deleteAPK() {
		// 安装完后删除安装包
		if (mDescPath != null) {
			File file = new File(mDescPath);
			if (file.exists()) {
				file.delete();
			}
		}
	}

}
